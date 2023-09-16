package io.github.kawaiicakes.civilization.api.utils;

import net.minecraft.nbt.Tag;

/**
 * Pretty much identical to <code>INBTSerializable</code>, but <code>#deserialize</code>
 * now returns an instance of the implementing class.
 */
public interface NBTSerializable<T extends Tag, U> {
    T serializeNBT();
    U deserializeNBT(T nbt);
}
