package io.github.kawaiicakes.civilization.capabilities;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

/**
 * The actual data to store in the capability representing nation info, as well as methods surrounding management of it
 */
public class CivPlayerCapability {
    private UUID nation;


    public UUID getNation() {
        return this.nation;
    }

    public void setNation(UUID nation) {
        this.nation = nation;
    }

    public void copyFrom(CivPlayerCapability source) {
        this.nation = source.nation;
    }

    public void saveNBTData(CompoundTag nbt) {
        if (this.nation == null) {
            // NationManager#createNation or somethin?
        }
        nbt.putUUID("nation", this.nation);
    }

    public void loadNBTData(CompoundTag nbt) {
        this.nation = nbt.getUUID("nation");
    }
}
