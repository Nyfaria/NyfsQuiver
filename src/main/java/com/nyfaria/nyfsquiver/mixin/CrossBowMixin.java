package com.nyfaria.nyfsquiver.mixin;


import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.ItemStack;
import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;


@Mixin(CrossbowItem.class)
public abstract class CrossBowMixin
{
	
	/**
	 * @author
	 */
	@Overwrite
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return ShootableItem.ARROW_OR_FIREWORK;
	}
}