package io.github.kawaiicakes.civilization.registry;

import io.github.kawaiicakes.civilization.registry.blocks.TownHallBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    // TODO
    public static final RegistryObject<TownHallBlock> TOWN_HALL = BLOCKS.register("town_hall", () -> new TownHallBlock(BlockBehaviour.Properties.of(Material.WOOD)));

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
