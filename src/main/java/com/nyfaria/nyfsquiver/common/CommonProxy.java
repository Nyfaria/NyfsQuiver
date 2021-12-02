package com.nyfaria.nyfsquiver.common;

import com.nyfaria.nyfsquiver.common.items.QuiverInventory;
import com.nyfaria.nyfsquiver.common.items.QuiverItem;
import com.nyfaria.nyfsquiver.common.items.QuiverStorageManager;
import com.nyfaria.nyfsquiver.common.items.QuiverType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * Created 2/7/2020 by SuperMartijn642
 */
public class CommonProxy {

    public void init(){
        return;
    }

    public PlayerEntity getClientPlayer(){
        return null; 
    }
    public static void nextSlot(ItemStack stack, PlayerEntity player, int bagSlot)
    {
    	int quiverSize = QuiverStorageManager.getInventory(stack.getOrCreateTag().getInt("nyfsquiver:invIndex")).getSlots();
    	int currentSlot = stack.getOrCreateTag().getInt("nyfsquiver:slotIndex");
    	int newSlot = currentSlot + bagSlot;
    	if(newSlot >= quiverSize) {
    		newSlot = 0;
    	}
    	if(newSlot < 0) {
    		newSlot = quiverSize - 1;
    	}
    	System.out.println("OldSlot: " + currentSlot + " NewSlot: " + newSlot);
    	stack.getOrCreateTag().putInt("nyfsquiver:slotIndex", newSlot);
    }

    public static void openQuiverInventory(ItemStack stack, PlayerEntity player, int bagSlot){
        QuiverType type = ((QuiverItem)stack.getItem()).type;
        CompoundNBT compound = stack.getOrCreateTag();
        if(!compound.contains("nyfsquiver:invIndex") || QuiverStorageManager.getInventory(compound.getInt("nyfsquiver:invIndex")) == null){
            compound.putInt("nyfsquiver:invIndex", QuiverStorageManager.createInventoryIndex(type));
            compound.putInt("nyfsquiver:slotIndex", 0);
            stack.setTag(compound);
        }else{
            QuiverInventory inventory = QuiverStorageManager.getInventory(compound.getInt("nyfsquiver:invIndex"));
            int rows = inventory.rows;
            int columns = inventory.columns;
            if(rows != type.getRows())
                inventory.adjustSize(type.getRows(),type.getColumns());
            if(columns != type.getColumns())
                inventory.adjustSize(type.getRows(),type.getColumns());
        }
        int inventoryIndex = compound.getInt("nyfsquiver:invIndex");
        QuiverInventory inventory = QuiverStorageManager.getInventory(inventoryIndex);
        if(bagSlot != -6) {
            NetworkHooks.openGui((ServerPlayerEntity) player, new QuiverItem.ContainerProvider(stack.getDisplayName(), bagSlot, inventoryIndex, inventory), a -> {
                a.writeInt(bagSlot);
                a.writeInt(inventoryIndex);
                a.writeInt(inventory.rows);
                a.writeInt(inventory.columns);
            });
        }
    }

}