package com.victoralonso.cubixrealms.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Fired before a player is teleported into a realm via the /realm command.
 * Cancelling prevents the teleport from happening.
 */
public final class RealmTeleportEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final UUID targetOwnerUuid;
    private boolean cancelled;

    public RealmTeleportEvent(Player player, UUID targetOwnerUuid) {
        this.player          = player;
        this.targetOwnerUuid = targetOwnerUuid;
    }

    public Player getPlayer()          { return player; }
    public UUID getTargetOwnerUuid()   { return targetOwnerUuid; }

    @Override public boolean isCancelled()            { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers()         { return HANDLERS; }
    public static HandlerList getHandlerList()         { return HANDLERS; }
}
