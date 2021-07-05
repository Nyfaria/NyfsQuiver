package com.nyfaria.nyfsquiver.client.curios;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class ArrowsCurio implements ICurio {

	ItemStack quiverItem;

	public ArrowsCurio(ItemStack stack) {
		quiverItem = stack;
	}

	
	@Override
	public boolean canRender(String identifier, int index, LivingEntity livingEntity) {
		return false;
	}
	
	@Override
	public DropRule getDropRule(LivingEntity livingEntity) {
	    return DropRule.ALWAYS_KEEP;
	}
	
	@Override
	public boolean canEquip(String identifier, LivingEntity livingEntity) {
		return false;
	}

	@Override
	public boolean canUnequip(String identifier, LivingEntity livingEntity) {
		return false;
	}

	@Override
	public boolean canRightClickEquip() {
		return false;
	}
	@Override
	public void render(String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (livingEntity.hasItemInSlot(EquipmentSlotType.CHEST)) {
			return;
		}
		
		matrixStack.pushPose();
		
		RenderHelper.translateIfSneaking(matrixStack, livingEntity);
		RenderHelper.rotateIfSneaking(matrixStack, livingEntity);
		
		//matrixStack.mulPose(Vector3f.XN.rotation((float) Math.PI));
		
		matrixStack.translate(0.1, 0.0, 0.0);
		
		matrixStack.scale(0.5F, 0.5F, 0.5F);
		
		
		Minecraft.getInstance().getItemRenderer().renderStatic(quiverItem, ItemCameraTransforms.TransformType.FIXED, 0xF000F0, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer);
		
		matrixStack.popPose();
	}
}
