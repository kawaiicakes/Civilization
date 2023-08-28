package io.github.kawaiicakes.civilization.nation.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerNationCapsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerNationCaps> PLAYER_NATION = CapabilityManager.get(new CapabilityToken<>() {});

    private PlayerNationCaps nation = null;
    private final LazyOptional<PlayerNationCaps> lazyHandler = LazyOptional.of(this::createPlayerNationCaps);

    private PlayerNationCaps createPlayerNationCaps() {
        if (this.nation == null) {
            this.nation = new PlayerNationCaps();
        }

        return this.nation;
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
