package io.github.kawaiicakes.civilization.capabilities.data;

import io.github.kawaiicakes.civilization.api.data.CivSerializable;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class CivNation extends CivSerializable<CompoundTag> {
    public static final CivNation NULLARIA = new CivNation(
            Util.NIL_UUID,
            "Nullaria"
    );

    public CivNation(UUID id, String name) {
        super(id);
        this.name = name;
    }

    public CivNation(CompoundTag tag) {
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
    public void deserializeNBT(CompoundTag tag) {
        this.name = tag.getString(NAME_NBT_KEY);
    }
}
