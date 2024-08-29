package com.nyfaria.nyfsquiver.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.menu.QuiverContainer;
import io.wispforest.accessories.api.AccessoriesCapability;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public class PlayerMixin {


//    @WrapMethod(method = "getProjectile")
//    public ItemStack getProjectile(ItemStack pShootable, Operation<ItemStack> original) {
//        ItemStack itemStack = AccessoriesCapability.get((Player) (Object) this).getFirstEquipped(ItemInit.QUIVER.get()).stack();
//        if (itemStack.isEmpty()) {
//            return original.call(pShootable);
//        }
//        QuiverContainer quiverContainer = new QuiverContainer(itemStack);
//        int currentSlot = itemStack.getOrDefault(DataComponentInit.CURRENT_SLOT.get(),0);
//        ItemStack stack = quiverContainer.getItem(currentSlot);
//        if (!stack.isEmpty()) {
//            return stack;
//        }
//        return original.call(pShootable);
//    }
}
