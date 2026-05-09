package com.victoralonso.cubixrealms.api.model;

import java.util.UUID;

/**
 * Immutable snapshot of a realm's public state.
 *
 * @param ownerUuid       UUID of the realm owner
 * @param ownerName       last known display name of the owner
 * @param worldName       overworld world name on the server
 * @param netherWorldName nether world name on the server
 * @param visitorRule     name of the visitor access rule (EVERYONE, MEMBERS_ONLY, FRIENDS_ONLY, PRIVATE)
 * @param loaded          whether the realm worlds are currently loaded in memory
 */
public record RealmInfo(
        UUID ownerUuid,
        String ownerName,
        String worldName,
        String netherWorldName,
        String visitorRule,
        boolean loaded
) {}
