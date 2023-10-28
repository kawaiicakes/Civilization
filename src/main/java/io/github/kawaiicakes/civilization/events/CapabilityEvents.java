package io.github.kawaiicakes.civilization.events;

import io.github.kawaiicakes.civilization.capabilities.CivLevelCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;

public class CapabilityEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesOverworld(AttachCapabilitiesEvent<Level> event) {
        if (!(event.getObject() instanceof ServerLevel level)) return;

        if (level.getCapability(CivLevelCapability.Provider.CIV_LEVEL_CAP).isPresent()) return;

        event.addCapability(new ResourceLocation(MOD_ID, level.dimension().location().getPath() + "_data"), new CivLevelCapability.Provider());
    }
}
