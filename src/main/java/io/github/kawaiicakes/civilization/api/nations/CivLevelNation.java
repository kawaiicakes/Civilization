package io.github.kawaiicakes.civilization.api.nations;

import io.github.kawaiicakes.civilization.api.level.HexTilePos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * This class holds all information needed to define a nation. It can be passed around for data transfer.
 * Long-term storage is not recommended as many actions in-game may cause the characteristics of a nation
 * to evolve with time. It is exclusively meant to shuttle nation info around.
 * <br><br>
 * Despite the nature of UUIDs, sets are used to ensure the same city/player is added to the list again
 * on account of another part of the code.
 */
public final class CivLevelNation extends CivLevelData<CompoundTag> {
    public UUID nationUUID;
    public String nationName;
    public Set<CivLevelPlayer> players;
    public Set<CivLevelCity> cities;
    public Set<HexTilePos> tiles;
    public int diplomacy;

    public CivLevelNation(
            UUID nationUUID,
            String nationName,
            Set<CivLevelPlayer> players,
            Set<CivLevelCity> cities,
            Set<HexTilePos> tiles,
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
                nationNBT.getUUID("nation_id"),
                nationNBT.getString("nation_name"),
                listTagToSet(nationNBT.getList("players", Tag.TAG_COMPOUND),
                        tag -> new CivLevelPlayer(((CompoundTag) tag))),
                listTagToSet(nationNBT.getList("cities", Tag.TAG_LIST),
                        tag -> new CivLevelCity(((CompoundTag) tag))),
                listTagToSet(nationNBT.getList("tiles", Tag.TAG_LIST),
                        tag -> new HexTilePos(((IntArrayTag) tag))),
                nationNBT.getInt("diplomacy"));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();

        compoundTag.putUUID("nation_id", this.nationUUID);
        compoundTag.putString("nation_name", this.nationName);
        compoundTag.put("players", collectionToListTag(this.players));
        compoundTag.put("cities", collectionToListTag(this.cities));
        compoundTag.put("tiles", collectionToListTag(this.tiles));
        compoundTag.putInt("diplomacy", this.diplomacy);

        return compoundTag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj.getClass() != this.getClass()) return false;
        var that = (CivLevelNation) obj;
        return Objects.equals(this.nationUUID, that.nationUUID) &&
                Objects.equals(this.nationName, that.nationName) &&
                Objects.equals(this.players, that.players) &&
                Objects.equals(this.cities, that.cities) &&
                Objects.equals(this.tiles, that.tiles) &&
                this.diplomacy == that.diplomacy;
    }
}
