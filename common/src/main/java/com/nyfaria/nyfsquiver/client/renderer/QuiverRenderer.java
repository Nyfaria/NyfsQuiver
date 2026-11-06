package com.nyfaria.nyfsquiver.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nyfaria.nyfsquiver.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

public class QuiverRenderer {
    private static final Identifier BACK_MODEL = Constants.modLoc("quiver_back");

    public static void submitOnBack(ItemStack stack, LivingEntity entity, PoseStack poseStack, SubmitNodeCollector collector, int light) {
        ItemStack worn = stack.copy();
        worn.set(DataComponents.ITEM_MODEL, BACK_MODEL);
        ItemStackRenderState renderState = new ItemStackRenderState();
        Minecraft.getInstance().getItemModelResolver().updateForLiving(renderState, worn, ItemDisplayContext.NONE, entity);
        poseStack.pushPose();
        if (entity.isCrouching()) {
            poseStack.translate(0.0F, 0.2F, 0.0F);
            poseStack.mulPose(Axis.XP.rotation(0.5F));
        }
        poseStack.translate(-0.75F, 0.35F, -0.145F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(90));
        poseStack.translate(-1.25F / 16.0F, -12.0F / 16.0F, 6.25F / 16.0F);
        poseStack.mulPose(new Quaternionf().rotationXYZ(0.0F, (float) Math.toRadians(180), (float) Math.toRadians(-45)));
        renderState.submit(poseStack, collector, light, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
