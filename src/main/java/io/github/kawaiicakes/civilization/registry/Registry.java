package io.github.kawaiicakes.civilization.registry;

import io.github.kawaiicakes.civilization.client.menu.TownHallMenu;
import io.github.kawaiicakes.civilization.registry.blocks.TownHallBlock;
import io.github.kawaiicakes.civilization.registry.blocks.entity.TownHallBlockEntity;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;
import static net.minecraftforge.registries.ForgeRegistries.*;

public class Registry {
    protected static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTRY = DeferredRegister.create(BLOCK_ENTITY_TYPES, MOD_ID);
    protected static final DeferredRegister<Block> BLOCK_REGISTRY = DeferredRegister.create(BLOCKS, MOD_ID);
    protected static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ITEMS, MOD_ID);
    protected static final DeferredRegister<MenuType<?>> MENU_REGISTRY = DeferredRegister.create(MENU_TYPES, MOD_ID);

    public static void register(IEventBus bus) {
        BLOCK_ENTITY_REGISTRY.register(bus);
        BLOCK_REGISTRY.register(bus);
        ITEM_REGISTRY.register(bus);
        MENU_REGISTRY.register(bus);
    }

    static {
        new Blocks();
        new BlockEntities();
        new Items();
        new Menus();
    }

    public static class Blocks {
        protected Blocks() {}

        public static final RegistryObject<TownHallBlock> TOWN_HALL_BLOCK
                = BLOCK_REGISTRY.register("town_hall", () -> new TownHallBlock(BlockBehaviour.Properties.of(Material.WOOD)));
    }

    public static class BlockEntities {
        protected BlockEntities() {}

        public static final RegistryObject<BlockEntityType<TownHallBlockEntity>> TOWN_HALL_BLOCK_ENTITY =
                BLOCK_ENTITY_REGISTRY.register("town_hall", () -> BlockEntityType.Builder.of(TownHallBlockEntity::new,
                        Blocks.TOWN_HALL_BLOCK.get()).build(null));
    }

    public static class Items {
        protected Items() {}

        public static final RegistryObject<BlockItem> TOWN_HALL_BLOCK_ITEM =
                ITEM_REGISTRY.register("town_hall", () -> new BlockItem(Blocks.TOWN_HALL_BLOCK.get(), new Item.Properties()));
    }

    public static class Menus {
        protected Menus() {}
        public static final RegistryObject<MenuType<TownHallMenu>> TOWN_HALL_MENU =
                MENU_REGISTRY.register("town_hall", () -> IForgeMenuType.create(TownHallMenu::new));
    }
}
