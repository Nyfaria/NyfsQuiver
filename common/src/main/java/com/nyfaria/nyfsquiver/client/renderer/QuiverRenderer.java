package com.nyfaria.nyfsquiver.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.api.QuiverType;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import io.wispforest.accessories.api.client.AccessoryRenderer;
import io.wispforest.accessories.api.client.Side;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class QuiverRenderer implements AccessoryRenderer {
    private static Map<ResourceLocation, ModelResourceLocation> models = new HashMap<>();

    @Override
    public <M extends LivingEntity> void render(ItemStack stack, SlotReference reference, PoseStack matrices, EntityModel<M> model, MultiBufferSource multiBufferSource, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrices.pushPose();
        String slot = reference.slotName().split("_")[1];
        QuiverType type = stack.get(DataComponentInit.QUIVER_TYPE.get());
        ModelResourceLocation modelLocation = models.computeIfAbsent(
                type.name().withSuffix("_"+slot), (rl) -> new ModelResourceLocation(
                        Constants.loc(rl.getNamespace(), "quiver/" + slot + "/" + type.name().getPath()),
                        "standalone"
                )
        );

        BakedModel themodel = Minecraft.getInstance().getModelManager().getModel(modelLocation);
        if(slot.equals("back")){
            AccessoryRenderer.transformToFace(matrices, ((HumanoidModel) model).body, Side.BACK);
            matrices.translate(0, 1.5, 0.4);
            matrices.scale(-1, 1, -1);
            matrices.scale(1.5f, 1.5f, 1.5f);
        } else {
            AccessoryRenderer.transformToFace(matrices, ((HumanoidModel) model).body, Side.RIGHT);
            matrices.translate(0.6, 1.7, 0);
            matrices.scale(-1, 1, -1);
            matrices.scale(1.5f, 1.5f, 1.5f);
        }
        Minecraft.getInstance().getItemRenderer().render(stack,  ItemDisplayContext.HEAD,false, matrices, multiBufferSource, light, OverlayTexture.NO_OVERLAY, themodel);
        matrices.popPose();
    }
}
