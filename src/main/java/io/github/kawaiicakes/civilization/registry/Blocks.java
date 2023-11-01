package io.github.kawaiicakes.civilization.registry;

import io.github.kawaiicakes.civilization.registry.blocks.TownHallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.RegistryObject;

import static io.github.kawaiicakes.civilization.registry.Registry.BLOCK_REGISTRY;

public class Blocks {
    // TODO
    public static final RegistryObject<TownHallBlock> TOWN_HALL = BLOCK_REGISTRY.register("town_hall", () -> new TownHallBlock(BlockBehaviour.Properties.of(Material.WOOD)));

}
