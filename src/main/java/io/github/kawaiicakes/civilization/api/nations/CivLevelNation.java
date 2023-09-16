package io.github.kawaiicakes.civilization.api.nations;

import io.github.kawaiicakes.civilization.api.level.HexTilePos;
import io.github.kawaiicakes.civilization.api.utils.NBTSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * This record class holds all information needed to define a nation. It can be passed around for data transfer.
 * Long-term storage is not recommended as many actions in-game may cause the characteristics of a nation
 * to evolve with time. It is exclusively meant to shuttle nation info around.
 * <br><br>
 * Originally written to pass data back and forth with NBT. The choice to make it a record was made because
 * as a data 'shipping' class, the contents should be immutable such as to discourage changing it as it's passed
 * around.
 * <br><br>
 * Despite the nature of UUIDs, sets are used to ensure the same city/player is added to the list again
 * on account of another part of the code.
 */
public record CivLevelNation(
        @NotNull UUID nationUUID,
        @NotNull String nationName,
        @NotNull Map<UUID, CivPermissions> players,
        @NotNull Set<CivLevelCity> cities,
        @NotNull Set<HexTilePos> tiles,
        int diplomacy
) implements NBTSerializable<CompoundTag, CivLevelNation> {
    @Override
    public @NotNull CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();

        compoundTag.putUUID("nation_id", this.nationUUID);
        compoundTag.putString("nation_name", this.nationName);
        compoundTag.putInt("players", 0); // TODO
        compoundTag.put("cities", citiesToListTag());
        compoundTag.put("tiles", tilesToListTag());
        compoundTag.putInt("diplomacy", 0);

        return compoundTag;
    }

    @Override
    public CivLevelNation deserializeNBT(CompoundTag nbt) {
        return null; // TODO
    }

    public ListTag tilesToListTag() {
        ListTag listTag = new ListTag();

        this.tiles.forEach(hex -> listTag.add(new IntArrayTag(hex.asIntArray())));

        return listTag;
    }

    public ListTag citiesToListTag() {
        ListTag listTag = new ListTag();

        this.cities.forEach(city -> {
            listTag.add(city.serializeNBT());
        });

        return listTag;
    }
}
