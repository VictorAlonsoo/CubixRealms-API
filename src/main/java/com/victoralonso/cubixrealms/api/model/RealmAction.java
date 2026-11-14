package com.victoralonso.cubixrealms.api.model;

/**
 * What a player can try to do inside a realm. Each constant maps to one role flag of
 * {@code roles.yml}.
 *
 * @since 2.3.0
 */
public enum RealmAction {
    /** Break or place blocks, buckets, hanging entities, vehicles. */
    BUILD,
    /** Right-click blocks and entities that are neither containers, doors nor redstone triggers. */
    INTERACT,
    /** Open chests, workstations and entities that hold items. */
    CONTAINERS,
    /** Open doors, trapdoors and fence gates. */
    DOORS,
    /** Buttons, levers, pressure plates and tripwire. */
    REDSTONE,
    /** Damage other players. */
    PVP,
    /** Damage mobs. */
    DAMAGE_MOBS,
    /** Drop items, and move items in item frames and armor stands. */
    ITEM_DROP
}
