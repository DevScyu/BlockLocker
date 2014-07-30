package nl.rutgerkok.chestsignprotect.impl.converter;

import java.util.Set;
import java.util.UUID;

import nl.rutgerkok.chestsignprotect.profile.PlayerProfile;
import nl.rutgerkok.chestsignprotect.profile.Profile;
import nl.rutgerkok.chestsignprotect.protection.Protection;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

final class ProtectionMissingIds {

    private final Set<String> namesMissingUniqueIds;
    private final Protection protection;

    /**
     * Builds the list of UUIDs to fetch. Must be called on the server thread.
     *
     * @param protection
     *            The protection to fetch the UUIDs for.
     */
    ProtectionMissingIds(Protection protection) {
        this.protection = protection;

        Builder<String> namesMissingUniqueIds = ImmutableSet.builder();

        for (Profile profile : protection.getAllowed()) {
            if (!(profile instanceof PlayerProfile)) {
                continue;
            }

            PlayerProfile playerProfile = (PlayerProfile) profile;
            Optional<UUID> uuid = playerProfile.getUniqueId();
            if (!uuid.isPresent()) {
                namesMissingUniqueIds.add(playerProfile.getDisplayName());
            }
        }

        this.namesMissingUniqueIds = namesMissingUniqueIds.build();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof ProtectionMissingIds)) {
            return false;
        }
        ProtectionMissingIds otherProtection = (ProtectionMissingIds) other;
        return otherProtection.protection.equals(protection);
    }

    /**
     * Gets the names that are missing unique ids. Can be called from any
     * thread.
     *
     * @return The names.
     */
    Set<String> getNamesMissingUniqueIds() {
        return namesMissingUniqueIds;
    }

    /**
     * Gets the protection that was missing unique ids. Can be called from any
     * thread, but keep in mind that methods on {@link Protection} aren't thread
     * safe.
     *
     * @return The protection.
     */
    Protection getProtection() {
        return protection;
    }

    @Override
    public int hashCode() {
        return protection.hashCode();
    }
}