package com.nyfaria.nyfsquiver.mixin;

import java.util.function.Predicate;

import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
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
		return ProjectileWeaponItem.ARROW_OR_FIREWORK;
	}
}