package io.github.kawaiicakes.civilization.capabilities;

import io.github.kawaiicakes.civilization.api.nations.CivLevelNation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CivLevelCapability {
    @NotNull
    private List<CivLevelNation> civLevelNations = new ArrayList<>();

    public @NotNull List<CivLevelNation> getNations() {
        return this.civLevelNations;
    }

    public void setNations(@NotNull List<CivLevelNation> civLevelNations) {
        this.civLevelNations = civLevelNations;
    }

    public void addNation(CivLevelNation civLevelNation) {
        this.civLevelNations.add(civLevelNation);
    }

    public void saveNBT(CompoundTag nbt) {
        this.saveNationsNBT(nbt);
    }

    public void loadNBT(CompoundTag nbt) {
        this.loadNationsNBT(nbt);
    }

    private void saveNationsNBT(CompoundTag nbt) {
        ListTag nationsNBTList = new ListTag();

        this.civLevelNations.forEach(levelNation -> {
            if (levelNation != null) nationsNBTList.add(levelNation.serializeNBT());
        });

        nbt.put("civLevelNations", nationsNBTList);
    }

    private void loadNationsNBT(CompoundTag nbt) {
        if (nbt != null && nbt.get("civLevelNations") != null) {
            if (nbt.get("civLevelNations") instanceof ListTag nationNBTList) {

                List<CivLevelNation> civLevelNationList = new ArrayList<>(nationNBTList.size());

                nationNBTList.forEach(n -> {
                    CompoundTag nationNBT = (CompoundTag) n;

                    civLevelNationList.add(new CivLevelNation(nationNBT));
                });

                this.setNations(civLevelNationList);
            }
        }
    }
}
