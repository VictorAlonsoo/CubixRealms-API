package com.victoralonso.cubixrealms.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Fired after a realm's worlds have been loaded into memory.
 */
public final class RealmLoadEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID ownerUuid;

    public RealmLoadEvent(UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public UUID getOwnerUuid() { return ownerUuid; }

    @Override public HandlerList getHandlers()   { return HANDLERS; }
    public static HandlerList getHandlerList()   { return HANDLERS; }
}
