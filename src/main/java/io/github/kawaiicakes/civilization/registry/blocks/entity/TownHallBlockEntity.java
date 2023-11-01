package io.github.kawaiicakes.civilization.registry.blocks.entity;

import io.github.kawaiicakes.civilization.client.menu.TownHallMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static io.github.kawaiicakes.civilization.registry.Registry.BlockEntities.TOWN_HALL_BLOCK_ENTITY;

public class TownHallBlockEntity extends BlockEntity implements MenuProvider {
    public final UUID cityId;
    public String cityName;

    protected final ContainerData data = new SimpleContainerData(0);

    protected final int[] buildVolume = new int[3];
    // Make this final
    protected int minimumValue;

    public TownHallBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.cityId = UUID.randomUUID();
    }

    public TownHallBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(TOWN_HALL_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.cityId = UUID.randomUUID();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.civilization.town_hall");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new TownHallMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, TownHallBlockEntity townHallBlockEntity) {
        if (level.isClientSide) return;
    }
}
