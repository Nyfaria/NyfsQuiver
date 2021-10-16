package com.nyfaria.nyfsquiver.common.containers;

import com.nyfaria.nyfsquiver.common.api.Dimension;
import com.nyfaria.nyfsquiver.common.api.Point;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Set;

import com.nyfaria.nyfsquiver.common.CommonProxy;
import com.nyfaria.nyfsquiver.common.items.QuiverInventory;
import com.nyfaria.nyfsquiver.common.items.QuiverItem;
import com.nyfaria.nyfsquiver.common.items.QuiverStorageManager;
import com.nyfaria.nyfsquiver.core.init.ContainerTypeInit;




public class QuiverContainer extends Container{

    public final int rows;
    public final int columns;
    public final int bagSlot;
    private final int padding = 8;
    private final int titleSpace = 10;
     
	public QuiverContainer(int id, PlayerInventory player, int bagSlot, int inventoryIndex, int rows, int columns) {
		super(ContainerTypeInit.container, id);
		this.bagSlot = bagSlot;
		this.rows = rows;
        this.columns = columns;
		
        QuiverInventory inventory = player.player.level.isClientSide ?
                new QuiverInventory(true, inventoryIndex, rows, columns) :
                QuiverStorageManager.getInventory(inventoryIndex);

            this.addSlots(this.rows, this.columns, inventory, player);
	}
    public Dimension getDimension() {
        return new Dimension(padding * 2 + Math.max(this.columns, 9) * 18, padding * 2 + titleSpace * 2 + 8 + (this.rows + 4) * 18);
    }
    public Point getQuiverSlotPosition(Dimension dimension, int x, int y) {
        return new Point(dimension.getWidth() / 2 - columns * 9 + x * 18, padding + titleSpace + y * 18);
    }
    public Point getPlayerInvSlotPosition(Dimension dimension, int x, int y) {

        return new Point(dimension.getWidth() / 2 - 9 * 9 + x * 18, dimension.getHeight() - padding - 4 * 18 - 3 + y * 18 + (y == 3 ? 4 : 0));

    }

    @SuppressWarnings("unused")
	private void addSlots(int rows, int columns, IItemHandler inventory, PlayerInventory player){
	    Dimension dimension = getDimension();
        int startX = 8;
        int startY = rows < 9 ? 17 : 8;

        rows = Math.min(rows, 9);
        for(int row = 0; row < rows; row++){
            for(int column = 0; column < columns; column++){
                Point getQuiverSlotPosition = getQuiverSlotPosition(dimension,column, row);
                int index = row * columns + column;
                this.addSlot(new SlotItemHandler(inventory, index, getQuiverSlotPosition.x, getQuiverSlotPosition.y));
            }
        }

        startX = 8 + (columns - 9) * 9;
        startY += rows * 18 + (rows == 9 ? 4 : 13);

        // player slots
        for(int row = 0; row < 3; row++){
            for(int column = 0; column < 9; column++){
                Point playerInvSlotPosition = getPlayerInvSlotPosition(dimension,column, row);
                int index = column + row * 9 + 9;
                if(index == this.bagSlot)
                    this.addSlot(new Slot(player, index, playerInvSlotPosition.x, playerInvSlotPosition.y) {
						public boolean canTakeStack(PlayerEntity playerIn){
                            return false;
                        }
                    });
                else
                    this.addSlot(new Slot(player, index, playerInvSlotPosition.x, playerInvSlotPosition.y));
            }
        }

        startY += 58;

        for(int column = 0; column < 9; column++){
            Point playerInvSlotPosition = getPlayerInvSlotPosition(dimension, column, 3);
            if(column == this.bagSlot)
                this.addSlot(new Slot(player, column, playerInvSlotPosition.x, playerInvSlotPosition.y) {
                    public boolean canTakeStack(PlayerEntity playerIn){
                        return false;
                    }
                });
            else
                this.addSlot(new Slot(player, column, playerInvSlotPosition.x, playerInvSlotPosition.y));
        }
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn){
        return true;
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index){
        ItemStack returnStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if(slot != null && slot.hasItem()){
            ItemStack slotStack = slot.getItem();
            returnStack = slotStack.copy();
            if(index < this.rows * this.columns){
                if(!this.moveItemStackTo(slotStack, this.rows * 9, this.slots.size(), true)){
                    return ItemStack.EMPTY;
                }
            }else if(!this.moveItemStackTo(slotStack, 0, this.rows * 9, false)){
                return ItemStack.EMPTY;
            }

            if(slotStack.isEmpty()){
                slot.set(ItemStack.EMPTY);
            }else{
                slot.setChanged();
            }
        }

        return returnStack;
    }

    @Override
    public ItemStack clicked(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player){
        if(clickTypeIn == ClickType.SWAP && dragType == this.bagSlot)
            return ItemStack.EMPTY;
        if(clickTypeIn == ClickType.PICKUP && dragType == 1 && slotId >= 0){
            Slot slot = this.getSlot(slotId);
            if(slot.mayPickup(player)){
                ItemStack stack = slot.getItem();
                if(stack.getItem() instanceof QuiverItem){
                    if(!player.level.isClientSide){
                        int bagSlot = slotId >= (this.rows + 3) * 9 ? slotId - (this.rows + 3) * 9 : slotId >= this.rows * 9 ? slotId - (this.rows - 1) * 9 : -1;
                        CommonProxy.openQuiverInventory(stack, player, bagSlot);
                    }
                    return ItemStack.EMPTY;
                }
            }
        }
        return super.clicked(slotId, dragType, clickTypeIn, player);
    }
}
