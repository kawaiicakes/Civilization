package io.github.kawaiicakes.civilization.capabilities;

import io.github.kawaiicakes.civilization.api.data.CivCapability;
import io.github.kawaiicakes.civilization.api.data.CivCapabilityProvider;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * This capability is intended to store chunk-specific information such as the owner of the chunk.
 */
public class CivLevelChunkCapability implements CivCapability<CompoundTag> {
    /**
     * The owner of this chunk. The nil UUID indicates it is not claimed.
     */
    @NotNull
    private UUID owner = Util.NIL_UUID;

    /**
     * Claims this chunk for the given owner. Pass null as the argument to unclaim the chunk.
     * @param id the <code>UUID</code> of the owner.
     */
    public void claim(@NotNull UUID id) {
        this.owner = id;
    }

    @NotNull
    public UUID owner() {
        return this.owner;
    }

    @Override
    public void writeNBT(CompoundTag tag) {
        tag.putUUID("owner", this.owner);
    }

    @Override
    public void readNBT(CompoundTag tag) {
        this.owner = tag.getUUID("owner");
    }

    public static class Provider extends CivCapabilityProvider<CivLevelChunkCapability, CompoundTag> {
        public static Capability<CivLevelChunkCapability> CIV_LEVEL_CHUNK_CAP = CapabilityManager.get(new CapabilityToken<>() {});

        protected CivLevelChunkCapability create() {
            if (this.capability == null) {
                this.capability =  new CivLevelChunkCapability();
            }
            return this.capability;
        }

        @Override
        public Capability<CivLevelChunkCapability> getCap() {
            return CIV_LEVEL_CHUNK_CAP;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            this.create().writeNBT(nbt);
            return nbt;
        }
    }
}
