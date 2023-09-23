package io.github.kawaiicakes.civilization.capabilities;

import io.github.kawaiicakes.civilization.api.nations.CivLevelData;
import io.github.kawaiicakes.civilization.api.nations.CivLevelNation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class CivLevelCapability {
    @NotNull
    private Set<CivLevelNation> civLevelNations = new HashSet<>();

    public @NotNull Set<CivLevelNation> getNations() {
        return this.civLevelNations;
    }

    public void setNations(@NotNull Set<CivLevelNation> civLevelNations) {
        this.civLevelNations = civLevelNations;
    }

    public void addNation(CivLevelNation civLevelNation) {
        this.civLevelNations.add(civLevelNation);
    }

    public void saveNBT(CompoundTag nbt) {
        nbt.put("civLevelNations", CivLevelData.collectionToListTag(this.civLevelNations));
    }

    public void loadNBT(CompoundTag nbt) {
        ListTag nationNBTList = nbt.getList("civLevelNations", Tag.TAG_COMPOUND);

        this.civLevelNations = CivLevelData.listTagToSet(nationNBTList, nat -> new CivLevelNation((CompoundTag) nat));
    }
}
