package com.victoralonso.cubixrealms.api.event;

import com.victoralonso.cubixrealms.api.model.RealmDimension;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Fired after a player successfully enters a realm world (access checks already passed).
 * Informational only — not cancellable at this stage.
 */
public final class RealmEnterEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final UUID realmOwner;
    private final RealmDimension dimension;
    private final String roleName;

    public RealmEnterEvent(Player player, UUID realmOwner, RealmDimension dimension, String roleName) {
        this.player     = player;
        this.realmOwner = realmOwner;
        this.dimension  = dimension;
        this.roleName   = roleName;
    }

    public Player getPlayer()          { return player; }
    public UUID getRealmOwner()        { return realmOwner; }
    public RealmDimension getDimension() { return dimension; }
    /** Role of the entering player: "OWNER", "VISITOR", or a custom role name. */
    public String getRoleName()        { return roleName; }

    @Override public HandlerList getHandlers()          { return HANDLERS; }
    public static HandlerList getHandlerList()          { return HANDLERS; }
}
