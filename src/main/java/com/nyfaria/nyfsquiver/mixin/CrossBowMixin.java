package com.nyfaria.nyfsquiver.mixin;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.config.NQConfig;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Predicate;


@Mixin(CrossbowItem.class)
public abstract class CrossBowMixin {

    /**
     * @author
     */
    @Overwrite
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ProjectileWeaponItem.ARROW_OR_FIREWORK;
    }

}