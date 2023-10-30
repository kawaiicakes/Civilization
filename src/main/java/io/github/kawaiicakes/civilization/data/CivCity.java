package io.github.kawaiicakes.civilization.data;

import io.github.kawaiicakes.civilization.api.data.CivSerializable;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class CivCity extends CivSerializable<CompoundTag> {
    public CivCity(UUID id, String name) {
        super(id);
        this.name = name;
    }

    public CivCity(CompoundTag tag) {
        super(tag.getUUID(ID_NBT_KEY), tag);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag toReturn = new CompoundTag();
        toReturn.putUUID(ID_NBT_KEY, this.id());
        toReturn.putString(NAME_NBT_KEY, this.name);
        return toReturn;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.name = nbt.getString(NAME_NBT_KEY);
    }
}
