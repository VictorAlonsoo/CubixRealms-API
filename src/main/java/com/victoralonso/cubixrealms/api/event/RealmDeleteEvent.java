package com.victoralonso.cubixrealms.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Fired before a realm is permanently deleted. Cancelling aborts the deletion.
 */
public final class RealmDeleteEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID ownerUuid;
    private boolean cancelled;

    public RealmDeleteEvent(UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public UUID getOwnerUuid() { return ownerUuid; }

    @Override public boolean isCancelled()            { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers()         { return HANDLERS; }
    public static HandlerList getHandlerList()         { return HANDLERS; }
}
