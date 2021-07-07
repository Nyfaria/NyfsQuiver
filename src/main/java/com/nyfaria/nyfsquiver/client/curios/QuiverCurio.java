package com.nyfaria.nyfsquiver.client.curios;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.common.items.QuiverItem;
import com.nyfaria.nyfsquiver.common.items.QuiverStorageManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

public class QuiverCurio implements ICurio {

	ItemStack quiverItem;

	public QuiverCurio(ItemStack stack) {
		quiverItem = stack;
	}

	@Override
	public boolean canRightClickEquip() {
		return false;
	} 

	@Override
	public boolean canRender(String identifier, int index, LivingEntity livingEntity) {
		return true;
	}

	  @Override
	  public void render(String identifier, int index, MatrixStack matrixStack,
	                      IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity,
	                      float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
	                      float netHeadYaw, float headPitch) {
		  /*
		  float translate = -0.16f;
			if (livingEntity.hasItemInSlot(EquipmentSlotType.CHEST)) {
				translate = -0.22f;
			}
			
			matrixStack.pushPose();
			
			RenderHelper.translateIfSneaking(matrixStack, livingEntity);
			RenderHelper.rotateIfSneaking(matrixStack, livingEntity);
			
			matrixStack.mulPose(Vector3f.XN.rotation((float) Math.PI));
			
			matrixStack.translate(0, -0.3, translate);
			
			matrixStack.scale(1F, 1F, 1F);
			
			Minecraft.getInstance().getItemRenderer().renderStatic(quiverItem, ItemCameraTransforms.TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer);
			
			matrixStack.popPose();*/
			
			
			
			  float translate = 0f;
			  if (livingEntity.hasItemInSlot(EquipmentSlotType.CHEST)) {
				  translate = 0.06f;
			  }
				
				matrixStack.pushPose();
				
				RenderHelper.translateIfSneaking(matrixStack, livingEntity);
				RenderHelper.rotateIfSneaking(matrixStack, livingEntity);
				
				//matrixStack.mulPose(Vector3f.XN.rotation((float) Math.PI));
				
				//matrixStack.mulPose( Vector3f.XN.rotation((0.5f) *(float) Math.PI));
				
				matrixStack.translate(0, -0.1, translate);
				
				//matrixStack.scale(1F, 1F, 1F);
				Minecraft.getInstance().getItemRenderer().renderStatic(quiverItem, ItemCameraTransforms.TransformType.HEAD, light, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer);
				
				matrixStack.popPose();

	  }
	
	
	@Override
	public void onEquip(SlotContext slotContext, ItemStack newStack) {
		if (quiverItem.getOrCreateTag().contains("nyfsquiver:invIndex")
				&& quiverItem.getOrCreateTag().contains("nyfsquiver:slotIndex")) {

		} else {
			quiverItem.getOrCreateTag().putBoolean("equipped", true);
			quiverItem.getOrCreateTag().putInt("nyfsquiver:slotIndex", 0);
			quiverItem.getOrCreateTag().putInt("nyfsquiver:invIndex",
					QuiverStorageManager.createInventoryIndex(((QuiverItem) quiverItem.getItem()).type));
		}
		ItemStack stack = QuiverStorageManager.getInventory(quiverItem.getOrCreateTag().getInt("nyfsquiver:invIndex"))
				.getStackInSlot(quiverItem.getOrCreateTag().getInt("nyfsquiver:slotIndex"));
		ItemStack checkStack = CuriosApi.getCuriosHelper()
				.findEquippedCurio(NyfsQuiver.arrow_predicate, slotContext.getWearer())
				.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right)
				.orElse(ItemStack.EMPTY);
		if (checkStack == ItemStack.EMPTY) {
			CuriosApi.getCuriosHelper().getCuriosHandler(slotContext.getWearer()).map(ICuriosItemHandler::getCurios)
					.map(stringICurioStacksHandlerMap -> stringICurioStacksHandlerMap.get("arrows"))
					.map(ICurioStacksHandler::getStacks)
					.ifPresent(curioStackHandler -> curioStackHandler.setStackInSlot(0, stack));
			QuiverStorageManager.getInventory(quiverItem.getOrCreateTag().getInt("nyfsquiver:invIndex"))
					.setStackInSlot(quiverItem.getOrCreateTag().getInt("nyfsquiver:slotIndex"), ItemStack.EMPTY);
		}
	}

	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack) {
		quiverItem.getOrCreateTag().putBoolean("equipped", false);
		System.out.println("Quiver UnEquipped");
		ItemStack stack = CuriosApi.getCuriosHelper()
				.findEquippedCurio(item -> item.getItem() instanceof ArrowItem, slotContext.getWearer())
				.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right)
				.orElse(ItemStack.EMPTY);
		if(stack.isEmpty()) {
			stack = CuriosApi.getCuriosHelper()
			.findEquippedCurio(item -> item.getItem() instanceof FireworkRocketItem, slotContext.getWearer())
			.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right)
			.orElse(ItemStack.EMPTY);
		}
		QuiverStorageManager.getInventory(quiverItem.getOrCreateTag().getInt("nyfsquiver:invIndex"))
				.setStackInSlot(quiverItem.getOrCreateTag().getInt("nyfsquiver:slotIndex"), stack);
		CuriosApi.getCuriosHelper().getCuriosHandler(slotContext.getWearer()).map(ICuriosItemHandler::getCurios)
				.map(stringICurioStacksHandlerMap -> stringICurioStacksHandlerMap.get("arrows"))
				.map(ICurioStacksHandler::getStacks)
				.ifPresent(curioStackHandler -> curioStackHandler.setStackInSlot(0, ItemStack.EMPTY));

	}
}
