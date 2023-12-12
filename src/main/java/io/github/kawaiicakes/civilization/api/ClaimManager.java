package io.github.kawaiicakes.civilization.api;

import io.github.kawaiicakes.civilization.api.level.HexTilePos;
import io.github.kawaiicakes.civilization.capabilities.CivLevelCapability;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.LazyOptional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static io.github.kawaiicakes.civilization.capabilities.CivLevelCapability.Provider.CIV_LEVEL_CAP;

public class ClaimManager {
    protected static final Map<HexTilePos, UUID> CLAIM_MAP = new HashMap<>();

    public static void setTileOwner(ServerLevel level, HexTilePos hex, UUID owner) {
        LazyOptional<CivLevelCapability> levelCap = level.getCapability(CIV_LEVEL_CAP);

        hex.toChunkPos().forEach(chunk -> levelCap.ifPresent(cap -> {
            cap.setOwner(chunk, owner);
            CLAIM_MAP.put(hex, owner);
        }));
    }

    /**
     * If the return is present, the tile is owned.
     */
    public static Optional<UUID> tileIsOwned(HexTilePos tile) {
        Optional<UUID> toReturn = Optional.empty();
        if (CLAIM_MAP.containsKey(tile)) toReturn = Optional.of(CLAIM_MAP.get(tile));
        return toReturn;
    }
}
