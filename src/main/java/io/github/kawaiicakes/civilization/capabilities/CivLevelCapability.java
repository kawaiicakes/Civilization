package io.github.kawaiicakes.civilization.capabilities;

import io.github.kawaiicakes.civilization.api.data.CivCapability;
import io.github.kawaiicakes.civilization.api.data.CivCapabilityProvider;
import io.github.kawaiicakes.civilization.capabilities.data.CivCity;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

/**
 * This capability is intended to store level-specific information such as who owns what chunks, and any cities in the
 * level.
 */
@ParametersAreNonnullByDefault
public class CivLevelCapability implements CivCapability<CompoundTag> {
    /**
     * A map whose keys correspond to a chunk. A key or value that is null may
     * be presumed to be unclaimed. In the case of the latter, the corresponding key
     * should be unmapped.
     */
    private final Map<ChunkPos, UUID> chunkMap = new HashMap<>();
    private final Map<UUID, CivCity> cityMap = new HashMap<>();

    public static final String CHUNK_MAP_KEY = "chunk_map";
    public static final String OWNER_KEY = "owner";
    public static final String POS_KEY = "chunks";

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

    public Set<CivCity> getCities() {
        return new HashSet<>(this.cityMap.values());
    }

    public void addCity(CivCity city) {
        this.cityMap.put(city.id(), city);
    }

    @Override
    public void writeNBT(CompoundTag tag) {
        saveChunkMap(tag);
        saveCities(tag);
    }

    @Override
    public void readNBT(CompoundTag tag) {
        loadChunkMap(tag);
        loadCities(tag);
    }

    private void saveChunkMap(CompoundTag tag) {
        ListTag chunkList = new ListTag();
        Map<UUID, LongArrayList> invertedMap = new HashMap<>();

        this.chunkMap.forEach((pos, id) -> invertedMap.computeIfAbsent(id, k -> new LongArrayList()).add(pos.toLong()));

        invertedMap.forEach((id, posArray) -> {
            CompoundTag mapping = new CompoundTag();
            LongArrayTag chunkArray = new LongArrayTag(posArray);

            mapping.putUUID(OWNER_KEY, id);
            mapping.put(POS_KEY, chunkArray);

            chunkList.add(mapping);
        });

        tag.put(CHUNK_MAP_KEY, chunkList);
    }

    private void loadChunkMap(CompoundTag tag) {
        ListTag chunkList = (ListTag) tag.get(POS_KEY);

        if (chunkList == null) return;

        chunkList.forEach(compoundTag -> {
            CompoundTag nbt = (CompoundTag) compoundTag;

            for (long chunkPos : nbt.getLongArray(POS_KEY)) {
                this.chunkMap.put(new ChunkPos(chunkPos), nbt.getUUID(OWNER_KEY));
            }
        });
    }

    private void saveCities(CompoundTag tag) {
        ListTag cityList = new ListTag();

        this.cityMap.values().forEach(city -> cityList.add(city.serializeNBT()));

        tag.put(CITY_MAP_KEY, cityList);
    }

    private void loadCities(CompoundTag tag) {
        ListTag cityList = (ListTag) tag.get(CITY_MAP_KEY);

        if (cityList == null) return;

        cityList.forEach(city -> {
            CivCity dsCity = new CivCity((CompoundTag) city);
            this.cityMap.put(dsCity.id(), dsCity);
        });
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
