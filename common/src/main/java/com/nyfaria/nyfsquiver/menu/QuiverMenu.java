package com.nyfaria.nyfsquiver.menu;

import com.nyfaria.nyfsquiver.api.Dimension;
import com.nyfaria.nyfsquiver.api.Point;
import com.nyfaria.nyfsquiver.codec.QuiverFabricCodec;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.init.MenuInit;
import com.nyfaria.nyfsquiver.init.TagInit;
import com.nyfaria.nyfsquiver.item.component.QuiverContainerContents;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class QuiverMenu extends AbstractContainerMenu {
    private final int padding = 8;
    private final int titleSpace = 10;
    private QuiverContainer quiverContainer;

    public QuiverMenu(int pContainerId, Inventory playerInventory, QuiverContainer inventory) {
        super(MenuInit.QUIVER_MENU.get(), pContainerId);
        this.quiverContainer = inventory;
        addSlots(inventory, playerInventory);
    }

    public QuiverMenu(int pContainerId, Inventory playerInventory) {
        this(pContainerId, playerInventory, new QuiverContainer(ItemInit.QUIVER.get().getDefaultInstance()));
    }

    public QuiverMenu(int i, Inventory inventory, RegistryFriendlyByteBuf buff) {
        this(i, inventory,  new QuiverContainer(ItemStack.STREAM_CODEC.decode(buff)));
    }

    public QuiverMenu(int i, Inventory inventory, QuiverFabricCodec quiverFabricCodec) {
        this(i, inventory, new QuiverContainer(quiverFabricCodec.stack()));
    }


    private void addSlots(QuiverContainer inventory, Inventory player) {
        Dimension dimension = getDimension();

        int rows = Math.min(inventory.getRows(), 9);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < inventory.getColumns(); column++) {
                Point getQuiverSlotPosition = getQuiverSlotPosition(dimension, column, row);
                int index = row * inventory.getColumns() + column;
                this.addSlot(new Slot(inventory, index, getQuiverSlotPosition.x, getQuiverSlotPosition.y){
                    @Override
                    public boolean mayPlace(ItemStack pStack) {
                        return pStack.is(TagInit.QUIVER_ITEMS);
                    }
                });
            }
        }


        // player slots
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                Point playerInvSlotPosition = getPlayerInvSlotPosition(dimension, column, row);
                int index = column + row * 9 + 9;
                this.addSlot(new Slot(player, index, playerInvSlotPosition.x, playerInvSlotPosition.y));
            }
        }


        for (int column = 0; column < 9; column++) {
            Point playerInvSlotPosition = getPlayerInvSlotPosition(dimension, column, 3);
            this.addSlot(new Slot(player, column, playerInvSlotPosition.x, playerInvSlotPosition.y));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < this.quiverContainer.getRows() * this.quiverContainer.getColumns()) {
                if (!this.moveItemStackTo(itemstack1, this.quiverContainer.getRows() * this.quiverContainer.getColumns(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.quiverContainer.getRows() * this.quiverContainer.getColumns(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    public Dimension getDimension() {
        return new Dimension(padding * 2 + Math.max(this.quiverContainer.getColumns(), 9) * 18, padding * 2 + titleSpace * 2 + 8 + (this.quiverContainer.getRows() + 4) * 18);
    }

    public Point getPlayerInvSlotPosition(Dimension dimension, int x, int y) {

        return new Point(dimension.getWidth() / 2 - 9 * 9 + x * 18, dimension.getHeight() - padding - 4 * 18 - 3 + y * 18 + (y == 3 ? 4 : 0));

    }

    public Point getQuiverSlotPosition(Dimension dimension, int x, int y) {
        return new Point(dimension.getWidth() / 2 - quiverContainer.getColumns() * 9 + x * 18, padding + titleSpace + y * 18);
    }
}
