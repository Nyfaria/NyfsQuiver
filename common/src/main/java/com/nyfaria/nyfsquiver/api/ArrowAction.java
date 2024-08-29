package com.nyfaria.nyfsquiver.api;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;


@FunctionalInterface
public interface ArrowAction {
    AbstractArrow createArrow(AbstractArrow entity, Level pLevel, ItemStack pAmmo, LivingEntity pShooter, @Nullable ItemStack pWeapon);
}
