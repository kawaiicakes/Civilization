package io.github.kawaiicakes.civilization.api.blocks.entity;

import io.github.kawaiicakes.civilization.api.level.HexTilePos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Subclasses are blocks that in some way may be used to claim a tile.
 */
public abstract class ClaimBlockEntity extends BlockEntity {
    public final HexTilePos parentTile;

    public ClaimBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.parentTile = HexTilePos.blockToHexPos(pPos);
    }

    public HexTilePos getTilePos() {
        return HexTilePos.blockToHexPos(this.getBlockPos());
    }
}
