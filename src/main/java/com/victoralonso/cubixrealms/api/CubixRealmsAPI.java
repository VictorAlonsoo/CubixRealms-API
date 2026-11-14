package com.victoralonso.cubixrealms.api;

import com.victoralonso.cubixrealms.api.model.RealmAction;
import com.victoralonso.cubixrealms.api.model.RealmDimension;
import com.victoralonso.cubixrealms.api.model.RealmInfo;
import com.victoralonso.cubixrealms.api.model.RealmMemberInfo;
import com.victoralonso.cubixrealms.api.model.VisitDenialReason;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

    // -------------------------------------------------------------------------
    // Identification (work without the world being loaded)
    // -------------------------------------------------------------------------

    /**
     * Returns public realm data for the given owner, or empty if the owner has no realm.
     */
    Optional<RealmInfo> getRealmInfo(UUID ownerUuid);

    /**
     * Returns public realm data for the given world name, or empty if the
     * world name does not belong to a realm. Works even when the world is
     * not currently loaded — lookup is by name, not by World instance.
     *
     * @since 1.1.0
     */
    Optional<RealmInfo> getRealmByWorldName(String worldName);

    /**
     * Returns the UUID of the realm owner for a given world,
     * or empty if the world is not a realm world.
     */
    Optional<UUID> getRealmOwner(World world);

    /**
     * Returns the UUID of the realm owner for a given world name,
     * or empty if the world name does not belong to any realm.
     * Works without the world being loaded.
     *
     * @since 1.1.0
     */
    Optional<UUID> getRealmOwner(String worldName);

    /**
     * Returns true if the given world belongs to any realm (overworld, nether or end).
     */
    boolean isRealmWorld(World world);

    /**
     * Returns true if the given world name matches the realm world naming pattern.
     * Does not require the world to be loaded.
     *
     * @since 1.1.0
     */
    boolean isRealmWorld(String worldName);

    /**
     * Returns true if the realm's overworld is currently loaded in memory.
     */
    boolean isLoaded(UUID ownerUuid);

    // -------------------------------------------------------------------------
    // Members and bans
    // -------------------------------------------------------------------------

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
    // Access check
    // -------------------------------------------------------------------------

    /**
     * Checks whether {@code visitor} is allowed to enter the realm owned by {@code ownerUuid}.
     *
     * <p>Applies the full access pipeline: bypass permission, ban list,
     * visitor rule (EVERYONE / MEMBERS_ONLY / FRIENDS_ONLY / PRIVATE),
     * and offline-host restriction.
     *
     * @return empty if the visitor is allowed; the denial reason otherwise.
     * @since 1.1.0
     */
    Optional<VisitDenialReason> checkVisit(UUID ownerUuid, Player visitor);

    /**
     * Returns the standard CubixRealms denial message for the given reason,
     * resolved against the active language file. The component already
     * includes the Realms prefix and the owner's display name where applicable,
     * so the caller only needs to forward it via {@code player.sendMessage(component)}.
     *
     * @since 1.1.0
     */
    Component getVisitDenialMessage(VisitDenialReason reason, UUID ownerUuid);

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    /**
     * Loads the realm's worlds if they are not already loaded.
     *
     * <p>Bukkit world creation runs on the main thread; this method schedules
     * the load there and returns a future that completes when the operation
     * finishes. The future resolves to {@code true} if the realm is loaded
     * after the call, {@code false} if the realm doesn't exist or load failed.
     *
     * @since 1.1.0
     */
    CompletableFuture<Boolean> loadRealmAsync(UUID ownerUuid);

    // -------------------------------------------------------------------------
    // Lookups (work without the world being loaded)
    // -------------------------------------------------------------------------

    /**
     * Returns true if the player owns a realm.
     *
     * @since 2.3.0
     */
    boolean hasRealm(UUID ownerUuid);

    /**
     * Returns every realm on the server, loaded or not. The collection is a snapshot.
     *
     * @since 2.3.0
     */
    Collection<RealmInfo> getRealms();

    /**
     * Returns which dimension of a realm this world is, or empty if it is not a realm world.
     *
     * @since 2.3.0
     */
    Optional<RealmDimension> getDimension(World world);

    /**
     * Returns the realm the player is standing in (any of its dimensions), or empty if
     * the player is outside every realm.
     *
     * @since 2.3.0
     */
    Optional<RealmInfo> getRealmAt(Player player);

    // -------------------------------------------------------------------------
    // Protection and teleport
    // -------------------------------------------------------------------------

    /**
     * Returns whether the player may perform the action where they stand, using the same
     * decision CubixRealms applies to its own protection: role flags of the realm, and the
     * bypass permissions. Always true outside realm worlds.
     *
     * <p>Use it so your plugin (shops, pets, custom blocks) respects realm roles.
     * Main thread only.
     *
     * @since 2.3.0
     */
    boolean isActionAllowed(Player player, RealmAction action);

    /**
     * Sends the player to the realm of {@code ownerUuid} through the standard flow: access
     * check, world load, safe spot, and the usual messages to the player when it is denied
     * or fails. Main thread only.
     *
     * @since 2.3.0
     */
    void teleportToRealm(Player player, UUID ownerUuid);

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