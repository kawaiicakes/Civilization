package io.github.kawaiicakes.civilization.client.menu;

import io.github.kawaiicakes.civilization.registry.MenuRegistry;
import io.github.kawaiicakes.civilization.registry.blocks.entity.TownHallBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import static io.github.kawaiicakes.civilization.registry.BlockRegistry.TOWN_HALL;

public class TownHallMenu extends AbstractContainerMenu {
    public final TownHallBlockEntity entity;
    private final Level level;

    @SuppressWarnings("DataFlowIssue")
    public TownHallMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, inv.player.level.getBlockEntity(buf.readBlockPos()), new SimpleContainerData(0));
    }

    public TownHallMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(MenuRegistry.TOWN_HALL.get(), id);
        checkContainerSize(inv, 0);
        this.entity = (TownHallBlockEntity) entity;
        this.level = inv.player.level;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        addDataSlots(data);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()), pPlayer, TOWN_HALL.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }
}
