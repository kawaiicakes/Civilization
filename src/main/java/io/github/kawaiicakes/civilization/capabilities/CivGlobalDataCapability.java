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
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * This capability is intended to store, on all <code>ServerLevel</code>s, non-dimension-specific information such as
 * who is in what nation, nation flags/names, etc.
 */
public class CivGlobalDataCapability implements CivCapability<CompoundTag> {
    // TODO: look into Int2ObjectArrayMaps
    private static final Map<UUID, CivNation> NATION_MAP = new HashMap<>();

    public static Set<CivNation> getNations() {
        return new HashSet<>(NATION_MAP.values());
    }

    @Nullable
    public static CivNation getNationById(UUID id) {
        return NATION_MAP.get(id);
    }

    public static void addNation(CivNation nation) {
        NATION_MAP.put(nation.id(), nation);
    }

    @Override
    public void writeNBT(CompoundTag tag) {
        ListTag nationNBTList = new ListTag();

        NATION_MAP.values().forEach(nation -> nationNBTList.add(nation.serializeNBT()));

        tag.put("nations", nationNBTList);
    }

    @Override
    public void readNBT(CompoundTag tag) {
        ListTag nationNBTList = tag.getList("nations", Tag.TAG_COMPOUND);

        nationNBTList.forEach(nbt ->
                NATION_MAP.put(((CompoundTag) nbt).getUUID("id"), new CivNation((CompoundTag) nbt))
        );
    }

    public static class Provider extends CivCapabilityProvider<CivGlobalDataCapability, CompoundTag> {
        public static Capability<CivGlobalDataCapability> CIV_GLOBAL_CAP = CapabilityManager.get(new CapabilityToken<>() {});

        protected CivGlobalDataCapability create() {
            if (this.capability == null) {
                // TODO: proper constructor
                this.capability =  new CivGlobalDataCapability();
            }
            return this.capability;
        }

        @Override
        public Capability<CivGlobalDataCapability> getCap() {
            return CIV_GLOBAL_CAP;
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
