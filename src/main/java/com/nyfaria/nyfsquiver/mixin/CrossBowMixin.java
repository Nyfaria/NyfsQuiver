package com.nyfaria.nyfsquiver.mixin;

import com.nyfaria.nyfsquiver.cap.QuiverHolderAttacher;
import com.nyfaria.nyfsquiver.init.EnchantmentInit;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Predicate;


@Mixin(CrossbowItem.class)
public abstract class CrossBowMixin {

    @Shadow
    private static void addChargedProjectile(ItemStack p_40929_, ItemStack p_40930_) {
    }

    /**
     * @author Nyfaria
     */
    @Overwrite
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ProjectileWeaponItem.ARROW_OR_FIREWORK;
    }

    @Inject(method = "loadProjectile", at=@At(value = "HEAD"), cancellable = true)
    private static void split(LivingEntity p_40863_, ItemStack p_40864_, ItemStack p_40865_, boolean p_40866_, boolean p_40867_, CallbackInfoReturnable<Boolean> cir) {
        ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof QuiverItem, p_40863_)
                .map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
        if (!quiverStack.isEmpty()) {
            if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.QUINFINITY.get(),quiverStack)>0) {
                addChargedProjectile(p_40864_, p_40865_.copy());
                cir.setReturnValue(true);
            }
        }
    }

}