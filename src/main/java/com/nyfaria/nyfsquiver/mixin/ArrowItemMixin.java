package com.nyfaria.nyfsquiver.mixin;

import com.nyfaria.nyfsquiver.items.QuiverItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(ArrowItem.class)
public class ArrowItemMixin {
    @Inject(method = "createArrow",at=@At("HEAD"), cancellable = true)
    public void customEntity(World pLevel, ItemStack pStack, LivingEntity pShooter, CallbackInfoReturnable<AbstractArrowEntity> cir){
        ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof QuiverItem,pShooter)
                .map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
        if(!quiverStack.isEmpty()){
            ArrowEntity arrow = new ArrowEntity(pLevel, pShooter);
            arrow.setEffectsFromItem(pStack);
            cir.setReturnValue(((QuiverItem)quiverStack.getItem()).modifyArrow(arrow));
        }
    }
}
