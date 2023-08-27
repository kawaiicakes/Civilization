package io.github.kawaiicakes.civilization.blocks.entity;

import io.github.kawaiicakes.civilization.api.blocks.entity.AbstractClaimBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TownHallBlockEntity extends AbstractClaimBlockEntity {
    public String cityName;

    public TownHallBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }
}
