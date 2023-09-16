package io.github.kawaiicakes.civilization.api.nations;

import io.github.kawaiicakes.civilization.api.level.HexTilePos;
import net.minecraft.core.NonNullList;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * This record class holds all information needed to define a nation. It can be passed around for data transfer.
 * Long-term storage is not recommended as many actions in-game may cause the characteristics of a nation
 * to evolve with time. It is exclusively meant to shuttle nation info around.
 * <br><br>
 * Originally written to pass data back and forth with NBT. The choice to make it a record was made because
 * as a data 'shipping' class, the contents should be immutable such as to discourage changing it as it's passed
 * around.
 * <br><br>
 * Despite the nature of UUIDs, sets are used to ensure the same city/player is added to the list again
 * on account of another part of the code.
 */
public record LevelNation(
        @NotNull UUID nationUUID,
        @NotNull String nationName,
        @NotNull Set<UUID> players,
        @NotNull Set<UUID> cities,
        @NotNull Set<HexTilePos> territory,
        int diplomacy
) {
}