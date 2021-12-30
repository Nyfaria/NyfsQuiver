package com.nyfaria.nyfsquiver.mixin;


import cofh.core.compat.curios.CuriosProxy;
import cofh.lib.capability.CapabilityArchery;
import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.cap.QuiverHolderAttacher;
import com.nyfaria.nyfsquiver.init.TagInit;
import com.nyfaria.nyfsquiver.items.QuiverInventory;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ShootableItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import cofh.lib.util.helpers.ArcheryHelper;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Iterator;
import java.util.function.Predicate;

import static cofh.lib.capability.CapabilityArchery.AMMO_ITEM_CAPABILITY;

@Mixin(ArcheryHelper.class)
public abstract class CoFHMixin
{

	@Shadow
    public static boolean isArrow(ItemStack stack) {
		return false;
	}


    @Shadow
    public static boolean isSimpleArrow(ItemStack stack) {
        return false;
    }



    /**
     * @author
     */
    @Inject(method = "findAmmo", at = @At("HEAD"), remap = false, cancellable = true)
    private static void findAmmo(PlayerEntity shooter, ItemStack weapon, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack quiver = ItemStack.EMPTY;
        if(CuriosApi.getCuriosHelper().findEquippedCurio(NyfsQuiver.QUIVER_PREDICATE,shooter).isPresent() ) {
            ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(NyfsQuiver.QUIVER_PREDICATE,shooter).get().right;
            if (!quiverStack.isEmpty()) {
                QuiverInventory quiverInventory = QuiverItem.getInventory(quiverStack);
                quiver = quiverInventory.getStackInSlot(QuiverHolderAttacher.getQuiverHolderUnwrap(quiverStack).getCurrentSlot());
            }
            if (!quiver.isEmpty()) {
                cir.setReturnValue(quiver);
            }
        }
    }



    /**
     * @author
     */
    @Inject(method = "findArrows",at=@At("HEAD"), remap = false, cancellable = true)
    private static void findArrows(PlayerEntity shooter, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack quiver = ItemStack.EMPTY;
        if(CuriosApi.getCuriosHelper().findEquippedCurio(NyfsQuiver.QUIVER_PREDICATE,shooter).isPresent() ) {
            ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(NyfsQuiver.QUIVER_PREDICATE,shooter).get().right;
            if (!quiverStack.isEmpty()) {
                QuiverInventory quiverInventory = QuiverItem.getInventory(quiverStack);
                quiver = quiverInventory.getStackInSlot(QuiverHolderAttacher.getQuiverHolderUnwrap(quiverStack).getCurrentSlot());
            }
            if (!quiver.isEmpty()) {
                cir.setReturnValue(quiver);
            }
        }
    }
}