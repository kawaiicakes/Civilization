package io.github.kawaiicakes.civilization.api.utils;

import io.github.kawaiicakes.civilization.api.level.HexTilePos;
import io.github.kawaiicakes.civilization.api.nations.LevelNation;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Utility class for writing classes to and from NBT data. Especially useful for easy capability shit
 * and networking. Methods are self-explanatory.
 */
public class CivNBT {
    public static CompoundTag fromNation(LevelNation levelNation) {
        CompoundTag nationNBT = new CompoundTag();

        nationNBT.putUUID("uuid", levelNation.nationUUID());
        nationNBT.putString("name", levelNation.nationName());
        nationNBT.put("players", uuidListToTag(levelNation.players()));
        nationNBT.put("cities", uuidListToTag(levelNation.cities()));
        nationNBT.put("tiles", hexTileListTag(levelNation.territory()));
        nationNBT.putInt("diplomacy", levelNation.diplomacy());

        return nationNBT;
    }

    public static LevelNation fromNBT(CompoundTag nationNBT) {
        return new LevelNation(
                nationNBT.getUUID("uuid"),
                nationNBT.getString("name"),
                uuidTagToList(nationNBT.getList("players", Tag.TAG_LIST)),
                uuidTagToList(nationNBT.getList("cities", Tag.TAG_LIST)),
                hexTagToList(nationNBT.getList("tiles", Tag.TAG_LIST)),
                nationNBT.getInt("diplomacy")
        );
    }

    /*
     * Helper methods below
     */

    private static ListTag uuidListToTag(Set<UUID> list) {
        ListTag listTag = new ListTag();
        list.forEach(uuid -> listTag.add(new IntArrayTag(UUIDUtil.uuidToIntArray(uuid))));
        return listTag;
    }

    private static ListTag hexTileListTag(Set<HexTilePos> list) {
        ListTag listTag = new ListTag();
        list.forEach(hexTilePos -> listTag.add(new IntArrayTag(hexTilePos.asIntArray())));
        return listTag;
    }

    private static Set<UUID> uuidTagToList(ListTag nationNBT) {
        Set<UUID> returnList = new HashSet<>(nationNBT.size());

        nationNBT.stream()
                .map(arr -> UUIDUtil.uuidFromIntArray(((IntArrayTag) arr).getAsIntArray()))
                .forEach(returnList::add);

        return returnList;
    }

    private static Set<HexTilePos> hexTagToList(ListTag nationNBT) {
        Set<HexTilePos> returnList = new HashSet<>(nationNBT.size());

        nationNBT.stream()
                .map(arr -> HexTilePos.fromIntArray(((IntArrayTag) arr).getAsIntArray()))
                .forEach(returnList::add);

        return returnList;
    }
}
