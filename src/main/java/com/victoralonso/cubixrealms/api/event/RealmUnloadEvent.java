package com.victoralonso.cubixrealms.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Fired before a realm's worlds are unloaded from memory.
 */
public final class RealmUnloadEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID ownerUuid;

    public RealmUnloadEvent(UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public UUID getOwnerUuid() { return ownerUuid; }

    @Override public HandlerList getHandlers()   { return HANDLERS; }
    public static HandlerList getHandlerList()   { return HANDLERS; }
}
