package io.github.kawaiicakes.civilization.events;

import io.github.kawaiicakes.civilization.capabilities.PlayerNationCapsProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class CapabilityEvents {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if(!player.getCapability(PlayerNationCapsProvider.PLAYER_NATION).isPresent()) {
                //event.addCapability(new ResourceLocation(MOD_ID, "properties"), new PlayerNationCapsProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerNationCapsProvider.PLAYER_NATION).ifPresent(original ->
                    event.getOriginal().getCapability(PlayerNationCapsProvider.PLAYER_NATION).ifPresent(copy ->
                            copy.copyFrom(original)));
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        //event.register(PlayerNationCaps.class);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            event.player.getCapability(PlayerNationCapsProvider.PLAYER_NATION).ifPresent(playerNationCaps -> {
                // TODO
            });
        }
    }
}
