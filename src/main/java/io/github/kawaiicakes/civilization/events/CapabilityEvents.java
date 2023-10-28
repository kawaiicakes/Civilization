package io.github.kawaiicakes.civilization.events;

import io.github.kawaiicakes.civilization.capabilities.CivLevelCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Objects;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;
import static net.minecraft.world.level.Level.OVERWORLD;

public class CapabilityEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesOverworld(AttachCapabilitiesEvent<Level> event) {
        if (!(event.getObject() instanceof ServerLevel level)) return;
        if (!(Objects.equals(level.dimension(), OVERWORLD))) return;

        if (level.getCapability(CivLevelCapability.Provider.CIV_LEVEL_CAP).isPresent()) return;

        event.addCapability(new ResourceLocation(MOD_ID, "level_data"), new CivLevelCapability.Provider());
    }
}
