package com.victoralonso.cubixrealms.api.event;

import com.victoralonso.cubixrealms.api.model.RealmDimension;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Fired when a player uses a portal within a realm dimension pair.
 * Cancelling prevents the portal travel. The vanilla portal event is already
 * cancelled by CubixRealms before this event fires.
 */
public final class RealmPortalTravelEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final UUID realmOwner;
    private final RealmDimension from;
    private final RealmDimension to;
    private boolean cancelled;

    public RealmPortalTravelEvent(Player player, UUID realmOwner,
                                   RealmDimension from, RealmDimension to) {
        this.player     = player;
        this.realmOwner = realmOwner;
        this.from       = from;
        this.to         = to;
    }

    public Player getPlayer()            { return player; }
    public UUID getRealmOwner()          { return realmOwner; }
    public RealmDimension getFrom()      { return from; }
    public RealmDimension getTo()        { return to; }

    @Override public boolean isCancelled()            { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers()         { return HANDLERS; }
    public static HandlerList getHandlerList()         { return HANDLERS; }
}
