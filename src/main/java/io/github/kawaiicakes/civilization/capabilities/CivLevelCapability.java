package io.github.kawaiicakes.civilization.capabilities;

import io.github.kawaiicakes.civilization.api.nations.Nation;
import io.github.kawaiicakes.civilization.api.utils.CivNBT;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CivLevelCapability {
    // TODO: consider replacing list with collection. Order isn't vital; at least it's not thought to be rn.
    @NotNull
    private List<Nation> nations = new ArrayList<>();

    public @NotNull List<Nation> getNations() {
        return this.nations;
    }

    public void setNations(@NotNull List<Nation> nations) {
        this.nations = nations;
    }

    public void addNation(Nation nation) {
        this.nations.add(nation);
    }

    public void saveNBT(CompoundTag nbt) {
        this.saveNationsNBT(nbt);
    }

    public void loadNBT(CompoundTag nbt) {
        this.loadNationsNBT(nbt);
    }

    private void saveNationsNBT(CompoundTag nbt) {
        ListTag nationsNBTList = new ListTag();

        this.nations.forEach(nation -> {
            if (nation != null) nationsNBTList.add(CivNBT.fromNation(nation));
        });

        nbt.put("nations", nationsNBTList);
    }

    private void loadNationsNBT(CompoundTag nbt) {
        if (nbt != null && nbt.get("nations") != null) {
            if (nbt.get("nations") instanceof ListTag nationNBTList) {

                List<Nation> nationList = new ArrayList<>(nationNBTList.size());

                nationNBTList.forEach(n -> {
                    CompoundTag nationNBT = (CompoundTag) n;

                    nationList.add(CivNBT.fromNBT(nationNBT));
                });

                this.setNations(nationList);
            }
        }
    }
}
