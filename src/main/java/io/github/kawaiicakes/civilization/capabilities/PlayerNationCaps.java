package io.github.kawaiicakes.civilization.capabilities;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

/**
 * The actual data to store in the capability representing nation info, as well as methods surrounding management of it
 */
public class PlayerNationCaps {
    private UUID nation = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    public UUID getNation() {
        return this.nation;
    }

    public void setNation(UUID nation) {
        this.nation = nation;
    }

    public void copyFrom(PlayerNationCaps source) {
        this.nation = source.nation;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putUUID("nation", this.nation);
    }

    public void loadNBTData(CompoundTag nbt) {
        this.nation = nbt.getUUID("nation");
    }
}
