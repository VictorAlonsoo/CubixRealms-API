package com.victoralonso.cubixrealms.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Fired before a new realm is created. Cancelling aborts the creation.
 */
public final class RealmCreateEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID ownerUuid;
    private final String ownerName;
    private boolean cancelled;

    public RealmCreateEvent(UUID ownerUuid, String ownerName) {
        this.ownerUuid = ownerUuid;
        this.ownerName = ownerName;
    }

    public UUID getOwnerUuid()   { return ownerUuid; }
    public String getOwnerName() { return ownerName; }

    @Override public boolean isCancelled()            { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers()         { return HANDLERS; }
    public static HandlerList getHandlerList()         { return HANDLERS; }
}
