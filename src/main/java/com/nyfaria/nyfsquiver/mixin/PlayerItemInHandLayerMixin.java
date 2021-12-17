package com.nyfaria.nyfsquiver.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nyfaria.nyfsquiver.init.EnchantmentInit;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerItemInHandLayer.class)
public class PlayerItemInHandLayerMixin {

    @Inject(method = "renderArmWithItem", at=@At("HEAD"), cancellable = true)
    private void melding(LivingEntity livingEntity, ItemStack itemstack, ItemTransforms.TransformType p_174527_, HumanoidArm p_174528_, PoseStack p_174529_, MultiBufferSource p_174530_, int p_174531_, CallbackInfo ci){
        if(EnchantmentHelper.getEnchantments(itemstack).containsKey(EnchantmentInit.MELD_ENCHANTMENT.get()) && livingEntity.isInvisible()){
            ci.cancel();
        }
    }
}
