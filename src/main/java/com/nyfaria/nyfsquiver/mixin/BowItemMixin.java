package com.nyfaria.nyfsquiver.mixin;

import com.nyfaria.nyfsquiver.items.QuiverItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(ArrowItem.class)
public class BowItemMixin {


    @Inject(method = "createArrow",at=@At("HEAD"), cancellable = true)
    public void customEntity(Level p_40513_, ItemStack p_40514_, LivingEntity p_40515_, CallbackInfoReturnable<AbstractArrow> cir){
        ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof QuiverItem,p_40515_)
                .map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
        if(!quiverStack.isEmpty()){
            Arrow arrow = new Arrow(p_40513_, p_40515_);
            arrow.setEffectsFromItem(p_40514_);
            cir.setReturnValue(((QuiverItem)quiverStack.getItem()).modifyArrow(arrow));
        }
    }


}
