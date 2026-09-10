package com.nyfaria.nyfsquiver.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.init.TagInit;
import com.nyfaria.nyfsquiver.menu.QuiverContainer;
import com.nyfaria.nyfsquiver.util.QuiverEquipment;
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
        if(!pStack.is(TagInit.QUIVER_ITEMS))
            return original.call(pStack);
        if(pStack.isEmpty())
            return false;
        ItemStack itemStack = QuiverEquipment.getEquippedQuiver(player);
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
