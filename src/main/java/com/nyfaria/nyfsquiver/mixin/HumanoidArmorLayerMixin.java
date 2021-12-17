package com.nyfaria.nyfsquiver.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nyfaria.nyfsquiver.init.EnchantmentInit;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {


    @Inject(method = "renderArmorPiece", at=@At("HEAD"), cancellable = true)
    private void melding(PoseStack poseStack, MultiBufferSource bufferSource, LivingEntity livingEntity, EquipmentSlot equipmentSlot, int flag1, HumanoidModel<LivingEntity> model, CallbackInfo ci){
        ItemStack itemstack = livingEntity.getItemBySlot(equipmentSlot);
        if(EnchantmentHelper.getEnchantments(itemstack).containsKey(EnchantmentInit.MELD_ENCHANTMENT.get()) && livingEntity.isInvisible()){
            ci.cancel();
        }
    }

}
