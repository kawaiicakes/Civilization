package io.github.kawaiicakes.civilization.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CivLevelCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
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
