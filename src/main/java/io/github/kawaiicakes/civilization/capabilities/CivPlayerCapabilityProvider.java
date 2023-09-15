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

public class CivPlayerCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
    public static Capability<CivPlayerCapability> PLAYER_NATION = CapabilityManager.get(new CapabilityToken<>(){});

    private CivPlayerCapability civInfo = null;

    private final LazyOptional<CivPlayerCapability> lazyHandler = LazyOptional.of(this::createPlayerNationCaps);

    private CivPlayerCapability createPlayerNationCaps() {
        if (this.civInfo == null) {
            this.civInfo = new CivPlayerCapability();
        }

        return this.civInfo;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_NATION) {
            return lazyHandler.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerNationCaps().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerNationCaps().loadNBTData(nbt);
    }
}
