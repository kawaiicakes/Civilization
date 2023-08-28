package io.github.kawaiicakes.civilization.api.utils;

import io.github.kawaiicakes.civilization.nation.NationManager;
import net.minecraft.network.chat.Component;

import java.util.UUID;

/**
 * Convenience record used for identifying the name of a city, nation, etc.
 * @param nameUUID  the <code>UUID</code> corresponding to the name.
 * @param nameComponent the <code>Component</code> corresponding to the name.
 */
public record NamedUUID(UUID nameUUID, Component nameComponent) {
    public String fromComponent() {
        return this.nameComponent.getString();
    }

    public static NamedUUID fromUUID(UUID uuid) {
        return NationManager.fromUUID(uuid);
    }
}
