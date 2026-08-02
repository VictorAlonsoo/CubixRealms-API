# CubixRealms API

API pública en Java para que otros plugins de Paper se integren con
**CubixRealms** — mundos privados propiedad del jugador (Overworld, Nether y
End), roles, teleportes seguros y portales aislados por realm.

## Dónde conseguir CubixRealms

- **BuiltByBit:** https://builtbybit.com/resources/cubix-realms.108449/
- **Discord (soporte):** https://discord.gg/ueHSMWU4KA

## ¿Para quién es esta API?

Para desarrolladores de plugins/addons que quieran, sin tocar el almacenamiento
interno de CubixRealms:

- Leer datos públicos de un realm (dueño, nombres de mundo, si está cargado,
  regla de visitas).
- Consultar el rol de un jugador dentro de un realm y si está baneado.
- Comprobar si un visitante puede entrar a un realm y obtener el mensaje de
  denegación oficial (ya traducido, con el prefijo de CubixRealms).
- Cargar un realm bajo demanda si sus mundos no están en memoria.
- Reaccionar a eventos del ciclo de vida de un realm: creación, borrado,
  carga/descarga, entrada/salida, teletransporte, viaje entre dimensiones,
  cambio de rol, ban/unban, reset de dimensión y cambio de spawn.

Esta API **no** sirve para modificar la configuración del plugin ni para
acceder a datos internos de almacenamiento — es una fachada de solo lectura
más eventos.

## Instalación (Maven)

CubixRealms solo se distribuye vía Maven a través de JitPack:

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

`scope=provided` porque el JAR de la API ya viene incluido dentro del plugin
CubixRealms en el servidor — tu addon solo necesita las clases en tiempo de
compilación. Declara CubixRealms como dependencia blanda en tu
`paper-plugin.yml` / `plugin.yml`:

```yml
softdepend: [CubixRealms]
```

## Uso básico

La instancia es `null` antes de que CubixRealms se habilite y después de que
se deshabilite — compruébalo siempre:

```java
CubixRealmsAPI api = CubixRealmsAPI.get();
if (api != null) {
    Optional<RealmInfo> realm = api.getRealmInfo(ownerUuid);
    realm.ifPresent(info -> {
        player.sendMessage("Realm de " + info.ownerName() + " — cargado: " + info.loaded());
    });
}
```

Comprobar acceso antes de dejar visitar un realm:

```java
Optional<VisitDenialReason> denial = api.checkVisit(ownerUuid, visitor);
if (denial.isPresent()) {
    visitor.sendMessage(api.getVisitDenialMessage(denial.get(), ownerUuid));
    return;
}
```

## Ejemplos

Registra tu listener normalmente, con CubixRealms como `softdepend`. Todos
los eventos se disparan en el hilo principal.

### Creación de un realm

```java
@EventHandler
public void onRealmCreate(RealmCreateEvent event) {
    Bukkit.getLogger().info(event.getOwnerName() + " ha creado un realm.");
    // event.setCancelled(true); // evento cancelable
}
```

### Cambio de dimensión (entrada a Overworld/Nether/End)

```java
@EventHandler
public void onRealmEnter(RealmEnterEvent event) {
    if (event.getDimension() == RealmDimension.END) {
        event.getPlayer().sendMessage("Has entrado al End de este realm.");
    }
}
```

### Viaje por portal entre dimensiones del mismo realm

```java
@EventHandler
public void onPortalTravel(RealmPortalTravelEvent event) {
    if (event.getFrom() == RealmDimension.OVERWORLD && event.getTo() == RealmDimension.NETHER) {
        event.getPlayer().sendMessage("Cruzando al Nether de tu realm...");
    }
    // event.setCancelled(true); // evento cancelable
}
```

### Teletransporte al realm de otro jugador

```java
@EventHandler
public void onRealmTeleport(RealmTeleportEvent event) {
    Optional<RealmInfo> target = api.getRealmInfo(event.getTargetOwnerUuid());
    target.ifPresent(info ->
        event.getPlayer().sendMessage("Viajando al realm de " + info.ownerName() + "..."));
    // event.setCancelled(true); // evento cancelable
}
```

### Comprobación de visita manual (fuera de un evento)

```java
Optional<VisitDenialReason> denial = api.checkVisit(ownerUuid, visitor);
if (denial.isEmpty()) {
    // el visitante puede entrar
}
```

## Referencia rápida

### `CubixRealmsAPI`


| Método                                                           | Descripción                                                                                                                     |
| ----------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------- |
| `getRealmInfo(UUID ownerUuid)`                                    | Datos públicos del realm por dueño.                                                                                            |
| `getRealmByWorldName(String worldName)`                           | Datos públicos del realm por nombre de mundo, sin necesidad de que esté cargado.                                               |
| `getRealmOwner(World world)` / `getRealmOwner(String worldName)`  | Dueño del realm al que pertenece un mundo.                                                                                      |
| `isRealmWorld(World world)` / `isRealmWorld(String worldName)`    | Si un mundo pertenece a algún realm.                                                                                            |
| `isLoaded(UUID ownerUuid)`                                        | Si el overworld del realm está cargado en memoria.                                                                              |
| `getRoleName(UUID playerUuid, UUID ownerUuid)`                    | Rol del jugador dentro del realm (`OWNER`, `VISITOR`, o rol personalizado).                                                      |
| `isBanned(UUID playerUuid, UUID ownerUuid)`                       | Si el jugador está baneado del realm.                                                                                           |
| `getMembers(UUID ownerUuid)`                                      | Lista de miembros explícitos (no visitantes) del realm.                                                                         |
| `checkVisit(UUID ownerUuid, Player visitor)`                      | Aplica todo el pipeline de acceso (bypass, ban, regla de visitas, dueño offline) y devuelve el motivo de denegación si lo hay. |
| `getVisitDenialMessage(VisitDenialReason reason, UUID ownerUuid)` | Mensaje oficial de denegación, ya traducido.                                                                                    |
| `loadRealmAsync(UUID ownerUuid)`                                  | Carga los mundos del realm si no están cargados; se resuelve en el hilo principal.                                              |

### Modelos


| Clase               | Descripción                                                                                                     |
| ------------------- | ---------------------------------------------------------------------------------------------------------------- |
| `RealmInfo`         | Snapshot inmutable del realm: dueño, nombre, mundos (overworld/nether/end), regla de visitas, si está cargado. |
| `RealmMemberInfo`   | Snapshot de un miembro: UUID, rol, fecha de ingreso.                                                             |
| `RealmDimension`    | `OVERWORLD`, `NETHER`, `END`.                                                                                    |
| `VisitDenialReason` | `REALM_NOT_FOUND`, `BANNED`, `MEMBERS_ONLY`, `FRIENDS_ONLY`, `PRIVATE`, `OWNER_OFFLINE`.                         |

### Eventos (`org.bukkit.event.Event`, paquete `api.event`)


| Evento                     | Cancelable | Se dispara cuando...                                            |
| -------------------------- | ---------- | --------------------------------------------------------------- |
| `RealmCreateEvent`         | Sí        | Se crea un nuevo realm.                                         |
| `RealmDeleteEvent`         | Sí        | Se borra un realm.                                              |
| `RealmLoadEvent`           | No         | Los mundos de un realm se cargan en memoria.                    |
| `RealmUnloadEvent`         | No         | Los mundos de un realm se descargan.                            |
| `RealmEnterEvent`          | No         | Un jugador entra a una dimensión de un realm.                  |
| `RealmLeaveEvent`          | No         | Un jugador sale de una dimensión de un realm.                  |
| `RealmTeleportEvent`       | Sí        | Un jugador se teletransporta al realm de otro jugador.          |
| `RealmPortalTravelEvent`   | Sí        | Un jugador viaja entre dimensiones del mismo realm vía portal. |
| `RealmResetDimensionEvent` | Sí        | Se resetea una dimensión del realm.                            |
| `RealmRoleChangeEvent`     | Sí        | Cambia el rol de un miembro.                                    |
| `RealmBanEvent`            | Sí        | Se banea a un jugador del realm.                                |
| `RealmUnbanEvent`          | No         | Se le quita el ban a un jugador.                                |
| `RealmSpawnSetEvent`       | No         | El dueño fija el spawn de su realm.                            |
