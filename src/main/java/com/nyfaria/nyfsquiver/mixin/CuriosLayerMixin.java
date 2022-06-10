package com.nyfaria.nyfsquiver.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nyfaria.nyfsquiver.enchantment.Meld;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.client.render.CuriosLayer;

import javax.annotation.Nonnull;

@Mixin(CuriosLayer.class)
public class CuriosLayerMixin<T extends LivingEntity, M extends EntityModel<T>> extends
        RenderLayer<T, M> {
    @Shadow
    @Final
    private RenderLayerParent<T, M> renderLayerParent;

    public CuriosLayerMixin(RenderLayerParent<T, M> p_117346_) {
        super(p_117346_);
    }

    /**
     * @author theNyfaria
     */
    @Overwrite(remap = false)
    public void render(@Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource renderTypeBuffer,
                       int light, @Nonnull T livingEntity, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        matrixStack.pushPose();
        CuriosApi.getCuriosHelper().getCuriosHandler(livingEntity)
                .ifPresent(handler -> handler.getCurios().forEach((id, stacksHandler) -> {
                    IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                    IDynamicStackHandler cosmeticStacksHandler = stacksHandler.getCosmeticStacks();

                    for (int i = 0; i < stackHandler.getSlots(); i++) {
                        ItemStack stack = cosmeticStacksHandler.getStackInSlot(i);
                        boolean cosmetic = true;

                        if (stack.isEmpty() && stacksHandler.getRenders().get(i)) {
                            stack = stackHandler.getStackInSlot(i);
                            cosmetic = false;
                        }

                        if (!stack.isEmpty() && Meld.shouldRender(stack, livingEntity)) {
                            SlotContext slotContext =
                                    new SlotContext(id, livingEntity, i, cosmetic, stacksHandler.getRenders().get(i));
                            ItemStack finalStack = stack;
                            CuriosRendererRegistry.getRenderer(stack.getItem()).ifPresent(
                                    renderer -> renderer
                                            .render(finalStack, slotContext, matrixStack, renderLayerParent,
                                                    renderTypeBuffer, light, limbSwing, limbSwingAmount, partialTicks,
                                                    ageInTicks, netHeadYaw, headPitch));
                        }
                    }
                }));
        matrixStack.popPose();

    }
}
