package io.github.kawaiicakes.civilization.nbt;

import io.github.kawaiicakes.civilization.api.level.HexTilePos;
import io.github.kawaiicakes.civilization.api.nbt.CivLevelData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Used to easily convey city data to and from NBT and in other contexts.
 * Don't cache values returned from the level as these are not intended to store live data.
 */
public class CivLevelCity extends CivLevelData<CompoundTag> {
    public UUID cityId;
    public String cityName;
    public Set<HexTilePos> tiles;
    public Set<CivLevelPlayer> citizens;
    public int diplomacy;

    public CivLevelCity(
            UUID cityId,
            String cityName,
            Set<HexTilePos> tiles,
            Set<CivLevelPlayer> citizens,
            int diplomacy
    ) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.tiles = tiles;
        this.citizens = citizens;
        this.diplomacy = diplomacy;
    }

    public CivLevelCity(CompoundTag nbt) {
        this(
                nbt.getUUID("city_id"),
                nbt.getString("city_name"),
                listTagToSet(nbt.getList("tiles", Tag.TAG_INT_ARRAY),
                        tag -> new HexTilePos(((IntArrayTag) tag).getAsIntArray())),
                listTagToSet(nbt.getList("citizens", Tag.TAG_COMPOUND),
                        tag -> new CivLevelPlayer(((CompoundTag) tag).getUUID("id"))),
                nbt.getInt("diplomacy")
        );
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();

        compoundTag.putUUID("city_id", this.cityId);
        compoundTag.putString("city_name", this.cityName);
        compoundTag.put("tiles", collectionToListTag(this.tiles));
        compoundTag.put("citizens", collectionToListTag(this.citizens));
        compoundTag.putInt("diplomacy", 0);

        return compoundTag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj.getClass() != this.getClass()) return false;
        var that = (CivLevelCity) obj;
        return Objects.equals(this.cityId, that.cityId) &&
                Objects.equals(this.cityName, that.cityName) &&
                Objects.equals(this.tiles, that.tiles) &&
                Objects.equals(this.citizens, that.citizens) &&
                this.diplomacy == that.diplomacy;
    }
}
