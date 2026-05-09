package com.victoralonso.cubixrealms.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Fired after a player has been unbanned from a realm.
 */
public final class RealmUnbanEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID realmOwner;
    private final UUID targetUuid;

    public RealmUnbanEvent(UUID realmOwner, UUID targetUuid) {
        this.realmOwner = realmOwner;
        this.targetUuid = targetUuid;
    }

    public UUID getRealmOwner() { return realmOwner; }
    public UUID getTargetUuid() { return targetUuid; }

    @Override public HandlerList getHandlers()   { return HANDLERS; }
    public static HandlerList getHandlerList()   { return HANDLERS; }
}
