package io.github.kawaiicakes.civilization.api.utils;

import io.github.kawaiicakes.civilization.api.level.HexTilePos;
import io.github.kawaiicakes.civilization.api.nations.Nation;
import net.minecraft.core.NonNullList;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.UUID;

/**
 * Utility class for writing classes to and from NBT data. Especially useful for easy capability shit
 * and networking. Methods are self-explanatory.
 */
public class CivNBT {
    public static CompoundTag fromNation(Nation nation) {
        CompoundTag nationNBT = new CompoundTag();

        nationNBT.putUUID("uuid", nation.nationUUID());
        nationNBT.putString("name", nation.nationName());
        nationNBT.put("players", uuidListToTag(nation.players()));
        nationNBT.put("cities", uuidListToTag(nation.cities()));
        nationNBT.put("tiles", hexTileListTag(nation.territory()));
        nationNBT.putInt("diplomacy", nation.diplomacy());

        return nationNBT;
    }

    public static Nation fromNBT(CompoundTag nationNBT) {
        return new Nation(
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

    private static ListTag uuidListToTag(NonNullList<UUID> list) {
        ListTag listTag = new ListTag();
        list.forEach(uuid -> listTag.add(new IntArrayTag(UUIDUtil.uuidToIntArray(uuid))));
        return listTag;
    }

    private static ListTag hexTileListTag(NonNullList<HexTilePos> list) {
        ListTag listTag = new ListTag();
        list.forEach(hexTilePos -> listTag.add(new IntArrayTag(hexTilePos.asIntArray())));
        return listTag;
    }

    private static NonNullList<UUID> uuidTagToList(ListTag nationNBT) {
        NonNullList<UUID> returnList = NonNullList.createWithCapacity(nationNBT.size());

        nationNBT.stream()
                .map(arr -> UUIDUtil.uuidFromIntArray(((IntArrayTag) arr).getAsIntArray()))
                .forEach(returnList::add);

        return returnList;
    }

    private static NonNullList<HexTilePos> hexTagToList(ListTag nationNBT) {
        NonNullList<HexTilePos> returnList = NonNullList.createWithCapacity(nationNBT.size());

        nationNBT.stream()
                .map(arr -> HexTilePos.fromIntArray(((IntArrayTag) arr).getAsIntArray()))
                .forEach(returnList::add);

        return returnList;
    }
}
