package io.github.kawaiicakes.civilization.api.nbt;

import net.minecraft.nbt.Tag;

/**
 * Pretty much identical to <code>INBTSerializable</code>, but without <code>#deserialize</code>.
 */
public interface NBTSerializable<T extends Tag> {
    T serializeNBT();
}
