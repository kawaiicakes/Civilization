package io.github.kawaiicakes.civilization.api.nations;

import io.github.kawaiicakes.civilization.api.utils.NBTSerializable;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public final class CivLevelPlayer implements NBTSerializable<CompoundTag> {
    public @NotNull UUID playerId;

    public CivLevelPlayer(
            @NotNull UUID playerId
    ) {
        this.playerId = playerId;
    }

    public CivLevelPlayer(CompoundTag tag) {
        this(tag.getUUID("player_id"));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putUUID("player_id", this.playerId);

        return tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CivLevelPlayer) obj;
        return Objects.equals(this.playerId, that.playerId);
    }
}
