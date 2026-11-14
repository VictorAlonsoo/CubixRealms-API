# CubixRealms API — Changes

Public API changelog (events, models, `CubixRealmsAPI`). Audience: third-party plugins
consuming the API through JitPack — `com.github.VictorAlonsoo:CubixRealms-API:<version>`,
where the version is a Git tag of [CubixRealms-API](https://github.com/VictorAlonsoo/CubixRealms-API).

> Maintainers: the source of truth is the `cubixrealms-api` module of the plugin
> repository. `changelogs/REVIEW-CURSOR.md` there records the last commit whose API
> surface was reviewed.

## 2.3.0

Additive only: addons compiled against 2.2.0 keep working without recompiling.

### Added

- `CubixRealmsAPI#hasRealm(UUID ownerUuid)` — whether the player owns a realm.
- `CubixRealmsAPI#getRealms()` — `Collection<RealmInfo>` snapshot of every realm.
- `CubixRealmsAPI#getDimension(World world)` — `Optional<RealmDimension>` of a realm world.
- `CubixRealmsAPI#getRealmAt(Player player)` — the realm the player is standing in.
- `CubixRealmsAPI#isActionAllowed(Player player, RealmAction action)` — the plugin's own
  protection decision (role flags and bypass); `true` outside realm worlds.
- `CubixRealmsAPI#teleportToRealm(Player player, UUID ownerUuid)` — standard teleport flow.
- `model.RealmAction` enum: `BUILD`, `INTERACT`, `CONTAINERS`, `DOORS`, `REDSTONE`, `PVP`,
  `DAMAGE_MOBS`, `ITEM_DROP`.

### Behaviour worth knowing

- `RealmEnterEvent` / `RealmLeaveEvent` now also fire when a player goes straight from one
  realm to another owner's realm (it used to count as a dimension change).

- `RealmDeleteEvent` still fires before a deletion is queued, and cancelling it
  still stops the deletion. The world folders are now deleted off the main thread,
  and if a world cannot be unloaded or removed the realm is **kept** (its record is
  no longer dropped with folders left behind). An uncancelled `RealmDeleteEvent` is
  therefore not a guarantee that the realm is gone — check `getRealmInfo` later if
  your addon needs certainty.
- `RealmResetDimensionEvent` behaves the same way: the reset finishes a few ticks
  after the event, and can still fail afterwards.

## 2.2.0

**No changes to the public API surface.**

The work in this cycle (realm activity tracking, automatic deletion of abandoned
realms, per-dimension Paper world settings, realm list sorting) is entirely
plugin-internal:

- `RealmInfo` is unchanged — the accumulated playtime and last-activity stamp are
  internal fields on the plugin's own `RealmProfile`, not exposed here.
- No event was added, removed or changed. Automatic deletions fire the existing
  `RealmDeleteEvent`, so an addon can already observe or cancel them; there is no
  flag distinguishing an automatic deletion from a manual one.
- `CubixRealmsAPI` keeps the same method signatures.

Addons compiled against 2.1.0 keep working without recompiling.

Javadoc: `isRealmWorld(World)` mentions the End, which it has matched since 2.0.0.

## 2.1.0

**No changes to the public API surface.** The module version moved with the plugin;
`cubixrealms-api/src` was not modified in this cycle (global realm time and weather,
admin time/weather commands, Bedrock invite forms and `/realm invites` are all
plugin-internal). Addons compiled against 2.0.0 keep working without recompiling.

## 2.0.0

### Added
- `RealmDimension.END` enum constant.
- `RealmInfo#endWorldName()` — returns the realm's End world name.

### Changed
- `RealmInfo` canonical record constructor now takes `endWorldName` as its
  5th component, inserted between `netherWorldName` and `visitorRule` —
  breaking for any code constructing `RealmInfo` positionally.
