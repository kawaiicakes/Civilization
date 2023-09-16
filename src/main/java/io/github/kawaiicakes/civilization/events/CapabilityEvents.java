package io.github.kawaiicakes.civilization.events;

import io.github.kawaiicakes.civilization.capabilities.CivLevelCapabilityProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;

public class CapabilityEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesLevel(AttachCapabilitiesEvent<Level> event) {
        if (event.getObject() instanceof ServerLevel server && !event.getObject().isClientSide()) {
            if (!server.getCapability(CivLevelCapabilityProvider.CIV_LEVEL_CAP).isPresent()) {
                event.addCapability(new ResourceLocation(MOD_ID, "level_data"), new CivLevelCapabilityProvider());
            }
        }
    }
}
