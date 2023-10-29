package io.github.kawaiicakes.civilization.capabilities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Objects;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;
import static io.github.kawaiicakes.civilization.capabilities.CivGlobalDataCapability.Provider.CIV_GLOBAL_CAP;
import static io.github.kawaiicakes.civilization.capabilities.CivLevelCapability.Provider.CIV_LEVEL_CAP;
import static net.minecraft.world.level.Level.OVERWORLD;

public class CapabilityEvents {
    @SubscribeEvent
    public static void attachGlobalData(AttachCapabilitiesEvent<Level> event) {
        if (!(event.getObject() instanceof ServerLevel level)) return;
        if (!(Objects.equals(level.dimension(), OVERWORLD))) return;
        if (level.getCapability(CIV_GLOBAL_CAP).isPresent()) return;

        event.addCapability(new ResourceLocation(MOD_ID, "global_data"), new CivGlobalDataCapability.Provider());
    }

    // TODO: if fake dimensions like the Create: Aeronautics worldshell space are ServerLevel instances, this poses a problem...
    // A solution I can think of is including a configurable list of ResourceLocations corresponding to regions in which this mod should fully work...
    @SubscribeEvent
    public static void attachLevelData(AttachCapabilitiesEvent<Level> event) {
        if (event.getObject().isClientSide) return;

        if (event.getObject().getCapability(CIV_LEVEL_CAP).isPresent()) return;

        event.addCapability(
                new ResourceLocation(MOD_ID, "level_" + event.getObject().dimension().location().getPath() + "_data"),
                new CivLevelCapability.Provider()
        );
    }
}
