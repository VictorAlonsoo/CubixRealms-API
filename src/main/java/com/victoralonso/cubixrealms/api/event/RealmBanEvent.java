package com.victoralonso.cubixrealms.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Fired before a player is banned from a realm. Cancelling prevents the ban.
 */
public final class RealmBanEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID realmOwner;
    private final UUID targetUuid;
    private final UUID actorUuid;
    private boolean cancelled;

    public RealmBanEvent(UUID realmOwner, UUID targetUuid, UUID actorUuid) {
        this.realmOwner = realmOwner;
        this.targetUuid = targetUuid;
        this.actorUuid  = actorUuid;
    }

    public UUID getRealmOwner() { return realmOwner; }
    public UUID getTargetUuid() { return targetUuid; }
    public UUID getActorUuid()  { return actorUuid; }

    @Override public boolean isCancelled()            { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers()         { return HANDLERS; }
    public static HandlerList getHandlerList()         { return HANDLERS; }
}
