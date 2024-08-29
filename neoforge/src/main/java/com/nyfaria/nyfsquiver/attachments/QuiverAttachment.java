package com.nyfaria.nyfsquiver.attachments;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class QuiverAttachment implements INBTSerializable<CompoundTag> {

    private ItemStack stack = ItemStack.EMPTY;

    public QuiverAttachment(IAttachmentHolder iAttachmentHolder) {
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        if (!stack.isEmpty()) {
            tag.put("stack", stack.save(provider));
        }
        return null;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        stack = ItemStack.parse(provider, nbt.getCompound("stack")).orElse(ItemStack.EMPTY);
    }
}
