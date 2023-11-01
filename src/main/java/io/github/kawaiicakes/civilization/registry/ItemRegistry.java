package io.github.kawaiicakes.civilization.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;
import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

public class ItemRegistry {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ITEMS, MOD_ID);

    public static final RegistryObject<BlockItem> TOWN_HALL =
            REGISTER.register("town_hall", () -> new BlockItem(BlockRegistry.TOWN_HALL.get(), new Item.Properties()));

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
