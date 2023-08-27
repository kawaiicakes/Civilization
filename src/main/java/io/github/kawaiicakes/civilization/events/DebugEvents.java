package io.github.kawaiicakes.civilization.events;

import io.github.kawaiicakes.civilization.api.level.HexTilePos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static io.github.kawaiicakes.civilization.Civilization.LOGGER;

public class DebugEvents {
    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && !(event.getLevel().isClientSide())) {
            HexTilePos hex = HexTilePos.blockToHexPos(event.getPos());
            LOGGER.info(hex.getPrettyCoordinates());
            player.sendSystemMessage(Component.literal("Placed block in hex " + hex.getPrettyCoordinates()));
        }
    }
}
