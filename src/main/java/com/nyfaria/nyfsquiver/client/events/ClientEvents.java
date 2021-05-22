package com.nyfaria.nyfsquiver.client.events;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nyfaria.nyfsquiver.NQConfig_Client;
import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.common.items.QuiverItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import top.theillusivec4.curios.api.CuriosApi;

@EventBusSubscriber(modid = NyfsQuiver.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
	
    @SuppressWarnings("resource")
	@SubscribeEvent
    public static void onRenderGameOverlay(final RenderGameOverlayEvent event) {
        @Nullable ClientPlayerEntity player = Minecraft.getInstance().player;
		ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof QuiverItem,player)
				.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL && player != null) {
			int slot = quiverStack.getOrCreateTag().getInt("nyfsquiver:slotIndex");
            ItemStack playerHand;
            if(player.getMainHandItem().getItem() instanceof ShootableItem && !quiverStack.isEmpty()) {
                playerHand = player.getMainHandItem();
                NyfsQuiver.lastHeld = playerHand;

                if(NyfsQuiver.interpolation < 1.0f && !Minecraft.getInstance().isPaused() && NQConfig_Client.hides()) {
                    NyfsQuiver.interpolation += 0.01f;
                }
            } else if(player.getOffhandItem().getItem() instanceof ShootableItem) {
                playerHand = player.getOffhandItem();
                NyfsQuiver.lastHeld = playerHand;

                if(NyfsQuiver.interpolation < 1.0f && !Minecraft.getInstance().isPaused() && NQConfig_Client.hides()) {
                	NyfsQuiver.interpolation += 0.01f;
                }
            } else {
                if(!NQConfig_Client.animates() && NQConfig_Client.hides()) {
                    return;
                }
                
                if(NyfsQuiver.interpolation > 0 && !Minecraft.getInstance().isPaused() && NQConfig_Client.hides()) {
                	NyfsQuiver.interpolation -= 0.01f;
                }

                if(NQConfig_Client.hides()) {
                    if(NyfsQuiver.lastHeld == null) {
                        return;
                    } else {
                        playerHand = NyfsQuiver.lastHeld;
                    }
                } else {
                    playerHand = NyfsQuiver.lastHeld = new ItemStack(Items.BOW);
                }
            }

            if(!NQConfig_Client.hides()) {
            	NyfsQuiver.interpolation = 1.0f;
            }

            float left = NQConfig_Client.getHorizontalOffset();
            float top = NQConfig_Client.getVerticalOffset();

            event.getMatrixStack().pushPose();
            event.getMatrixStack().translate(left, NQConfig_Client.animates() ? NyfsQuiver.bezier(NyfsQuiver.interpolation, -top, top) : top, 0);
            Minecraft.getInstance().getTextureManager().bind(NyfsQuiver.WIDGETS);
            AbstractGui.blit(event.getMatrixStack(), -12, -12, 0, 0, 24, 24, 36, 24);
            event.getMatrixStack().popPose();

            List<ItemStack> readyArrows;
            if(player.getMainHandItem().getItem() instanceof ShootableItem || player.getOffhandItem().getItem() instanceof ShootableItem || !NQConfig_Client.hides()) {
                readyArrows = NyfsQuiver.findAmmos(player, playerHand);
                NyfsQuiver.lastReadyArrows = readyArrows;
            } else {
                readyArrows = NyfsQuiver.lastReadyArrows;
            }
            List<Integer> skips = Lists.newLinkedList();
            int xMultiplier = 0;

            if(readyArrows != null) {
                
                if(readyArrows.size() == 0) {
                    event.getMatrixStack().pushPose();
                    event.getMatrixStack().translate(0, 1, 1);
                    AbstractGui.drawString(event.getMatrixStack(), Minecraft.getInstance().font, new StringTextComponent("0"), Math.round(3 + left), Math.round(NQConfig_Client.animates() ? NyfsQuiver.bezier(NyfsQuiver.interpolation, -top, top) : top), 16733525);
                    event.getMatrixStack().scale(0.49f, 0.49f, 0);
                    AbstractGui.drawCenteredString(event.getMatrixStack(), Minecraft.getInstance().font, new StringTextComponent(String.valueOf(slot + 1)), Math.round(left), Math.round(NQConfig_Client.animates() ? NyfsQuiver.bezier(NyfsQuiver.interpolation, -top-6, top-6) : top-6), 16777215);
                    
                    event.getMatrixStack().popPose();
              
                } else {
                    for(int i = 0; i < readyArrows.size(); ++ i) {
                        if(skips.contains(i)) {
                            continue;
                        }

                        ItemStack readyArrow = readyArrows.get(i);
                        float x = 24 * xMultiplier + left, y = NQConfig_Client.animates() ? NyfsQuiver.bezier(NyfsQuiver.interpolation, -top, top) : top;

                        event.getMatrixStack().pushPose();
                        event.getMatrixStack().translate(x, y, i + 1);
                        event.getMatrixStack().scale(16, -16, 1);
                        event.getMatrixStack().mulPose(Vector3f.YP.rotationDegrees(180));
                        event.getMatrixStack().mulPose(Vector3f.XP.rotationDegrees(360));
                        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().renderBuffers().bufferSource();
                        RenderSystem.enableDepthTest();
                        RenderSystem.disableCull();
                        Minecraft.getInstance().getItemRenderer().renderStatic(readyArrow, ItemCameraTransforms.TransformType.FIXED, i == 0 ? 15728880 : 14540253, OverlayTexture.NO_OVERLAY, event.getMatrixStack(), buffer);
                        buffer.endBatch();
                        event.getMatrixStack().popPose();
                        RenderSystem.enableCull();
                        RenderSystem.disableDepthTest();
                        if(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, playerHand) > 0) {
                            event.getMatrixStack().pushPose();
                            if(readyArrow.getItem() == Items.FIREWORK_ROCKET) {
                                event.getMatrixStack().translate(x - 5, y + 3, i + 1);
                            } else {
                                event.getMatrixStack().translate(x - 4, y - 1, i + 1);
                            }
                            event.getMatrixStack().scale(10, -10, 1);
                            event.getMatrixStack().mulPose(Vector3f.YP.rotationDegrees(180));
                            event.getMatrixStack().mulPose(Vector3f.XP.rotationDegrees(360));
                            if(readyArrow.getItem() == Items.FIREWORK_ROCKET) {
                                event.getMatrixStack().mulPose(Vector3f.ZP.rotationDegrees(-20));
                            } else {
                                event.getMatrixStack().mulPose(Vector3f.ZP.rotationDegrees(-30));
                            }                                IRenderTypeBuffer.Impl buffer2 = Minecraft.getInstance().renderBuffers().bufferSource();
                            RenderSystem.enableDepthTest();
                            RenderSystem.disableCull();
                            Minecraft.getInstance().getItemRenderer().renderStatic(readyArrow, ItemCameraTransforms.TransformType.FIXED, i == 0 ? 15728880 : 14540253, OverlayTexture.NO_OVERLAY, event.getMatrixStack(), buffer);
                            buffer2.endBatch();
                            event.getMatrixStack().popPose();
                            RenderSystem.enableCull();
                            RenderSystem.disableDepthTest();

                            event.getMatrixStack().pushPose();
                            if(readyArrow.getItem() == Items.FIREWORK_ROCKET) {
                                event.getMatrixStack().translate(x + 5, y + 3, i + 1);
                            } else {
                                event.getMatrixStack().translate(x + 1, y + 4, i + 1);
                            }
                            event.getMatrixStack().scale(10, -10, 1);
                            event.getMatrixStack().mulPose(Vector3f.YP.rotationDegrees(180));
                            event.getMatrixStack().mulPose(Vector3f.XP.rotationDegrees(360));
                            if(readyArrow.getItem() == Items.FIREWORK_ROCKET) {
                                event.getMatrixStack().mulPose(Vector3f.ZP.rotationDegrees(20));
                            } else {
                                event.getMatrixStack().mulPose(Vector3f.ZP.rotationDegrees(30));
                            }
                            IRenderTypeBuffer.Impl buffer3 = Minecraft.getInstance().renderBuffers().bufferSource();
                            RenderSystem.enableDepthTest();
                            RenderSystem.disableCull();
                            Minecraft.getInstance().getItemRenderer().renderStatic(readyArrow, ItemCameraTransforms.TransformType.FIXED, i == 0 ? 15728880 : 14540253, OverlayTexture.NO_OVERLAY, event.getMatrixStack(), buffer);
                            buffer3.endBatch();
                            event.getMatrixStack().popPose();
                            RenderSystem.enableCull();
                            RenderSystem.disableDepthTest();
                        }

                        int count = readyArrow.getCount();

                        for(int j = i + 1; j < readyArrows.size(); ++ j) {
                            ItemStack nextArrow = readyArrows.get(j);
                            if(nextArrow.sameItem(readyArrow) && ItemStack.tagMatches(nextArrow, readyArrow)) {
                                count += nextArrow.getCount();
                                skips.add(j);
                            } else {
                                break;
                            }
                        }

                        event.getMatrixStack().pushPose();
                        if(player.isCreative() || readyArrow.getItem() instanceof ArrowItem && ((ArrowItem) readyArrow.getItem()).isInfinite(readyArrow, playerHand, player)) {
                            event.getMatrixStack().translate(x + 3, y + 5, i + 1 + readyArrows.size());
                            Minecraft.getInstance().getTextureManager().bind(NyfsQuiver.WIDGETS);
                            AbstractGui.blit(event.getMatrixStack(), -6, -4, 24, i == 0 ? 0 : 8, 12, 8, 36, 24);
                        } else {
                            boolean using = player.getUseItemRemainingTicks() > 0 && readyArrow == player.getProjectile(playerHand) && player.getUseItem().getItem() instanceof ShootableItem;
                            String displayCount = using ? String.valueOf(count - 1) : String.valueOf(count);
                            int length = displayCount.length();
                            int color = i == 0 ? (using ? (count - 1 == 0 ? 16733525 /*red*/ : 16777045 /*yellow*/) : 16777215 /*white*/) : 10066329 /*gray*/;
                            event.getMatrixStack().translate(0, 0, i + 1 + readyArrows.size());
                            AbstractGui.drawString(event.getMatrixStack(), Minecraft.getInstance().font, new StringTextComponent(displayCount), Math.round(x + 9 - (6 * length)), Math.round(y + 1), color);
                            event.getMatrixStack().scale(0.49f, 0.49f, 0);
                            AbstractGui.drawCenteredString(event.getMatrixStack(), Minecraft.getInstance().font, new StringTextComponent(String.valueOf(slot + 1)),Math.round(x), Math.round(y - 4), 16777215);
                           
                        } event.getMatrixStack().popPose();

                        ++ xMultiplier;
                    }
                }
            }
        }
    }

	
    
	
}
