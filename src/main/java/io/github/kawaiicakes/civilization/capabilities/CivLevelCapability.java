package io.github.kawaiicakes.civilization.capabilities;

import io.github.kawaiicakes.civilization.api.data.CivCapability;
import io.github.kawaiicakes.civilization.api.data.CivCapabilityProvider;
import io.github.kawaiicakes.civilization.data.ChunkMap;
import io.github.kawaiicakes.civilization.data.CityMap;
import io.github.kawaiicakes.civilization.data.CivCity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * This capability is intended to store and interact with level-specific information such as who owns what chunks, and
 * any cities in the level.
 */
@ParametersAreNonnullByDefault
public class CivLevelCapability implements CivCapability<CompoundTag> {
    /**
     * A map wrapper whose keys correspond to a chunk. A key or value that is null may
     * be presumed to be unclaimed. In the case of the latter, the corresponding key
     * should be unmapped.
     */
    private final ChunkMap chunkMap = new ChunkMap();
    private final CityMap cityMap = new CityMap();

    public static final String CHUNK_MAP_KEY = "chunk_map";
    public static final String CITY_MAP_KEY = "cities";

    /**
     * Sets the owner for this chunk.
     * @param pos the <code>ChunkPos</code> to claim.
     * @param id the <code>UUID</code> of the owner.
     */
    public void setOwner(ChunkPos pos, UUID id) {
        this.chunkMap.put(pos, id);
    }

    /**
     * Returns the owner of the given <code>ChunkPos</code>.
     * @param pos the <code>ChunkPos</code>.
     * @return  returns the owner of this chunk as a <code>UUID</code>. If null, this chunk is unclaimed.
     */
    @Nullable
    public UUID owner(ChunkPos pos) {
        return this.chunkMap.get(pos);
    }

    /**
     * @return a <code>Set</code> of all cities in this level.
     */
    public Set<CivCity> getCities() {
        return new HashSet<>(this.cityMap.values());
    }

    public void addCity(CivCity city) {
        this.cityMap.put(null, city);
    }

    @Override
    public void writeNBT(CompoundTag tag) {
        tag.put(CHUNK_MAP_KEY, chunkMap.serializeNBT());
        tag.put(CITY_MAP_KEY, cityMap.serializeNBT());
    }

    @Override
    public void readNBT(CompoundTag tag) {
        chunkMap.deserializeNBT(tag.getList(CHUNK_MAP_KEY, chunkMap.tagType()));
        cityMap.deserializeNBT(tag.getList(CITY_MAP_KEY, cityMap.tagType()));
    }

    public static class Provider extends CivCapabilityProvider<CivLevelCapability, CompoundTag> {
        public static Capability<CivLevelCapability> CIV_LEVEL_CAP = CapabilityManager.get(new CapabilityToken<>() {});

        protected CivLevelCapability create() {
            if (this.capability == null) {
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
            this.create().writeNBT(nbt);
            return nbt;
        }
    }
}
