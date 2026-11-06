package com.nyfaria.nyfsquiver.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nyfaria.nyfsquiver.client.renderer.QuiverRenderer;
import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class TrinketQuiverRenderer implements TrinketRenderer {

    @Override
    public void submit(ItemStack stack, TrinketSlotAccess slotReference, EntityModel<? extends LivingEntityRenderState> contextModel, PoseStack poseStack, SubmitNodeCollector submit, int light, LivingEntityRenderState state, float limbAngle, float limbDistance) {
        LivingEntity entity = slotReference.inventory().getAttachment().getEntity();
        if (entity != null) {
            QuiverRenderer.submitOnBack(stack, entity, poseStack, submit, light);
        }
    }
}
