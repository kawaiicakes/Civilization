package io.github.kawaiicakes.civilization.capabilities;

import io.github.kawaiicakes.civilization.api.level.HexTilePos;
import io.github.kawaiicakes.civilization.api.nations.Nation;
import net.minecraft.core.NonNullList;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CivLevelCapability {
    // TODO: consider replacing list with collection. Order isn't vital; at least it's not thought to be rn.
    @NotNull
    private List<Nation> nations = new ArrayList<>();

    public @NotNull List<Nation> getNations() {
        return this.nations;
    }

    public void setNations(@NotNull List<Nation> nations) {
        this.nations = nations;
    }

    public void addNation(Nation nation) {
        this.nations.add(nation);
    }

    public void saveNBT(CompoundTag nbt) {
        this.saveNationsNBT(nbt);
    }

    public void loadNBT(CompoundTag nbt) {
        if (nbt != null && nbt.get("nations") != null) {
            if (nbt.get("nations") instanceof ListTag nationNBTList) {

                List<Nation> nationList = new ArrayList<>(nationNBTList.size());

                nationNBTList.forEach(n -> {
                    CompoundTag nationNBT = (CompoundTag) n;

                    nationList.add(new Nation(
                            nationNBT.getUUID("uuid"),
                            nationNBT.getString("name"),
                            uuidTagToList(nationNBT.getList("players", Tag.TAG_LIST)),
                            uuidTagToList(nationNBT.getList("cities", Tag.TAG_LIST)),
                            hexTagToList(nationNBT.getList("tiles", Tag.TAG_LIST)),
                            nationNBT.getInt("diplomacy")
                    ));
                });

                this.setNations(nationList);
            }
        }
    }

    /*
        HELPER METHODS BELOW
     */
    private void saveNationsNBT(CompoundTag nbt) {
        ListTag nationsNBTList = new ListTag();

        this.nations.forEach(nation -> {
            if (nation != null) {
                CompoundTag nationNBT = new CompoundTag();

                nationNBT.putUUID("uuid", nation.nationUUID());
                nationNBT.putString("name", nation.nationName());
                nationNBT.put("players", uuidListToTag(nation.players()));
                nationNBT.put("cities", uuidListToTag(nation.cities()));
                nationNBT.put("tiles", hexTileListTag(nation.territory()));
                nationNBT.putInt("diplomacy", nation.diplomacy());

                nationsNBTList.add(nationNBT);
            }
        });

        nbt.put("nations", nationsNBTList);
    }

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
