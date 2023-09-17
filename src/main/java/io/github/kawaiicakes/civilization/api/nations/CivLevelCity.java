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
 * Used to easily convey city data to and from NBT and in other contexts.
 * Don't cache values returned from the level as these are not intended to store live data.
 * They are records for a reason, after all.
 */
public class CivLevelCity implements NBTSerializable<CompoundTag> {
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
        new CivLevelCity(
                nbt.getUUID("city_id"),
                nbt.getString("city_name"),
                CivNBT.listTagToSet(nbt.getList("tiles", Tag.TAG_INT_ARRAY),
                        tag -> new HexTilePos(((IntArrayTag) tag).getAsIntArray())),
                CivNBT.listTagToSet(nbt.getList("citizens", Tag.TAG_COMPOUND),
                        tag -> new CivLevelPlayer(((CompoundTag) tag).getUUID("id"))),
                nbt.getInt("diplomacy")
        );
    }

    @Override
    public @NotNull CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();

        compoundTag.putUUID("city_id", this.cityId);
        compoundTag.putString("city_name", this.cityName);
        compoundTag.put("tiles", CivNBT.collectionToListTag(this.tiles));
        compoundTag.put("citizens", CivNBT.collectionToListTag(this.citizens));
        compoundTag.putInt("diplomacy", 0);

        return compoundTag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CivLevelCity) obj;
        return Objects.equals(this.cityId, that.cityId) &&
                Objects.equals(this.cityName, that.cityName) &&
                Objects.equals(this.tiles, that.tiles) &&
                Objects.equals(this.citizens, that.citizens) &&
                this.diplomacy == that.diplomacy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityId, cityName, tiles, citizens, diplomacy);
    }

    @Override
    public String toString() {
        return "CivLevelCity[" +
                "cityId=" + cityId + ", " +
                "cityName=" + cityName + ", " +
                "tiles=" + tiles + ", " +
                "citizens=" + citizens + ", " +
                "diplomacy=" + diplomacy + ']';
    }
}
