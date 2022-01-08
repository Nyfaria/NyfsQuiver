package com.nyfaria.nyfsquiver.curios;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.enchantment.MeldEnchantment;
import com.nyfaria.nyfsquiver.init.EnchantmentInit;
import com.nyfaria.nyfsquiver.items.QuiverModels;
import com.nyfaria.nyfsquiver.items.QuiverStorageManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class QuiverRenderer implements ICurioRenderer {
    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if(!MeldEnchantment.shouldRender(stack,slotContext.entity())){
			return;
		}
		matrixStack.pushPose();
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		LivingEntity living = slotContext.entity();
        ICurioRenderer.translateIfSneaking(matrixStack, living);
		ICurioRenderer.rotateIfSneaking(matrixStack, living);
		//matrixStack.mulPose(Vector3f.XN.rotationDegrees(180));
		matrixStack.translate(-.75,0.35,-0.145);
		matrixStack.mulPose(Vector3f.ZP.rotationDegrees(90));
		//matrixStack.translate(0,0.6,-0.65 - (living.getItemBySlot(EquipmentSlot.CHEST).isEmpty() ? 0.0 : 0.05));
		ItemStack arrowsE = QuiverStorageManager.getCurrentSlotStack(stack);
		BakedModel quiver = itemRenderer.getItemModelShaper().getModelManager().getModel(QuiverModels.getQuiverModel(stack,!arrowsE.isEmpty()));
		MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
		itemRenderer.render(stack, ItemTransforms.TransformType.HEAD,true,matrixStack,buffer,light,light,quiver);
		matrixStack.popPose();

    }
}
