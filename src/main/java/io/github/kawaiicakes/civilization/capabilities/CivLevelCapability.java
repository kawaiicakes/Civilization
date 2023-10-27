package io.github.kawaiicakes.civilization.capabilities;

import io.github.kawaiicakes.civilization.api.nations.CivLevelData;
import io.github.kawaiicakes.civilization.api.nations.CivLevelNation;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class CivLevelCapability {
    @NotNull
    private Set<CivLevelNation> civLevelNations = new HashSet<>();

    public @NotNull Set<CivLevelNation> getNations() {
        return this.civLevelNations;
    }

    public void setNations(@NotNull Set<CivLevelNation> civLevelNations) {
        this.civLevelNations = civLevelNations;
    }

    public void addNation(CivLevelNation civLevelNation) {
        this.civLevelNations.add(civLevelNation);
    }

    public void saveNBT(CompoundTag nbt) {
        nbt.put("civLevelNations", CivLevelData.collectionToListTag(this.civLevelNations));
    }

    public void loadNBT(CompoundTag nbt) {
        ListTag nationNBTList = nbt.getList("civLevelNations", Tag.TAG_COMPOUND);

        this.civLevelNations = CivLevelData.listTagToSet(nationNBTList, nat -> new CivLevelNation((CompoundTag) nat));
    }

    public static class Provider implements ICapabilitySerializable<CompoundTag> {
        public static Capability<CivLevelCapability> CIV_LEVEL_CAP = CapabilityManager.get(new CapabilityToken<>() {});

        private CivLevelCapability civLevelCapability = null;

        private final LazyOptional<CivLevelCapability> lazyHandler = LazyOptional.of(this::createCapability);

        private CivLevelCapability createCapability() {
            if (this.civLevelCapability == null) {
                // TODO: proper constructor
                this.civLevelCapability =  new CivLevelCapability();
            }
            return this.civLevelCapability;
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if (cap == CIV_LEVEL_CAP) {
                return lazyHandler.cast();
            }

            return LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            // TODO: code shit here.
            this.createCapability().saveNBT(nbt);
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            // TODO
            this.createCapability().loadNBT(nbt);
        }
    }
}
