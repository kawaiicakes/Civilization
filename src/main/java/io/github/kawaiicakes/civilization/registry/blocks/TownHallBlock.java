package io.github.kawaiicakes.civilization.registry.blocks;

import io.github.kawaiicakes.civilization.registry.blocks.entity.TownHallBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import static io.github.kawaiicakes.civilization.registry.BlockEntityRegistry.TOWN_HALL;

public class TownHallBlock extends BaseEntityBlock {
    public TownHallBlock(Properties pProperties) {
        super(pProperties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!(pPlayer instanceof ServerPlayer player)) return InteractionResult.sidedSuccess(true);

        if (pLevel.getBlockEntity(pPos) instanceof TownHallBlockEntity entity) {
            NetworkHooks.openScreen(player, entity, pPos);
        } else {
            throw new IllegalStateException("There is no MenuProvider for TownHallBlock!");
        }

        return InteractionResult.sidedSuccess(false);
    }

    @SuppressWarnings("deprecation")
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TownHallBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, TOWN_HALL.get(), TownHallBlockEntity::tick);
    }
}
