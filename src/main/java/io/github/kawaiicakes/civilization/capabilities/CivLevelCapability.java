package io.github.kawaiicakes.civilization.capabilities;

import io.github.kawaiicakes.civilization.api.nations.LevelNation;
import io.github.kawaiicakes.civilization.api.utils.CivNBT;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CivLevelCapability {
    // TODO: consider replacing list with collection. Order isn't vital; at least it's not thought to be rn.
    @NotNull
    private List<LevelNation> levelNations = new ArrayList<>();

    public @NotNull List<LevelNation> getNations() {
        return this.levelNations;
    }

    public void setNations(@NotNull List<LevelNation> levelNations) {
        this.levelNations = levelNations;
    }

    public void addNation(LevelNation levelNation) {
        this.levelNations.add(levelNation);
    }

    public void saveNBT(CompoundTag nbt) {
        this.saveNationsNBT(nbt);
    }

    public void loadNBT(CompoundTag nbt) {
        this.loadNationsNBT(nbt);
    }

    private void saveNationsNBT(CompoundTag nbt) {
        ListTag nationsNBTList = new ListTag();

        this.levelNations.forEach(levelNation -> {
            if (levelNation != null) nationsNBTList.add(CivNBT.fromNation(levelNation));
        });

        nbt.put("levelNations", nationsNBTList);
    }

    private void loadNationsNBT(CompoundTag nbt) {
        if (nbt != null && nbt.get("levelNations") != null) {
            if (nbt.get("levelNations") instanceof ListTag nationNBTList) {

                List<LevelNation> levelNationList = new ArrayList<>(nationNBTList.size());

                nationNBTList.forEach(n -> {
                    CompoundTag nationNBT = (CompoundTag) n;

                    levelNationList.add(CivNBT.fromNBT(nationNBT));
                });

                this.setNations(levelNationList);
            }
        }
    }
}
