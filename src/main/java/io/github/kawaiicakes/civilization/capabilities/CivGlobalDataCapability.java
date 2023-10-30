package io.github.kawaiicakes.civilization.capabilities;

import io.github.kawaiicakes.civilization.api.data.CivCapability;
import io.github.kawaiicakes.civilization.api.data.CivCapabilityProvider;
import io.github.kawaiicakes.civilization.capabilities.data.CivNation;
import io.github.kawaiicakes.civilization.capabilities.data.NationMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * This capability is intended to store and interact, on all <code>ServerLevel</code>s, non-dimension-specific
 * information such as who is in what nation, nation flags/names, etc.
 */
public class CivGlobalDataCapability implements CivCapability<CompoundTag> {
    public static final String NATION_KEY = "nations";

    private static final NationMap NATION_MAP = new NationMap();

    public static Set<CivNation> getNations() {
        return new HashSet<>(NATION_MAP.values());
    }

    @Nullable
    public static CivNation getNationById(UUID id) {
        return NATION_MAP.get(id);
    }

    public static void addNation(CivNation nation) {
        NATION_MAP.put(null, nation);
    }

    @Override
    public void writeNBT(CompoundTag tag) {
        tag.put(NATION_KEY, NATION_MAP.serializeNBT());
    }

    @Override
    public void readNBT(CompoundTag tag) {
        NATION_MAP.deserializeNBT(tag.getList(NATION_KEY, NATION_MAP.tagType()));
    }

    public static class Provider extends CivCapabilityProvider<CivGlobalDataCapability, CompoundTag> {
        public static Capability<CivGlobalDataCapability> CIV_GLOBAL_CAP = CapabilityManager.get(new CapabilityToken<>() {});

        protected CivGlobalDataCapability create() {
            if (this.capability == null) {
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
            this.create().writeNBT(nbt);
            return nbt;
        }
    }
}
