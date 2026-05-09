package com.victoralonso.cubixrealms.api;

import com.victoralonso.cubixrealms.api.model.RealmInfo;
import com.victoralonso.cubixrealms.api.model.RealmMemberInfo;
import org.bukkit.World;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Public API facade for CubixRealms.
 *
 * Obtain the instance via {@link #get()} after the plugin has enabled.
 * The instance is null before CubixRealms enables and after it disables.
 *
 * <pre>{@code
 * CubixRealmsAPI api = CubixRealmsAPI.get();
 * if (api != null) {
 *     Optional<RealmInfo> realm = api.getRealmInfo(someUuid);
 * }
 * }</pre>
 */
public interface CubixRealmsAPI {

    /**
     * Returns public realm data for the given owner, or empty if the owner has no realm.
     */
    Optional<RealmInfo> getRealmInfo(UUID ownerUuid);

    /**
     * Returns the UUID of the realm owner for a given world,
     * or empty if the world is not a realm world.
     */
    Optional<UUID> getRealmOwner(World world);

    /**
     * Returns true if the given world belongs to any realm (overworld or nether).
     */
    boolean isRealmWorld(World world);

    /**
     * Returns true if the realm's overworld is currently loaded in memory.
     */
    boolean isLoaded(UUID ownerUuid);

    /**
     * Returns the role name for {@code playerUuid} inside the realm owned by {@code ownerUuid}.
     * Possible values: "OWNER", "VISITOR", or any custom role name (e.g. "TRUSTED", "RESIDENT").
     */
    String getRoleName(UUID playerUuid, UUID ownerUuid);

    /**
     * Returns true if {@code playerUuid} is banned from the realm owned by {@code ownerUuid}.
     */
    boolean isBanned(UUID playerUuid, UUID ownerUuid);

    /**
     * Returns all explicit (non-visitor) members of the realm owned by {@code ownerUuid}.
     */
    List<RealmMemberInfo> getMembers(UUID ownerUuid);

    // -------------------------------------------------------------------------
    // Singleton access
    // -------------------------------------------------------------------------

    static CubixRealmsAPI get() {
        return Holder.INSTANCE;
    }

    static void setInstance(CubixRealmsAPI api) {
        Holder.INSTANCE = api;
    }

    final class Holder {
        private Holder() {}
        static volatile CubixRealmsAPI INSTANCE;
    }
}
