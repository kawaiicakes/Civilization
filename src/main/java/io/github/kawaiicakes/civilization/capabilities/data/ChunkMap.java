package io.github.kawaiicakes.civilization.capabilities.data;

import io.github.kawaiicakes.civilization.api.data.DataMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.ChunkPos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChunkMap extends DataMap<ChunkPos, UUID> {
    public static final String OWNER_KEY = "owner";
    public static final String POS_KEY = "chunks";

    @Override
    public int tagType() {
        return Tag.TAG_COMPOUND;
    }

    /**
     * @return the <code>UUID</code> of the owner of the passed <code>ChunkPos</code>. <code>null</code> if no owner exists.
     */
    @Override
    public UUID get(ChunkPos pos) {
        return this.map.get(pos);
    }

    @Override
    public UUID put(ChunkPos pos, UUID owner) {
        return this.map.put(pos, owner);
    }

    @Override
    public ListTag serializeNBT() {
        ListTag chunkList = new ListTag();
        Map<UUID, LongArrayList> invertedMap = new HashMap<>();

        this.map.forEach((pos, id) -> invertedMap.computeIfAbsent(id, k -> new LongArrayList()).add(pos.toLong()));

        invertedMap.forEach((id, posArray) -> {
            CompoundTag mapping = new CompoundTag();
            LongArrayTag chunkArray = new LongArrayTag(posArray);

            mapping.putUUID(OWNER_KEY, id);
            mapping.put(POS_KEY, chunkArray);

            chunkList.add(mapping);
        });

        return chunkList;
    }

    @Override
    public void deserializeNBT(ListTag nbt) {
        nbt.forEach(compoundTag -> {
            CompoundTag cumPoundTag = (CompoundTag) compoundTag;

            for (long chunkPos : cumPoundTag.getLongArray(POS_KEY)) {
                this.map.put(new ChunkPos(chunkPos), cumPoundTag.getUUID(OWNER_KEY));
            }
        });
    }
}
