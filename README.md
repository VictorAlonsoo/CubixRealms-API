# CubixRealms API

Public Java API for other Paper plugins to integrate with **CubixRealms** —
player-owned private worlds (Overworld, Nether and End), roles, safe
teleportation and per-realm isolated portal routing.

## Where to get CubixRealms

- **BuiltByBit:** https://builtbybit.com/resources/cubix-realms.108449/
- **Discord (support):** https://discord.gg/ueHSMWU4KA

## Who is this API for?

For plugin/addon developers who want to, without touching CubixRealms'
internal storage:

- Read public data about a realm (owner, world names, whether it's loaded,
  visitor rule).
- Look up a player's role inside a realm and whether they're banned.
- Check whether a visitor is allowed into a realm and get the official
  denial message (already translated, with the CubixRealms prefix).
- Load a realm on demand if its worlds aren't in memory.
- React to a realm's lifecycle events: creation, deletion, load/unload,
  enter/leave, teleport, travel between dimensions, role change, ban/unban,
  dimension reset and spawn change.

This API is **not** for modifying the plugin's configuration or accessing
internal storage data — it's a read-only facade plus events.

## Installation (Maven)

CubixRealms is only distributed via Maven through JitPack:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.victoralonso</groupId>
        <artifactId>cubixrealms-api</artifactId>
        <version>2.0.0</version>
        <scope>provided</scope>
        <optional>true</optional>
    </dependency>
</dependencies>
```

`scope=provided` because the API JAR already ships inside the CubixRealms
plugin on the server — your addon only needs the classes at compile time.
Declare CubixRealms as a soft dependency in your `paper-plugin.yml` /
`plugin.yml`:

```yml
softdepend: [CubixRealms]
```

## Basic usage

The instance is `null` before CubixRealms enables and after it disables —
always check it:

```java
CubixRealmsAPI api = CubixRealmsAPI.get();
if (api != null) {
    Optional<RealmInfo> realm = api.getRealmInfo(ownerUuid);
    realm.ifPresent(info -> {
        player.sendMessage("Realm owned by " + info.ownerName() + " — loaded: " + info.loaded());
    });
}
```

Check access before letting someone visit a realm:

```java
Optional<VisitDenialReason> denial = api.checkVisit(ownerUuid, visitor);
if (denial.isPresent()) {
    visitor.sendMessage(api.getVisitDenialMessage(denial.get(), ownerUuid));
    return;
}
```

## Examples

Register your listener normally, with CubixRealms as a `softdepend`. All
events fire on the main thread.

### Realm creation

```java
@EventHandler
public void onRealmCreate(RealmCreateEvent event) {
    Bukkit.getLogger().info(event.getOwnerName() + " created a realm.");
    // event.setCancelled(true); // cancellable event
}
```

### Dimension change (entering Overworld/Nether/End)

```java
@EventHandler
public void onRealmEnter(RealmEnterEvent event) {
    if (event.getDimension() == RealmDimension.END) {
        event.getPlayer().sendMessage("You entered this realm's End.");
    }
}
```

### Portal travel between dimensions of the same realm

```java
@EventHandler
public void onPortalTravel(RealmPortalTravelEvent event) {
    if (event.getFrom() == RealmDimension.OVERWORLD && event.getTo() == RealmDimension.NETHER) {
        event.getPlayer().sendMessage("Crossing into your realm's Nether...");
    }
    // event.setCancelled(true); // cancellable event
}
```

### Teleporting to another player's realm

```java
@EventHandler
public void onRealmTeleport(RealmTeleportEvent event) {
    Optional<RealmInfo> target = api.getRealmInfo(event.getTargetOwnerUuid());
    target.ifPresent(info ->
        event.getPlayer().sendMessage("Traveling to " + info.ownerName() + "'s realm..."));
    // event.setCancelled(true); // cancellable event
}
```

### Manual visit check (outside an event)

```java
Optional<VisitDenialReason> denial = api.checkVisit(ownerUuid, visitor);
if (denial.isEmpty()) {
    // the visitor is allowed in
}
```

## Quick reference

### `CubixRealmsAPI`

| Method | Description |
|---|---|
| `getRealmInfo(UUID ownerUuid)` | Public realm data by owner. |
| `getRealmByWorldName(String worldName)` | Public realm data by world name, without requiring it to be loaded. |
| `getRealmOwner(World world)` / `getRealmOwner(String worldName)` | Owner of the realm a world belongs to. |
| `isRealmWorld(World world)` / `isRealmWorld(String worldName)` | Whether a world belongs to any realm. |
| `isLoaded(UUID ownerUuid)` | Whether the realm's overworld is currently loaded in memory. |
| `getRoleName(UUID playerUuid, UUID ownerUuid)` | The player's role inside the realm (`OWNER`, `VISITOR`, or a custom role). |
| `isBanned(UUID playerUuid, UUID ownerUuid)` | Whether the player is banned from the realm. |
| `getMembers(UUID ownerUuid)` | List of explicit (non-visitor) members of the realm. |
| `checkVisit(UUID ownerUuid, Player visitor)` | Runs the full access pipeline (bypass, ban, visitor rule, offline owner) and returns the denial reason if any. |
| `getVisitDenialMessage(VisitDenialReason reason, UUID ownerUuid)` | Official, already-translated denial message. |
| `loadRealmAsync(UUID ownerUuid)` | Loads the realm's worlds if not already loaded; resolves on the main thread. |

### Models

| Class | Description |
|---|---|
| `RealmInfo` | Immutable realm snapshot: owner, name, worlds (overworld/nether/end), visitor rule, whether it's loaded. |
| `RealmMemberInfo` | Snapshot of a member: UUID, role, join date. |
| `RealmDimension` | `OVERWORLD`, `NETHER`, `END`. |
| `VisitDenialReason` | `REALM_NOT_FOUND`, `BANNED`, `MEMBERS_ONLY`, `FRIENDS_ONLY`, `PRIVATE`, `OWNER_OFFLINE`. |

### Events (`org.bukkit.event.Event`, package `api.event`)

| Event | Cancellable | Fires when... |
|---|---|---|
| `RealmCreateEvent` | Yes | A new realm is created. |
| `RealmDeleteEvent` | Yes | A realm is deleted. |
| `RealmLoadEvent` | No | A realm's worlds are loaded into memory. |
| `RealmUnloadEvent` | No | A realm's worlds are unloaded. |
| `RealmEnterEvent` | No | A player enters a realm dimension. |
| `RealmLeaveEvent` | No | A player leaves a realm dimension. |
| `RealmTeleportEvent` | Yes | A player teleports to another player's realm. |
| `RealmPortalTravelEvent` | Yes | A player travels between dimensions of the same realm via a portal. |
| `RealmResetDimensionEvent` | Yes | A realm dimension is reset. |
| `RealmRoleChangeEvent` | Yes | A member's role changes. |
| `RealmBanEvent` | Yes | A player is banned from the realm. |
| `RealmUnbanEvent` | No | A player is unbanned. |
| `RealmSpawnSetEvent` | No | The owner sets their realm's spawn. |

## Versioning

This module's versioning is independent from the main plugin
(`cubixrealms-api/pom.xml`, not the repo root) and follows SemVer. Public
surface changes (added/changed/removed methods, events, fields) are
documented in [`CHANGES.md`](./CHANGES.md).
