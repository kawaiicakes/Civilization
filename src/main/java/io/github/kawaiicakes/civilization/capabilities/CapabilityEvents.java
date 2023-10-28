package io.github.kawaiicakes.civilization.capabilities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;

public class CapabilityEvents {

    // TODO: if fake dimensions like the Create: Aeronautics worldshell space are ServerLevel instances, this poses a problem...
    // A solution I can think of is including a configurable list of ResourceLocations corresponding to regions in which this mod should fully work...
    @SubscribeEvent
    public static void onAttachCapabilitiesOverworld(AttachCapabilitiesEvent<Level> event) {
        if (!(event.getObject() instanceof ServerLevel level)) return;

        if (level.getCapability(CivLevelCapability.Provider.CIV_LEVEL_CAP).isPresent()) return;

        event.addCapability(new ResourceLocation(MOD_ID, level.dimension().location().getPath() + "_data"), new CivLevelCapability.Provider());
    }
}
