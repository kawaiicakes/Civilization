package io.github.kawaiicakes.civilization;

import com.mojang.logging.LogUtils;
import io.github.kawaiicakes.civilization.events.CapabilityEvents;
import io.github.kawaiicakes.civilization.events.DebugEvents;
import io.github.kawaiicakes.civilization.network.CivPacketHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Civilization.MOD_ID)
public class Civilization
{
    public static final String MOD_ID = "civilization";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Style STYLE = Style.EMPTY.withFont(Style.DEFAULT_FONT).withBold(true).withColor(ChatFormatting.GOLD);
    private static final MutableComponent CHAT_HEADER = Component.literal("❖ Civilization ❭  ").setStyle(STYLE);
    public static MutableComponent CHAT_HEADER() {
        return CHAT_HEADER.copy();
    }

    public Civilization()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(DebugEvents.class);
        MinecraftForge.EVENT_BUS.register(CapabilityEvents.class);

        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        CivPacketHandler.register();
    }
}
