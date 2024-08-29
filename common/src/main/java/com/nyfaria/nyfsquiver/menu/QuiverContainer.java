package com.nyfaria.nyfsquiver.menu;

import com.nyfaria.nyfsquiver.init.DataComponentInit;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;

public class QuiverContainer implements Container {

    private ItemStack contents;
    private NonNullList<ItemStack> items;

    public QuiverContainer(ItemStack quiverStack) {
        this.contents = quiverStack;
//        this.items = contents.get(DataComponentInit.QUIVER_CONTENTS.get()).items();
        this.items = NonNullList.withSize(getColumns() * getRows(), ItemStack.EMPTY);
        contents.get(DataComponents.CONTAINER).copyInto(items);
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
//        return contents.get(DataComponentInit.QUIVER_CONTENTS.get()).isEmpty();
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return items.get(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack stack = items.get(pSlot);
        ItemStack newStack = stack.split(pAmount);
        if (stack.isEmpty()) {
            items.set(pSlot, ItemStack.EMPTY);
        }
        setChanged();
        return newStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        ItemStack stack = items.get(pSlot);
        items.set(pSlot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        items.set(pSlot, pStack);
        setChanged();
    }

    @Override
    public void setChanged() {
//        contents.set(DataComponentInit.QUIVER_CONTENTS.get(), QuiverContainerContents.fromItems(items));
        contents.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(items));
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public boolean canPlaceItem(int pSlot, ItemStack pStack) {
        return pStack.is(Items.ARROW);
    }

    @Override
    public void clearContent() {
        items.clear();
        setChanged();
    }

    public int getColumns() {
        return contents.get(DataComponentInit.QUIVER_TYPE.get()).columns();
    }

    public int getRows() {
        return contents.get(DataComponentInit.QUIVER_TYPE.get()).rows();
    }

    public ItemStack add(int pSlot, ItemStack pStack) {
        if (pStack.isEmpty()) {
            return pStack;
        } else {
            try {
                if (pStack.isDamaged()) {
                    if (pSlot == -1) {
                        pSlot = this.getFreeSlot();
                    }

                    if (pSlot >= 0) {
                        this.items.set(pSlot, pStack.copyAndClear());
                        this.items.get(pSlot).setPopTime(5);
                        this.setChanged();
                        return pStack;
                    } else {
                        return pStack;
                    }
                } else {
                    int i;
                    do {
                        i = pStack.getCount();
                        if (pSlot == -1) {
                            pStack.setCount(this.addResource(pStack));
                        } else {
                            pStack.setCount(this.addResource(pSlot, pStack));
                        }
                    } while (!pStack.isEmpty() && pStack.getCount() < i);
                    setChanged();
                    return pStack;
                }
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.forThrowable(throwable, "Adding item to inventory");
                CrashReportCategory crashreportcategory = crashreport.addCategory("Item being added");
                crashreportcategory.setDetail("Item ID", Item.getId(pStack.getItem()));
                crashreportcategory.setDetail("Item data", pStack.getDamageValue());
                crashreportcategory.setDetail("Item name", () -> pStack.getHoverName().getString());
                throw new ReportedException(crashreport);
            }
        }
    }

    public int getFreeSlot() {
        for (int i = 0; i < this.items.size(); i++) {
            if (this.items.get(i).isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    private int addResource(ItemStack pStack) {
        int i = this.getSlotWithRemainingSpace(pStack);
        if (i == -1) {
            i = this.getFreeSlot();
        }

        return i == -1 ? pStack.getCount() : this.addResource(i, pStack);
    }

    private int addResource(int pSlot, ItemStack pStack) {
        int i = pStack.getCount();
        ItemStack itemstack = this.getItem(pSlot);
        if (itemstack.isEmpty()) {
            itemstack = pStack.copyWithCount(0);
            this.setItem(pSlot, itemstack);
        }

        int j = this.getMaxStackSize(itemstack) - itemstack.getCount();
        int k = Math.min(i, j);
        if (k == 0) {
            return i;
        } else {
            i -= k;
            itemstack.grow(k);
            itemstack.setPopTime(5);
            return i;
        }
    }

    public int getSlotWithRemainingSpace(ItemStack pStack) {
        for (int i = 0; i < this.items.size(); i++) {
            if (this.hasRemainingSpaceForItem(this.items.get(i), pStack)) {
                return i;
            }
        }
        return -1;
    }

    private boolean hasRemainingSpaceForItem(ItemStack pDestination, ItemStack pOrigin) {
        return !pDestination.isEmpty()
                && ItemStack.isSameItemSameComponents(pDestination, pOrigin)
                && pDestination.isStackable()
                && pDestination.getCount() < this.getMaxStackSize(pDestination);
    }
    public int getNextNonEmptySlot(int pSlot) {
        for (int i = pSlot; i < this.items.size(); i++) {
            if (!this.items.get(i).isEmpty()) {
                return i;
            }
        }
        for(int i = 0; i < pSlot; i++){
            if (!this.items.get(i).isEmpty()) {
                return i;
            }
        }

        return -1;
    }
}
