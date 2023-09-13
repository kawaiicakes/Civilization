package io.github.kawaiicakes.civilization.api.nations;

import io.github.kawaiicakes.civilization.api.level.HexTilePos;

import java.util.List;
import java.util.UUID;

/**
 * This record class holds all information needed to define a nation. It can be passed around for data transfer.
 * Long-term storage is not recommended as many actions in-game may cause the characteristics of a nation
 * to evolve with time. It is exclusively meant to shuttle nation info around.
 * <br><br>
 * Originally written to pass data back and forth with NBT. The choice to make it a record was made because
 * as a data 'shipping' class, the contents should be immutable such as to discourage changing it as it's passed
 * around.
 */
public record Nation(UUID nationUUID, String nationName, List<UUID> players, List<UUID> cities, List<HexTilePos> territory, int diplomacy) {
}
