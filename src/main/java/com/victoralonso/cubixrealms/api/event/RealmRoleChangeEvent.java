package com.victoralonso.cubixrealms.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Fired before a player's role in a realm is changed or removed.
 * Cancelling prevents the role change from being persisted.
 *
 * <p>{@code oldRole} is "VISITOR" when the player had no stored role before.
 * {@code newRole} is "VISITOR" when the role is being removed (demoted to visitor).
 */
public final class RealmRoleChangeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID realmOwner;
    private final UUID memberUuid;
    private final String oldRole;
    private final String newRole;
    private boolean cancelled;

    public RealmRoleChangeEvent(UUID realmOwner, UUID memberUuid,
                                 String oldRole, String newRole) {
        this.realmOwner = realmOwner;
        this.memberUuid = memberUuid;
        this.oldRole    = oldRole;
        this.newRole    = newRole;
    }

    public UUID getRealmOwner() { return realmOwner; }
    public UUID getMemberUuid() { return memberUuid; }
    /** Role before the change. "VISITOR" if the player had no stored role. */
    public String getOldRole()  { return oldRole; }
    /** Role after the change. "VISITOR" if the role is being removed. */
    public String getNewRole()  { return newRole; }

    @Override public boolean isCancelled()            { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers()         { return HANDLERS; }
    public static HandlerList getHandlerList()         { return HANDLERS; }
}
