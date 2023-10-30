package io.github.kawaiicakes.civilization.capabilities.data;

import io.github.kawaiicakes.civilization.api.data.DataMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import javax.annotation.Nullable;
import java.util.UUID;

public class CityMap extends DataMap<UUID, CivCity> {
    @Override
    public int tagType() {
        return Tag.TAG_COMPOUND;
    }

    /**
     * @return the <code>CivCity</code> with the passed <code>UUID</code>. <code>null</code> if no such city exists.
     */
    @Override
    public CivCity get(UUID id) {
        return this.map.get(id);
    }

    /**
     * @param id this is useless. For data integrity reasons, the <code>CivCity</code>'s ID is used
     *           in the call to <code>Map#put(K, V)</code>.
     */
    @Override
    public CivCity put(@Nullable UUID id, CivCity city) {
        return this.map.put(city.id(), city);
    }

    /**
     * When written to disk it is unnecessary to keep this data in the redundant format of the map.
     * Only during runtime when looking up a key does the map confer us an advantage
     */
    @Override
    public ListTag serializeNBT() {
        ListTag cityList = new ListTag();

        this.map.values().forEach(city -> cityList.add(city.serializeNBT()));

        return cityList;
    }

    @Override
    public void deserializeNBT(ListTag nbt) {
        nbt.forEach(city -> this.map.put(((CompoundTag) city).getUUID(ID_KEY), new CivCity((CompoundTag) city)));
    }
}
