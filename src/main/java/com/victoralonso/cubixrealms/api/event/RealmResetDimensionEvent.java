package com.victoralonso.cubixrealms.api.event;

import com.victoralonso.cubixrealms.api.model.RealmDimension;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Fired before a realm dimension is reset. Cancelling aborts the reset.
 */
public final class RealmResetDimensionEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID ownerUuid;
    private final RealmDimension dimension;
    private boolean cancelled;

    public RealmResetDimensionEvent(UUID ownerUuid, RealmDimension dimension) {
        this.ownerUuid = ownerUuid;
        this.dimension = dimension;
    }

    public UUID getOwnerUuid()           { return ownerUuid; }
    public RealmDimension getDimension() { return dimension; }

    @Override public boolean isCancelled()            { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers()         { return HANDLERS; }
    public static HandlerList getHandlerList()         { return HANDLERS; }
}
