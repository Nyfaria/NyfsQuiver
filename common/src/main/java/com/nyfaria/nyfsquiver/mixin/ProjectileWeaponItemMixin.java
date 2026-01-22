package com.nyfaria.nyfsquiver.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.init.ArrowActionInit;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.menu.QuiverContainer;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.slot.SlotEntryReference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(ProjectileWeaponItem.class)
public class ProjectileWeaponItemMixin {
    @WrapMethod(method = "getHeldProjectile")
    private static ItemStack getHeldProjectile(LivingEntity pShooter, Predicate<ItemStack> pIsAmmo, Operation<ItemStack> original) {
        if(!(pShooter instanceof Player)){
            return original.call(pShooter, pIsAmmo);
        }
        SlotEntryReference slotReference = AccessoriesCapability.get(pShooter).getFirstEquipped(ItemInit.QUIVER.get());
        if(slotReference == null) {
            return original.call(pShooter, pIsAmmo);
        }
        ItemStack itemStack = slotReference.stack();

        if (itemStack.isEmpty()) {
            return original.call(pShooter, pIsAmmo);
        }
        QuiverContainer quiverContainer = new QuiverContainer(itemStack);
        int currentSlot = itemStack.getOrDefault(DataComponentInit.CURRENT_SLOT.get(),0);
        ItemStack stack = quiverContainer.getItem(currentSlot);
        if(stack.isEmpty()){
            int cyclingLevel = EnchantmentHelper.getItemEnchantmentLevel(pShooter.level().registryAccess().lookup(Registries.ENCHANTMENT).get().getOrThrow(Constants.CYCLING), itemStack);
            if(cyclingLevel > 0){
                int nextNonEmptySlot = quiverContainer.getNextNonEmptySlot(currentSlot);
                if(nextNonEmptySlot != -1){
                    stack = quiverContainer.getItem(nextNonEmptySlot);
                    itemStack.set(DataComponentInit.CURRENT_SLOT.get(), nextNonEmptySlot);
                }
            }
        }
        if (!stack.isEmpty() && pIsAmmo.test(stack)) {
            return stack;
        }
        return original.call(pShooter, pIsAmmo);
    }
    @Inject(method="useAmmo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;split(I)Lnet/minecraft/world/item/ItemStack;"), cancellable = true)
    private static void wrapOperation_useAmmo_removeItem(ItemStack pWeapon, ItemStack pAmmo, LivingEntity pShooter, boolean pIntangable, CallbackInfoReturnable<ItemStack> cir) {
        if(!(pShooter instanceof Player)){
            return;
        }
        SlotEntryReference slotReference = AccessoriesCapability.get(pShooter).getFirstEquipped(ItemInit.QUIVER.get());
        if(slotReference == null) {
            return;
        }
        ItemStack itemStack = slotReference.stack();
        if (!itemStack.isEmpty()) {
            QuiverContainer quiverContainer = new QuiverContainer(itemStack);
            int currentSlot = itemStack.getOrDefault(DataComponentInit.CURRENT_SLOT.get(),0);
            ItemStack stack = quiverContainer.getItem(currentSlot);
            if(stack.isEmpty()){
                return;
            }
            ItemStack stack2 = stack.split(1);
            if (!stack.isEmpty() && stack.getCount() == 1) {
                quiverContainer.setItem(currentSlot, ItemStack.EMPTY);
            } else if (!stack.isEmpty()) {
                quiverContainer.setItem(currentSlot, stack);
            }
            pWeapon.set(DataComponentInit.ARROW_ACTION.get(), itemStack.getOrDefault(DataComponentInit.ARROW_ACTION.get(), ArrowActionInit.VANILLA.get()));
            cir.setReturnValue(stack2);
        }
    }
    @WrapOperation(method="createProjectile", at= @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ArrowItem;createArrow(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/projectile/AbstractArrow;"))
    public AbstractArrow wrapOperation_createProjectile_createArrow(ArrowItem instance, Level pLevel, ItemStack pAmmo, LivingEntity pShooter, ItemStack pWeapon, Operation<AbstractArrow> original) {
        return pWeapon.getOrDefault(DataComponentInit.ARROW_ACTION.get(), ArrowActionInit.VANILLA.get()).createArrow(original.call(instance, pLevel, pAmmo, pShooter, pWeapon), pLevel, pAmmo, pShooter, pWeapon);
    }

}
