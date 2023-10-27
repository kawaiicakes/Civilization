package io.github.kawaiicakes.civilization.capabilities;

import io.github.kawaiicakes.civilization.api.data.CivCapability;
import io.github.kawaiicakes.civilization.api.data.CivCapabilityProvider;
import io.github.kawaiicakes.civilization.api.nbt.CivSerializable;
import io.github.kawaiicakes.civilization.nbt.CivLevelNation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class CivLevelCapability implements CivCapability<CompoundTag> {
    CivLevelNation civLevelNations;

    @Override
    public void writeNBT(CompoundTag tag) {
        tag.put("civLevelNations", CivSerializable.collectionToListTag(this.civLevelNations));
    }

    @Override
    public void readNBT(CompoundTag tag) {
        ListTag nationNBTList = tag.getList("civLevelNations", Tag.TAG_COMPOUND);

        this.civLevelNations = CivSerializable.listTagToSet(nationNBTList, nat -> new CivLevelNation((CompoundTag) nat));
    }

    public static class Provider extends CivCapabilityProvider<CivLevelCapability, CompoundTag> {
        public static Capability<CivLevelCapability> CIV_LEVEL_CAP = CapabilityManager.get(new CapabilityToken<>() {});

        protected CivLevelCapability create() {
            if (this.capability == null) {
                // TODO: proper constructor
                this.capability =  new CivLevelCapability();
            }
            return this.capability;
        }

        @Override
        public Capability<CivLevelCapability> getCap() {
            return CIV_LEVEL_CAP;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            // TODO: code shit here.
            this.create().writeNBT(nbt);
            return nbt;
        }
    }
}
