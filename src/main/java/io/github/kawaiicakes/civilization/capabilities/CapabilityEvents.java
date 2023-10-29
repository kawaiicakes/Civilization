package io.github.kawaiicakes.civilization.capabilities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;
import static io.github.kawaiicakes.civilization.capabilities.CivGlobalDataCapability.Provider.CIV_GLOBAL_CAP;

public class CapabilityEvents {

    // TODO: if fake dimensions like the Create: Aeronautics worldshell space are ServerLevel instances, this poses a problem...
    // A solution I can think of is including a configurable list of ResourceLocations corresponding to regions in which this mod should fully work...
    @SubscribeEvent
    public static void attachGlobalData(AttachCapabilitiesEvent<Level> event) {
        if (!(event.getObject() instanceof ServerLevel level)) return;

        if (level.getCapability(CIV_GLOBAL_CAP).isPresent()) return;

        event.addCapability(new ResourceLocation(MOD_ID, "global_data"), new CivGlobalDataCapability.Provider());
    }
}
