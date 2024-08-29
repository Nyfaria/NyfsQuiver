package com.nyfaria.nyfsquiver.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.menu.QuiverContainer;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.slot.SlotEntryReference;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Inventory.class)
public class InventoryMixin {

    @Shadow @Final public Player player;

    @WrapMethod(method = "add(Lnet/minecraft/world/item/ItemStack;)Z")
    public boolean addInventoryItem(ItemStack pStack, Operation<Boolean> original) {
        if(pStack.isEmpty())
            return false;
        SlotEntryReference slotReference = AccessoriesCapability.get(player).getFirstEquipped(ItemInit.QUIVER.get());
        if(slotReference == null) {
            return original.call(pStack);
        }
        ItemStack itemStack = slotReference.stack();
        if (itemStack.isEmpty()) {
            return original.call(pStack);
        }
        QuiverContainer quiverContainer = new QuiverContainer(itemStack);
        ItemStack stack = quiverContainer.add(-1,pStack);
        if(stack.isEmpty()) {
            return true;
        }
        return original.call(stack);
    }

}
