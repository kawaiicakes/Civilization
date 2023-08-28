package io.github.kawaiicakes.civilization.nation.capabilities;

import io.github.kawaiicakes.civilization.api.utils.NamedUUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerNationCaps {
    private NamedUUID nation;
    private List<NamedUUID> cities = new ArrayList<>();
    private int diplomacyScore;

    public NamedUUID getNation() {
        return this.nation;
    }

    public List<NamedUUID> getCities() {
        return this.cities;
    }

    public int getDiplomacyScore() {
        return this.diplomacyScore;
    }

    public void setNation(NamedUUID nation) {
        this.nation = nation;
    }

    public void setCities(List<NamedUUID> cities) {
        this.cities = cities;
    }

    public void setDiplomacyScore(int diplomacyScore) {
        this.diplomacyScore = diplomacyScore;
    }

    public void copyFrom(PlayerNationCaps source) {
        this.setNation(source.getNation());
        this.setCities(source.getCities());
        this.setDiplomacyScore(source.getDiplomacyScore());
    }

    public void saveNBTData(CompoundTag nbt) {
        if (this.nation != null) {
            nbt.putUUID("nation", this.nation.nameUUID());
        }

        ListTag uuidTag = new ListTag();
        this.cities.stream().map(NamedUUID::nameUUID).forEach(UUID -> {
            CompoundTag tag = new CompoundTag();
            tag.putUUID("city", UUID);

            uuidTag.add(tag);
        });
        nbt.put("cities", uuidTag);

        nbt.putInt("diplomacy_score", this.diplomacyScore);
    }

    public void loadNBTData(CompoundTag nbt) {
        this.nation = NamedUUID.fromUUID(nbt.getUUID("nation"));

        // TODO verify this works
        this.cities = nbt.getList("cities", Tag.TAG_COMPOUND).stream().map(tag ->
                NamedUUID.fromUUID(UUID.fromString(tag.getAsString()))).toList();

        this.diplomacyScore = nbt.getInt("diplomacy_score");
    }
}
