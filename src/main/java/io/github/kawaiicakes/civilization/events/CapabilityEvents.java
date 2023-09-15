package io.github.kawaiicakes.civilization.events;

import io.github.kawaiicakes.civilization.capabilities.CivLevelCapabilityProvider;
import io.github.kawaiicakes.civilization.capabilities.CivPlayerCapability;
import io.github.kawaiicakes.civilization.capabilities.CivPlayerCapabilityProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;

public class CapabilityEvents {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if(!player.getCapability(CivPlayerCapabilityProvider.PLAYER_NATION).isPresent()) {
                event.addCapability(new ResourceLocation(MOD_ID, "properties"), new CivPlayerCapabilityProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesLevel(AttachCapabilitiesEvent<Level> event) {
        if (event.getObject() instanceof ServerLevel server && !event.getObject().isClientSide()) {
            if (!server.getCapability(CivLevelCapabilityProvider.CIV_LEVEL_CAP).isPresent()) {
                event.addCapability(new ResourceLocation(MOD_ID, "level_data"), new CivLevelCapabilityProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(CivPlayerCapabilityProvider.PLAYER_NATION).ifPresent(original ->
                    event.getOriginal().getCapability(CivPlayerCapabilityProvider.PLAYER_NATION).ifPresent(copy ->
                            copy.copyFrom(original)));
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(CivPlayerCapability.class);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            event.player.getCapability(CivPlayerCapabilityProvider.PLAYER_NATION).ifPresent(civPlayerCapability -> {
                // TODO
            });
        }
    }
}
