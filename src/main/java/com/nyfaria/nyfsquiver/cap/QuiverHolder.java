package com.nyfaria.nyfsquiver.cap;

import com.nyfaria.nyfsquiver.items.QuiverInventory;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import com.nyfaria.nyfsquiver.items.QuiverType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class QuiverHolder implements INBTSavable<CompoundNBT> {
    protected final ItemStack quiverStack;
    private QuiverInventory inventory;
    private int currentSlot = 0;

    protected QuiverHolder(ItemStack itemStack) {
        this.quiverStack = itemStack;
        QuiverType type = ((QuiverItem)itemStack.getItem()).type;
        this.inventory  = new QuiverInventory(false, type.getRows(),type.getColumns());
    }

    @Override
    public CompoundNBT serializeNBT(boolean savingToDisk) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("inventory",inventory.serializeNBT());
        nbt.putInt("currentslot",currentSlot);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt, boolean readingFromDisk) {
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
