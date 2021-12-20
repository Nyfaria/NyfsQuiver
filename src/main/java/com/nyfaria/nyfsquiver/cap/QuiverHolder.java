package com.nyfaria.nyfsquiver.cap;

import com.nyfaria.nyfsquiver.items.QuiverInventory;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import com.nyfaria.nyfsquiver.items.QuiverType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class QuiverHolder implements INBTSavable<CompoundTag>{

    protected final ItemStack itemStack;
    private QuiverInventory inventory;
    private int currentSlot = 0;

    public QuiverHolder(ItemStack itemStack) {
        this.itemStack = itemStack;
        QuiverType type = ((QuiverItem)itemStack.getItem()).type;
        this.inventory  = new QuiverInventory(false, type.getRows(),type.getColumns());
    }


    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag tag = new CompoundTag();
        tag.put("inventory", inventory.serializeNBT());
        tag.putInt("currentslot",currentSlot);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        if(nbt == null)return;
        if(nbt.contains("inventory")) {
            inventory.deserializeNBT(nbt.getCompound("inventory"));
            currentSlot = nbt.getInt("currentslot");
        }
    }

    public QuiverInventory getInventory(){
        return inventory;
    }

    public int getCurrentSlot() {
        return currentSlot;
    }

    public void changeCurrentSlot(int direction) {
        if(this.currentSlot + direction == inventory.getSlots()){
            this.currentSlot = 0;
        }
        else if(this.currentSlot + direction == -1){
            this.currentSlot = inventory.getSlots()-1;
        }
        else{
            this.currentSlot+=direction;
        }

    }
}
