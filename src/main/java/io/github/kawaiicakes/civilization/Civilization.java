package io.github.kawaiicakes.civilization;

import com.mojang.logging.LogUtils;
import io.github.kawaiicakes.civilization.events.DebugEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Civilization.MOD_ID)
public class Civilization
{
    public static final String MOD_ID = "civilization";
    public static final Logger LOGGER = LogUtils.getLogger();
    public Civilization()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(DebugEvents.class);
    }
}
