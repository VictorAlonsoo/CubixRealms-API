package com.victoralonso.cubixrealms.api.model;

/**
 * Reasons CubixRealms denies a player entry into a realm.
 * Returned by {@link com.victoralonso.cubixrealms.api.CubixRealmsAPI#checkVisit}.
 *
 * <ul>
 *   <li>{@link #REALM_NOT_FOUND} — the owner has no realm</li>
 *   <li>{@link #BANNED} — visitor is banned from this realm</li>
 *   <li>{@link #MEMBERS_ONLY} — realm is restricted to explicit members</li>
 *   <li>{@link #FRIENDS_ONLY} — realm is restricted to members or friends</li>
 *   <li>{@link #PRIVATE} — realm is closed to all but the owner</li>
 *   <li>{@link #OWNER_OFFLINE} — owner is offline and offline-host is disabled</li>
 * </ul>
 */
public enum VisitDenialReason {
    REALM_NOT_FOUND,
    BANNED,
    MEMBERS_ONLY,
    FRIENDS_ONLY,
    PRIVATE,
    OWNER_OFFLINE
}