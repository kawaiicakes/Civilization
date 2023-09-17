package io.github.kawaiicakes.civilization.api.nations;

import io.github.kawaiicakes.civilization.api.level.HexTilePos;
import io.github.kawaiicakes.civilization.api.utils.CivNBT;
import io.github.kawaiicakes.civilization.api.utils.NBTSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * This record class holds all information needed to define a nation. It can be passed around for data transfer.
 * Long-term storage is not recommended as many actions in-game may cause the characteristics of a nation
 * to evolve with time. It is exclusively meant to shuttle nation info around.
 * <br><br>
 * Despite the nature of UUIDs, sets are used to ensure the same city/player is added to the list again
 * on account of another part of the code.
 */
public final class CivLevelNation implements NBTSerializable<CompoundTag> {
    public @NotNull UUID nationUUID;
    public @NotNull String nationName;
    public @NotNull Set<CivLevelPlayer> players;
    public @NotNull Set<CivLevelCity> cities;
    public @NotNull Set<HexTilePos> tiles;
    public int diplomacy;
    public CivLevelNation(
            @NotNull UUID nationUUID,
            @NotNull String nationName,
            @NotNull Set<CivLevelPlayer> players,
            @NotNull Set<CivLevelCity> cities,
            @NotNull Set<HexTilePos> tiles,
            int diplomacy
    ) {
        this.nationUUID = nationUUID;
        this.nationName = nationName;
        this.players = players;
        this.cities = cities;
        this.tiles = tiles;
        this.diplomacy = diplomacy;
    }

    public CivLevelNation(CompoundTag nationNBT) {
        this(
                nationNBT.getUUID("uuid"),
                nationNBT.getString("name"),
                CivNBT.listTagToSet(nationNBT.getList("players", Tag.TAG_COMPOUND),
                        tag -> new CivLevelPlayer(((CompoundTag) tag))),
                CivNBT.listTagToSet(nationNBT.getList("tiles", Tag.TAG_LIST),
                        tag -> new CivLevelCity(((CompoundTag) tag))),
                CivNBT.listTagToSet(nationNBT.getList("tiles", Tag.TAG_LIST),
                        tag -> new HexTilePos(((IntArrayTag) tag))),
                nationNBT.getInt("diplomacy"));
    }

    @Override
    public @NotNull CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();

        compoundTag.putUUID("nation_id", this.nationUUID);
        compoundTag.putString("nation_name", this.nationName);
        compoundTag.put("players", CivNBT.collectionToListTag(this.players));
        compoundTag.put("cities", CivNBT.collectionToListTag(this.cities));
        compoundTag.put("tiles", CivNBT.collectionToListTag(this.tiles));
        compoundTag.putInt("diplomacy", this.diplomacy);

        return compoundTag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CivLevelNation) obj;
        return Objects.equals(this.nationUUID, that.nationUUID) &&
                Objects.equals(this.nationName, that.nationName) &&
                Objects.equals(this.players, that.players) &&
                Objects.equals(this.cities, that.cities) &&
                Objects.equals(this.tiles, that.tiles) &&
                this.diplomacy == that.diplomacy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nationUUID, nationName, players, cities, tiles, diplomacy);
    }

    @Override
    public String toString() {
        return "CivLevelNation[" +
                "nationUUID=" + nationUUID + ", " +
                "nationName=" + nationName + ", " +
                "players=" + players + ", " +
                "cities=" + cities + ", " +
                "tiles=" + tiles + ", " +
                "diplomacy=" + diplomacy + ']';
    }

}
