package com.nyfaria.nyfsquiver.mixin;


import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.cap.QuiverHolderAttacher;
import com.nyfaria.nyfsquiver.init.TagInit;
import com.nyfaria.nyfsquiver.items.QuiverInventory;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.CrossbowItem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Predicate;


@Mixin(PlayerEntity.class)
public class PlayerEntityMixin 
{
	@Inject(method = "getProjectile",at = @At(value="HEAD"),cancellable = true)
	private void checkQuiver(ItemStack shootable, CallbackInfoReturnable<ItemStack> cir) 
	{
		if (!(shootable.getItem() instanceof ShootableItem)) {
			return;
		}
		Predicate<ItemStack> predicate = ((ShootableItem)shootable.getItem()).getSupportedHeldProjectiles();
		ItemStack itemStack;
		if(!CuriosApi.getCuriosHelper().findEquippedCurio(NyfsQuiver.QUIVER_PREDICATE,(PlayerEntity)(Object)this).isPresent())
			return;

		ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(NyfsQuiver.QUIVER_PREDICATE,(PlayerEntity)(Object)this).get().right;
		if(quiverStack.isEmpty()){
			return;
		}

		QuiverInventory quiverInventory = QuiverItem.getInventory(quiverStack);
		itemStack = quiverInventory.getStackInSlot(QuiverHolderAttacher.getQuiverHolderUnwrap(quiverStack).getCurrentSlot());


		if (predicate.test(itemStack)) {
			if(((PlayerEntity)(Object)this).level.isClientSide()){
				cir.setReturnValue(ItemStack.EMPTY);
			}else {
				cir.setReturnValue(itemStack);
			}
		}
		
	}
}