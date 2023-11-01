package io.github.kawaiicakes.civilization.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import static io.github.kawaiicakes.civilization.registry.Registry.ITEM_REGISTRY;

public class Items {
    public static final RegistryObject<BlockItem> TOWN_HALL =
            ITEM_REGISTRY.register("town_hall", () -> new BlockItem(Blocks.TOWN_HALL.get(), new Item.Properties()));
}
