package io.github.kawaiicakes.civilization.api;

import io.github.kawaiicakes.civilization.api.level.HexTilePos;
import io.github.kawaiicakes.civilization.capabilities.CivLevelCapability;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.LazyOptional;

import java.util.UUID;

import static io.github.kawaiicakes.civilization.capabilities.CivLevelCapability.Provider.CIV_LEVEL_CAP;

public class ClaimManager {
    public static void setTileOwner(ServerLevel level, HexTilePos hex, UUID owner) {
        LazyOptional<CivLevelCapability> levelCap = level.getCapability(CIV_LEVEL_CAP);

        hex.toChunkPos().forEach(chunk -> levelCap.ifPresent(cap -> cap.setOwner(chunk, owner)));
    }
}
