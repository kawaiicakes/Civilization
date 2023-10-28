package io.github.kawaiicakes.civilization.capabilities;

import io.github.kawaiicakes.civilization.api.data.CivCapability;
import io.github.kawaiicakes.civilization.api.data.CivCapabilityProvider;
import io.github.kawaiicakes.civilization.capabilities.data.CivNation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import java.util.*;

/**
 * This capability is intended to store, on all <code>ServerLevel</code>s, non-dimension-specific information such as
 * who is in what nation, nation flags/names, etc.
 */
public class CivGlobalDataCapability implements CivCapability<CompoundTag> {
    // TODO: look into Int2ObjectArrayMaps
    protected final Map<UUID, CivNation> nationMap = new HashMap<>();

    public Set<CivNation> getNations() {
        return new HashSet<>(nationMap.values());
    }

    public void addNation(CivNation nation) {
        this.nationMap.put(nation.id(), nation);
    }

    @Override
    public void writeNBT(CompoundTag tag) {
        ListTag nationNBTList = new ListTag();

        this.nationMap.values().forEach(nation -> nationNBTList.add(nation.serializeNBT()));

        tag.put("nations", nationNBTList);
    }

    @Override
    public void readNBT(CompoundTag tag) {
        ListTag nationNBTList = tag.getList("nations", Tag.TAG_COMPOUND);

        nationNBTList.forEach(nbt ->
                this.nationMap.put(((CompoundTag) nbt).getUUID("id"), CivNation.deserializeNBT((CompoundTag) nbt))
        );
    }

    public static class Provider extends CivCapabilityProvider<CivGlobalDataCapability, CompoundTag> {
        public static Capability<CivGlobalDataCapability> CIV_LEVEL_CAP = CapabilityManager.get(new CapabilityToken<>() {});

        protected CivGlobalDataCapability create() {
            if (this.capability == null) {
                // TODO: proper constructor
                this.capability =  new CivGlobalDataCapability();
            }
            return this.capability;
        }

        @Override
        public Capability<CivGlobalDataCapability> getCap() {
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
