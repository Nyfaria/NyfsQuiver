package com.nyfaria.nyfsquiver.curios;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class ArrowsCurio implements ICurio {

    ItemStack quiverItem;

    public ArrowsCurio(ItemStack stack) {
        quiverItem = stack;
    }

//	@Override
//	public boolean canRender(String identifier, int index, LivingEntity livingEntity) {
//		return false;
//	}

    @Override
    public DropRule getDropRule(LivingEntity livingEntity) {
        return DropRule.ALWAYS_KEEP;
    }

    @Override
    public ItemStack getStack() {
        return quiverItem;
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

//	@Override
//	public void render(String identifier, int index, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
//		if (livingEntity.hasItemInSlot(EquipmentSlot.CHEST.CHEST)) {
//			return;
//		}
//
//		matrixStack.pushPose();
//
//		RenderHelper.translateIfSneaking(matrixStack, livingEntity);
//		RenderHelper.rotateIfSneaking(matrixStack, livingEntity);
//
//		//matrixStack.mulPose(Vector3f.XN.rotation((float) Math.PI));
//
//		matrixStack.translate(0.1, 0.0, 0.0);
//
//		matrixStack.scale(0.5F, 0.5F, 0.5F);
//
//
//		Minecraft.getInstance().getItemRenderer().renderStatic(quiverItem, ItemCameraTransforms.TransformType.FIXED, 0xF000F0, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer);
//
//		matrixStack.popPose();
//	}
}
