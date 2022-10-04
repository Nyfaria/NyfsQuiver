package com.nyfaria.nyfsquiver.events;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.cap.QuiverHolderAttacher;
import com.nyfaria.nyfsquiver.config.NQConfig_Client;
import com.nyfaria.nyfsquiver.init.TagInit;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = NyfsQuiver.MODID, value = Dist.CLIENT)
public class ClientForgeEvents {

    static int offsetX = 0;
    static int offsetY = 0;

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        @Nullable LocalPlayer player = Minecraft.getInstance().player;
        float scale = (float)NQConfig_Client.getGUIScale();
        PoseStack poseStack = event.getMatrixStack();
        ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof QuiverItem, player)
                .map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
        if (quiverStack.isEmpty()) return;
        if (event.getType() == RenderGameOverlayEvent.ElementType.LAYER && player != null) {
            int slot = QuiverHolderAttacher.getQuiverHolderUnwrap(quiverStack).getCurrentSlot();
            ItemStack playerHand;
            if (player.getMainHandItem().getItem() instanceof ProjectileWeaponItem && !quiverStack.isEmpty()) {
                playerHand = player.getMainHandItem();
                NyfsQuiver.lastHeld = playerHand;

                if (NyfsQuiver.interpolation < 1.0f && !Minecraft.getInstance().isPaused() && NQConfig_Client.hides()) {
                    NyfsQuiver.interpolation += 0.01f;
                }
            } else if (player.getOffhandItem().getItem() instanceof ProjectileWeaponItem) {
                playerHand = player.getOffhandItem();
                NyfsQuiver.lastHeld = playerHand;

                if (NyfsQuiver.interpolation < 1.0f && !Minecraft.getInstance().isPaused() && NQConfig_Client.hides()) {
                    NyfsQuiver.interpolation += 0.01f;
                }
            } else {
                if (!NQConfig_Client.animates() && NQConfig_Client.hides()) {
                    return;
                }

                if (NyfsQuiver.interpolation > 0 && !Minecraft.getInstance().isPaused() && NQConfig_Client.hides()) {
                    NyfsQuiver.interpolation -= 0.01f;
                }

                if (NQConfig_Client.hides()) {
                    if (NyfsQuiver.lastHeld == null) {
            return;
                    } else {
            playerHand = NyfsQuiver.lastHeld;
                    }
                } else {
                    playerHand = NyfsQuiver.lastHeld = new ItemStack(Items.BOW);
                }
            }

            if (!NQConfig_Client.hides()) {
                NyfsQuiver.interpolation = 1.0f;
            }

            float left = (float)NQConfig_Client.getAnchor().getX() + NQConfig_Client.getHorizontalOffset() * (1f/scale);
            float top = (float)NQConfig_Client.getAnchor().getY() + NQConfig_Client.getVerticalOffset() * (1f/scale);

            poseStack.pushPose();
            poseStack.scale(scale,scale,scale);
            poseStack.translate(left, NQConfig_Client.animates() ? NyfsQuiver.bezier(NyfsQuiver.interpolation, -top, top) : top, 0);
            RenderSystem.setShaderTexture(0, NyfsQuiver.WIDGETS);
            GuiComponent.blit(poseStack, -12, -12, 0, 0, 24, 24, 36, 24);
            poseStack.popPose();

            List<ItemStack> readyArrows;
            if (player.getMainHandItem().getItem() instanceof ProjectileWeaponItem || player.getOffhandItem().getItem() instanceof ProjectileWeaponItem || !NQConfig_Client.hides()) {
                readyArrows = NyfsQuiver.findAmmos(player, playerHand);
                NyfsQuiver.lastReadyArrows = readyArrows;
            } else {
                readyArrows = NyfsQuiver.lastReadyArrows;
            }
            List<Integer> skips = Lists.newLinkedList();
            int xMultiplier = 0;


            if (readyArrows != null) {

                if (readyArrows.size() == 0) {
                    float x = 24 * xMultiplier + left, y = NQConfig_Client.animates() ? NyfsQuiver.bezier(NyfsQuiver.interpolation, -top, top) : top;
                    poseStack.pushPose();
            poseStack.scale(scale,scale,scale);
                    poseStack.translate(0, 1, 1);
                    //GuiComponent.drawString(poseStack, Minecraft.getInstance().font, new TextComponent("0"), Math.round(3 + left), Math.round(NQConfig_Client.animates() ? NyfsQuiver.bezier(NyfsQuiver.interpolation, -top, top) : top), 16733525);
                    GuiComponent.drawCenteredString(poseStack, Minecraft.getInstance().font, new TextComponent("0"), Math.round(x + 3), Math.round(y + 1), 16733525);
                    poseStack.scale(0.49f, 0.49f, 0);
                    GuiComponent.drawCenteredString(poseStack, Minecraft.getInstance().font, new TextComponent(String.valueOf(slot + 1)), Math.round(((x - 8) / 49) * 100), Math.round(((y - 10) / 49) * 100) - 2, 16777215);

                    poseStack.popPose();

                } else {
                    for (int i = 0; i < readyArrows.size(); ++i) {
            if (skips.contains(i)) {
                continue;
            }

            float x = 24 * xMultiplier + left, y = NQConfig_Client.animates() ? NyfsQuiver.bezier(NyfsQuiver.interpolation, -top, top) : top;
            ItemStack readyArrow = readyArrows.get(i);

            poseStack.pushPose();
            poseStack.scale(scale,scale,scale);
            poseStack.translate(x, y, i + 1);
            poseStack.scale(16, 16, 1);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(180));
            renderItem(poseStack, i, readyArrow);
            poseStack.popPose();
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, playerHand) > 0) {
                poseStack.pushPose();
            poseStack.scale(scale,scale,scale);
                if (readyArrow.getItem() == Items.FIREWORK_ROCKET) {
                    poseStack.translate(x - 5, y + 3, i + 1);
                } else {
                    poseStack.translate(x - 4, y - 1, i + 1);
                }
                poseStack.scale(10, 10, 1);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(180));
                if (readyArrow.getItem() == Items.FIREWORK_ROCKET) {
                    poseStack.mulPose(Vector3f.ZN.rotationDegrees(-20));
                } else {
                    poseStack.mulPose(Vector3f.ZN.rotationDegrees(-30));
                }
                renderItem(poseStack, i, readyArrow);
                poseStack.popPose();

                poseStack.pushPose();
            poseStack.scale(scale,scale,scale);
                if (readyArrow.getItem() == Items.FIREWORK_ROCKET) {
                    poseStack.translate(x + 5, y + 3, i + 1);
                } else {
                    poseStack.translate(x + 1, y + 4, i + 1);
                }
                poseStack.scale(10, 10, 1);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(180));
                if (readyArrow.getItem() == Items.FIREWORK_ROCKET) {
                    poseStack.mulPose(Vector3f.ZN.rotationDegrees(20));
                } else {
                    poseStack.mulPose(Vector3f.ZN.rotationDegrees(30));
                }
                renderItem(poseStack, i, readyArrow);
                poseStack.popPose();
            }

            int count = readyArrow.getCount();

            for (int j = i + 1; j < readyArrows.size(); ++j) {
                ItemStack nextArrow = readyArrows.get(j);
                if (nextArrow.sameItem(readyArrow) && ItemStack.tagMatches(nextArrow, readyArrow)) {
                    count += nextArrow.getCount();
                    skips.add(j);
                } else {
                    break;
                }
            }

            poseStack.pushPose();
            poseStack.scale(scale,scale,scale);
            if (player.isCreative() || readyArrow.is(TagInit.QUIVER_ITEMS) && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, playerHand) > 0 && !(readyArrow.getItem() instanceof FireworkRocketItem) && !(readyArrow.getItem() instanceof SpectralArrowItem)) {
                poseStack.translate(x + 3, y + 5, i + 1 + readyArrows.size());
                RenderSystem.setShaderTexture(0, NyfsQuiver.WIDGETS);
                GuiComponent.blit(poseStack, -6, -4, 24, i == 0 ? 0 : 8, 12, 8, 36, 24);
                poseStack.translate(-(x + 3), -(y + 5), -(i + 1 + readyArrows.size()));
                poseStack.scale(0.49f, 0.49f, 0);
                GuiComponent.drawCenteredString(poseStack, Minecraft.getInstance().font, new TextComponent(String.valueOf(slot + 1)), Math.round(((x - 8) / 49) * 100), Math.round(((y - 10) / 49) * 100), 16777215);
            } else {
                boolean using = player.getUseItemRemainingTicks() > 0 && readyArrow == player.getProjectile(playerHand) && player.getUseItem().getItem() instanceof ProjectileWeaponItem;
                String displayCount = using ? String.valueOf(count - 1) : String.valueOf(count);
                int color = i == 0 ? (using ? (count - 1 == 0 ? 16733525 /*red*/ : 16777045 /*yellow*/) : 16777215 /*white*/) : 10066329 /*gray*/;
                poseStack.translate(0, 0, i + 1 + readyArrows.size());
                GuiComponent.drawCenteredString(poseStack, Minecraft.getInstance().font, new TextComponent(displayCount), Math.round(x + 3), Math.round(y + 1), color);
                poseStack.scale(0.49f, 0.49f, 0);
                GuiComponent.drawCenteredString(poseStack, Minecraft.getInstance().font, new TextComponent(String.valueOf(slot + 1)), Math.round(((x - 8) / 49) * 100), Math.round(((y - 10) / 49) * 100), 16777215);

            }
            poseStack.popPose();

            ++xMultiplier;
                    }
                }
            }
        }
    }

    private static void renderItem(PoseStack poseStack, int i, ItemStack readyArrow) {
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.applyModelViewMatrix();
        Lighting.setupForFlatItems();
        Minecraft.getInstance().getItemRenderer().renderStatic(readyArrow, ItemTransforms.TransformType.GUI, i == 0 ? 15728880 : 14540253, OverlayTexture.NO_OVERLAY, poseStack, buffer, 0);
        buffer.endBatch();
        RenderSystem.enableCull();
        RenderSystem.disableDepthTest();
    }

//
//    @SubscribeEvent
//    public static void onRenderToolTip(RenderTooltipEvent.Color event) {
//        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
//            offsetX = event.getX();
//            offsetY = event.getY() + (event.getComponents().size() * 14);
//            if (event.getItemStack().getItem() instanceof QuiverItem) {
//                PoseStack poseStack = event.getPoseStack();
//                ItemStack itemStack = event.getItemStack();//((AbstractContainerScreen) event.getScreen()).getSlotUnderMouse().getItem();
//                if (!itemStack.getOrCreateTag().contains("invIndex")) {
//                    return;
//                }
//                QuiverType type = ((QuiverItem) itemStack.getItem()).type;
//                List<ItemStack> items = QuiverStorageManager.getInventory(itemStack.getOrCreateTag().getInt("invIndex")).getStacks();
//                int slot = 0;
//                poseStack.pushPose();
//                for (int rows = 0; rows < type.getRows(); rows++) {
//                    for (int cols = 0; cols < type.getColumns(); cols++) {
//            int x = (cols * 13) + offsetX;
//            int y = (rows * 13) + offsetY;
//            RenderSystem.setShaderTexture(0,new ResourceLocation("textures/gui/container/hopper.png"));
//            poseStack.translate(x, y, 20);
//            poseStack.scale(12, 12, 1);
//            renderItem(poseStack, 0, items.get(slot));
//
//            GuiComponent.blit(poseStack, x , y  - 1, 43, 19, 18, 18,0,0,0);
//            poseStack.scale((1f / 12f), (1f / 12f), 1);
//            poseStack.translate(-x, -y, -20);
//            //Minecraft.getInstance().getItemRenderer().renderGuiItemDecorations(font,items.get(slot), cols * 18 + offsetX, rows * 18+ offsetY);
//            slot++;
//                    }
//                }
//                poseStack.popPose();
//            }
//        }
//    }

    @SubscribeEvent
    public static void post(ScreenEvent.DrawScreenEvent.Post event) {
//        if (Minecraft.getInstance().level == null) return;
//        if (event.getScreen() instanceof AbstractContainerScreen) {
//            if (((AbstractContainerScreen) event.getScreen()).getSlotUnderMouse() != null) {
//                if (((AbstractContainerScreen) event.getScreen()).getSlotUnderMouse().getItem().getItem() instanceof QuiverItem) {
//                    PoseStack poseStack = event.getPoseStack();
//                    ItemStack itemStack = ((AbstractContainerScreen) event.getScreen()).getSlotUnderMouse().getItem();
//                    if(!itemStack.getOrCreateTag().contains("invIndex")){
//            return;
//                    }
//                    QuiverType type = ((QuiverItem) itemStack.getItem()).type;
//                    Font font = Minecraft.getInstance().font;
//                    List<ItemStack> items = QuiverStorageManager.getInventory(itemStack.getOrCreateTag().getInt("invIndex")).getStacks();
//                    int slot = 0;
//                    poseStack.pushPose();
//                    for (int rows = 0; rows < type.getRows(); rows++) {
//            for (int cols = 0; cols < type.getColumns(); cols++) {
//                int x = (cols * 18) + offsetX;
//                int y = (rows * 18) + offsetY;
//                poseStack.translate(x,y,20);
//                poseStack.scale(16,16,1);
//                renderItem(poseStack,0,items.get(slot));
//                poseStack.scale((1f/16f),(1f/16f),1);
//                poseStack.translate(-x,-y,-20);
//                //Minecraft.getInstance().getItemRenderer().renderGuiItemDecorations(font,items.get(slot), cols * 18 + offsetX, rows * 18+ offsetY);
//                slot++;
//            }
//                    }
//                    poseStack.popPose();
//                }
//            }
//        }
    }

}
