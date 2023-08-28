package io.github.kawaiicakes.civilization.api.utils.json;

import io.github.kawaiicakes.civilization.api.utils.NamedUUID;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public class JsonIO {
    // TODO:
    public static NamedUUID getNamedUUID(UUID uuid) {
        return new NamedUUID(uuid, Component.empty());
    }
}
