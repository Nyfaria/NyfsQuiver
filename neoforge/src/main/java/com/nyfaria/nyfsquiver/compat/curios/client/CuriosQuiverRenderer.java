package com.nyfaria.nyfsquiver.compat.curios.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nyfaria.nyfsquiver.api.QuiverType;
import com.nyfaria.nyfsquiver.client.renderer.QuiverModels;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import com.nyfaria.nyfsquiver.menu.QuiverContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

/**
 * Draws the quiver on the wearer's back while it sits in the mod's dedicated Curios slot.
 */
public class CuriosQuiverRenderer implements ICurioRenderer {

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext,
                                                                         PoseStack matrices,
                                                                         RenderLayerParent<T, M> renderLayerParent,
                                                                         MultiBufferSource buffer, int light,
                                                                         float limbSwing, float limbSwingAmount,
                                                                         float partialTicks, float ageInTicks,
                                                                         float netHeadYaw, float headPitch) {
        QuiverType type = stack.get(DataComponentInit.QUIVER_TYPE.get());
        if (type == null) {
            return;
        }
        int currentSlot = stack.getOrDefault(DataComponentInit.CURRENT_SLOT.get(), 0);
        boolean hasArrows = !new QuiverContainer(stack).getItem(currentSlot).isEmpty();
        LivingEntity wearer = slotContext.entity();

        matrices.pushPose();
        // These are the offsets this mod used for its Curios slot before the Accessories port;
        // the "back" models still carry the matching (head) display transform.
        ICurioRenderer.translateIfSneaking(matrices, wearer);
        ICurioRenderer.rotateIfSneaking(matrices, wearer);
        matrices.translate(-0.75, 0.35, -0.145);
        matrices.mulPose(Axis.ZP.rotationDegrees(90));
        Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.HEAD, false, matrices, buffer,
                light, OverlayTexture.NO_OVERLAY, QuiverModels.baked("back", type, hasArrows));
        matrices.popPose();
    }
}
