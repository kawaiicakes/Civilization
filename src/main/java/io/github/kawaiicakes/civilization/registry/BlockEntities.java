package io.github.kawaiicakes.civilization.registry;

import io.github.kawaiicakes.civilization.registry.blocks.entity.TownHallBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

import static io.github.kawaiicakes.civilization.registry.Registry.BLOCK_ENTITY_REGISTRY;

public class BlockEntities {
    public static final RegistryObject<BlockEntityType<TownHallBlockEntity>> TOWN_HALL =
            BLOCK_ENTITY_REGISTRY.register("town_hall", () -> BlockEntityType.Builder.of(TownHallBlockEntity::new,
                    Blocks.TOWN_HALL.get()).build(null));
}
