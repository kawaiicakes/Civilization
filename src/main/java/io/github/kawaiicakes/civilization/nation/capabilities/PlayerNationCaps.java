package io.github.kawaiicakes.civilization.nation.capabilities;

import io.github.kawaiicakes.civilization.api.utils.NamedUUID;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public class PlayerNationCaps {
    private NamedUUID nation;
    private List<NamedUUID> cities;
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

    // FIXME: NullPointerException caused by this.nation being null
    public void saveNBTData(CompoundTag nbt) {
        nbt.putUUID("nation", this.nation.nameUUID());
        this.cities.stream().map(NamedUUID::nameUUID).forEach(UUID -> nbt.putUUID("cities", UUID)); // FIXME: can only store one UUID at a time????
        nbt.putInt("diplomacy_score", this.diplomacyScore);
    }

    public void loadNBTData(CompoundTag nbt) {
        this.nation = NamedUUID.fromUUID(nbt.getUUID("nation"));
        // TODO: city stuff once I figure more UUID stuff out
        this.diplomacyScore = nbt.getInt("diplomacy_score");
    }
}
