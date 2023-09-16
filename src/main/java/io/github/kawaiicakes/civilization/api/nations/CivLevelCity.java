package io.github.kawaiicakes.civilization.api.nations;

import io.github.kawaiicakes.civilization.api.level.HexTilePos;
import io.github.kawaiicakes.civilization.api.utils.CivNBT;
import io.github.kawaiicakes.civilization.api.utils.NBTSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Used to easily convey city data to and from NBT and in other contexts.
 * Don't cache values returned from the level as these are not intended to store live data.
 * They are records for a reason, after all.
 */
public record CivLevelCity(
        UUID cityId,
        String cityName,
        Set<HexTilePos> tiles,
        Map<UUID, CivPermissions> citizens,
        int diplomacy
) implements NBTSerializable<CompoundTag, CivLevelCity> {
    @Override
    public @NotNull CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();

        compoundTag.putUUID("city_id", this.cityId);
        compoundTag.putString("city_name", this.cityName);
        compoundTag.put("tiles", CivNBT.tilesToListTag(this.tiles));
        compoundTag.put("citizens", this.citizensToListTag());
        compoundTag.putInt("diplomacy", 0);

        return compoundTag;
    }

    @Override
    public CivLevelCity deserializeNBT(CompoundTag nbt) {
        return null;
        /*
                new CivLevelCity(
                nbt.getUUID("city_id"),
                nbt.getString("city_name"),
                ((ListTag) Objects.requireNonNull(nbt.get("tiles")))
                        .stream()
                        .map(arr -> (HexTilePos.fromIntArray(((IntArrayTag) arr).getAsIntArray())))
                        .collect(Collectors.toSet()),
                // TODO
        );
        */
    }

    @Deprecated // Marked for removal. This is just a placeholder method so no errors are thrown during runtime
    private ListTag citizensToListTag() {
        ListTag listTag = new ListTag();

        this.citizens.keySet().forEach(uuid -> {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putInt(uuid.toString(), 0); // TODO
            listTag.add(compoundTag);
        });

        return listTag;
    }
}
