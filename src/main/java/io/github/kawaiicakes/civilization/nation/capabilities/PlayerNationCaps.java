package io.github.kawaiicakes.civilization.nation.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerNationCaps {
    private UUID nation;
    private List<UUID> cities = new ArrayList<>();
    private int diplomacyScore;

    public UUID getNation() {
        return this.nation;
    }

    public List<UUID> getCities() {
        return this.cities;
    }

    public int getDiplomacyScore() {
        return this.diplomacyScore;
    }

    public void setNation(UUID nation) {
        this.nation = nation;
    }

    public void setCities(List<UUID> cities) {
        this.cities = cities;
    }

    public void setDiplomacyScore(int diplomacyScore) {
        this.diplomacyScore = diplomacyScore;
    }

    public void copyFrom(PlayerNationCaps source) {
        //this.setNation(source.getNation());
        this.setCities(source.getCities());
        this.setDiplomacyScore(source.getDiplomacyScore());
    }

    public void saveNBTData(CompoundTag nbt) {
        /*if (this.nation != null) {
            nbt.putUUID("nation", this.nation.nameUUID());
        } else {
            nbt.putUUID("nation", UUID.randomUUID());
        }*/

        ListTag uuidTag = new ListTag();
        this.cities.forEach(UUID -> {
            CompoundTag tag = new CompoundTag();
            tag.putUUID("city", UUID);

            uuidTag.add(tag);
        });
        nbt.put("cities", uuidTag);

        nbt.putInt("diplomacy_score", this.diplomacyScore);
    }

    public void loadNBTData(CompoundTag nbt) {
        //this.nation = UUID.fromUUID(nbt.getUUID("nation"));

        // TODO verify this works
        this.cities = nbt.getList("cities", Tag.TAG_COMPOUND).stream().map(tag ->
                ((CompoundTag) tag).getUUID("city")).toList();

        this.diplomacyScore = nbt.getInt("diplomacy_score");
    }
}
