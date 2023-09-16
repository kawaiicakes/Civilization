package io.github.kawaiicakes.civilization.api.utils;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

/**
 * Implementing classes agree to provide a method that allows the fields of the instance
 * to be serialized into NBT.
 */
public interface CompoundTagTransformable {
    @NotNull CompoundTag getAsCompoundTag();
}
