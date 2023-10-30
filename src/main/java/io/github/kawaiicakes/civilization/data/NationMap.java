package io.github.kawaiicakes.civilization.data;

import io.github.kawaiicakes.civilization.api.data.DataMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class NationMap extends DataMap<UUID, CivNation> {
    @Override
    public int tagType() {
        return Tag.TAG_COMPOUND;
    }

    @Override
    public CivNation get(UUID id) {
        return this.map.get(id);
    }

    /**
     * @param id this is useless. For data integrity reasons, the <code>CivNation</code>'s ID is used
     *           in the call to <code>Map#put(K, V)</code>.
     */
    @Override
    public CivNation put(@Nullable UUID id, CivNation nation) {
        return this.map.put(nation.id(), nation);
    }

    @Override
    public ListTag serializeNBT() {
        ListTag nationNBTList = new ListTag();
        this.map.values().forEach(nation -> nationNBTList.add(nation.serializeNBT()));
        return nationNBTList;
    }

    @Override
    public void deserializeNBT(ListTag nbt) {
        nbt.forEach(tag ->
                this.map.put(((CompoundTag) tag).getUUID(ID_KEY), new CivNation((CompoundTag) tag))
        );
    }
}
