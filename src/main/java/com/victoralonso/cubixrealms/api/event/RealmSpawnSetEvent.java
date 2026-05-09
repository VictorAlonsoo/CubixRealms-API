package com.victoralonso.cubixrealms.api.event;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Fired after a realm's custom spawn point has been saved.
 */
public final class RealmSpawnSetEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID ownerUuid;
    private final Location newSpawn;

    public RealmSpawnSetEvent(UUID ownerUuid, Location newSpawn) {
        this.ownerUuid = ownerUuid;
        this.newSpawn  = newSpawn;
    }

    public UUID getOwnerUuid()    { return ownerUuid; }
    public Location getNewSpawn() { return newSpawn; }

    @Override public HandlerList getHandlers()   { return HANDLERS; }
    public static HandlerList getHandlerList()   { return HANDLERS; }
}
