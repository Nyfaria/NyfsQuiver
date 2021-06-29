package com.nyfaria.nyfsquiver.mixin;


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


@Mixin(PlayerEntity.class)
public class PlayerEntityMixin 
{
	@Inject(method = "getProjectile",at = @At(value="HEAD"),cancellable = true)
	private void checkQuiver(ItemStack shootable, CallbackInfoReturnable<ItemStack> cir) 
	{
		if (!(shootable.getItem() instanceof ShootableItem)) {
			return;
		}
		ItemStack stack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof ArrowItem,(PlayerEntity)(Object)this)
				.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
		if(stack.isEmpty() && shootable.getItem() instanceof CrossbowItem) {
			stack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof FireworkRocketItem,(PlayerEntity)(Object)this)
					.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
		}
		
		if (!stack.isEmpty())cir.setReturnValue(stack);
		
	}
}