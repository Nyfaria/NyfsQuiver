package com.nyfaria.nyfsquiver.curios;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nyfaria.nyfsquiver.enchantment.Meld;
import com.nyfaria.nyfsquiver.items.QuiverModels;
import com.nyfaria.nyfsquiver.items.QuiverStorageManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class QuiverRenderer implements ICurioRenderer {
    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!Meld.shouldRender(stack, slotContext.entity())) {
            return;
        }
        matrixStack.pushPose();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        LivingEntity living = slotContext.entity();
        ICurioRenderer.translateIfSneaking(matrixStack, living);
        ICurioRenderer.rotateIfSneaking(matrixStack, living);
        //matrixStack.mulPose(Vector3f.XN.rotationDegrees(180));
        matrixStack.translate(-.75, 0.35, -0.145);
        matrixStack.mulPose(Axis.ZP.rotationDegrees(90));
        //matrixStack.translate(0,0.6,-0.65 - (living.getItemBySlot(EquipmentSlot.CHEST).isEmpty() ? 0.0 : 0.05));
        ItemStack arrowsE = QuiverStorageManager.getCurrentSlotStack(stack);
        BakedModel quiver = itemRenderer.getItemModelShaper().getModelManager().getModel(QuiverModels.getQuiverModel(stack, !arrowsE.isEmpty()));
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        itemRenderer.render(stack, ItemDisplayContext.HEAD, true, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY, quiver);
        matrixStack.popPose();

    }
}
