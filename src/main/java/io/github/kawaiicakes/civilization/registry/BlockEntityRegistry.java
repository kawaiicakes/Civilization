package io.github.kawaiicakes.civilization.registry;

import io.github.kawaiicakes.civilization.registry.blocks.entity.TownHallBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;
import static net.minecraftforge.registries.ForgeRegistries.BLOCK_ENTITY_TYPES;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY =
            DeferredRegister.create(BLOCK_ENTITY_TYPES, MOD_ID);

    public static final RegistryObject<BlockEntityType<TownHallBlockEntity>> TOWN_HALL =
            REGISTRY.register("town_hall", () -> BlockEntityType.Builder.of(TownHallBlockEntity::new,
                    BlockRegistry.TOWN_HALL.get()).build(null));

    public static void register(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
