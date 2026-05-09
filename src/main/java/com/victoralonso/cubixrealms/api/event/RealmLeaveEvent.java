package com.victoralonso.cubixrealms.api.event;

import com.victoralonso.cubixrealms.api.model.RealmDimension;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Fired when a player leaves a realm world for a non-realm world.
 * Informational only — not cancellable.
 */
public final class RealmLeaveEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final UUID realmOwner;
    private final RealmDimension dimension;

    public RealmLeaveEvent(Player player, UUID realmOwner, RealmDimension dimension) {
        this.player     = player;
        this.realmOwner = realmOwner;
        this.dimension  = dimension;
    }

    public Player getPlayer()            { return player; }
    public UUID getRealmOwner()          { return realmOwner; }
    /** Dimension the player was in before leaving. */
    public RealmDimension getDimension() { return dimension; }

    @Override public HandlerList getHandlers()   { return HANDLERS; }
    public static HandlerList getHandlerList()   { return HANDLERS; }
}
