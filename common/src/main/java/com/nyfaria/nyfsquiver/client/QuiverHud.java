package com.nyfaria.nyfsquiver.client;

import com.google.common.collect.Lists;
import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.config.NQConfigClient;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.init.TagInit;
import com.nyfaria.nyfsquiver.menu.QuiverContainer;
import com.nyfaria.nyfsquiver.platform.Services;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;

import java.util.List;

public class QuiverHud {
    public static float interpolation = 0;
    public static ItemStack lastHeld = null;
    public static List<ItemStack> lastReadyArrows = null;
    private static final Identifier HOTBAR_OFFHAND_LEFT_SPRITE = Constants.modLoc("textures/gui/slot.png");
    public void extract(GuiGraphicsExtractor guiGraphics, @Nullable DeltaTracker deltaTracker) {
        if (Minecraft.getInstance().player.getMainHandItem().getItem() instanceof ProjectileWeaponItem) {
            @Nullable LocalPlayer player = Minecraft.getInstance().player;
            float scale = (float) NQConfigClient.getGUIScale();
            Matrix3x2fStack poseStack = guiGraphics.pose();
            ItemStack quiverStack = Services.PLATFORM.getEquippedQuiver(player);
            if (quiverStack.isEmpty()) return;
            int slot = quiverStack.getOrDefault(DataComponentInit.CURRENT_SLOT.get(), 0);
            ItemStack playerHand;
            if (player.getMainHandItem().getItem() instanceof ProjectileWeaponItem && !quiverStack.isEmpty()) {
                playerHand = player.getMainHandItem();
                lastHeld = playerHand;

                if (interpolation < 1.0f && !Minecraft.getInstance().isPaused() && NQConfigClient.hides()) {
                    interpolation += 0.01f;
                }
            } else if (player.getOffhandItem().getItem() instanceof ProjectileWeaponItem) {
                playerHand = player.getOffhandItem();
                lastHeld = playerHand;

                if (interpolation < 1.0f && !Minecraft.getInstance().isPaused() && NQConfigClient.hides()) {
                    interpolation += 0.01f;
                }
            } else {
                if (!NQConfigClient.animates() && NQConfigClient.hides()) {
                    return;
                }

                if (interpolation > 0 && !Minecraft.getInstance().isPaused() && NQConfigClient.hides()) {
                    interpolation -= 0.01f;
                }

                if (NQConfigClient.hides()) {
                    if (lastHeld == null) {
                        return;
                    } else {
                        playerHand = lastHeld;
                    }
                } else {
                    playerHand = lastHeld = new ItemStack(Items.BOW);
                }
            }

            if (!NQConfigClient.hides()) {
                interpolation = 1.0f;
            }

            float left = (float) NQConfigClient.getAnchor().getX() + NQConfigClient.getHorizontalOffset() * (1f / scale);
            float top = (float) NQConfigClient.getAnchor().getY() + NQConfigClient.getVerticalOffset() * (1f / scale);

            poseStack.pushMatrix();
            poseStack.scale(scale, scale);
            poseStack.translate(left, NQConfigClient.animates() ? bezier(interpolation, -top, top) : top);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_LEFT_SPRITE, -12, -12, 0, 0, 22, 22, 22, 22);
            poseStack.popMatrix();

            List<ItemStack> readyArrows;
            if (player.getMainHandItem().getItem() instanceof ProjectileWeaponItem || player.getOffhandItem().getItem() instanceof ProjectileWeaponItem || !NQConfigClient.hides()) {
                readyArrows = List.of(new QuiverContainer(quiverStack).getItem(slot));
                lastReadyArrows = readyArrows;
            } else {
                readyArrows = lastReadyArrows;
            }
            List<Integer> skips = Lists.newLinkedList();
            int xMultiplier = 0;


            if (readyArrows != null) {

                if (readyArrows.size() == 0) {
                    float x = 24 * xMultiplier + left, y = NQConfigClient.animates() ? bezier(interpolation, -top, top) : top;
                    poseStack.pushMatrix();
                    poseStack.scale(scale, scale);
                    poseStack.translate(0, 1);
                    //guiGraphics.drawString(poseStack, Minecraft.getInstance().font, Component.literal("0"), Math.round(3 + left), Math.round(NQConfigClient.animates() ? bezier(interpolation, -top, top) : top), 16733525);
                    guiGraphics.centeredText(Minecraft.getInstance().font, Component.literal("0"), Math.round(x + 3), Math.round(y + 1), 0xFF000000 | 16733525);
                    poseStack.scale(0.49f, 0.49f);
                    guiGraphics.centeredText(Minecraft.getInstance().font, Component.literal(String.valueOf(slot + 1)), Math.round(((x - 8) / 49) * 100), Math.round(((y - 10) / 49) * 100) - 2, 0xFF000000 | 16777215);

                    poseStack.popMatrix();

                } else {
                    for (int i = 0; i < readyArrows.size(); ++i) {
                        if (skips.contains(i)) {
                            continue;
                        }

                        float x = 24 * xMultiplier + left, y = NQConfigClient.animates() ? bezier(interpolation, -top, top) : top;
                        ItemStack readyArrow = readyArrows.get(i);

                        poseStack.pushMatrix();
                        poseStack.scale(scale, scale);
//                            poseStack.translate(0, 0, -160);
//                        poseStack.scale(16, 16, 1);
//                        poseStack.mulPose(Axis.YP.rotationDegrees(180));
//                        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                        guiGraphics.item(readyArrow, (int) x - 9, (int) y - 8);
                        guiGraphics.itemDecorations(Minecraft.getInstance().font, readyArrow, (int) x - 9, (int) y - 8);
//                        renderItem(guiGraphics, i, readyArrow);
                        poseStack.popMatrix();
                        if (EnchantmentHelper.getItemEnchantmentLevel(Minecraft.getInstance().level.registryAccess().lookup(Registries.ENCHANTMENT).get().getOrThrow(Enchantments.MULTISHOT), playerHand) > 0) {
                            poseStack.pushMatrix();
                            poseStack.scale(scale, scale);
                            if (readyArrow.getItem() == Items.FIREWORK_ROCKET) {
                                poseStack.translate(x - 5, y + 3);
                            } else {
                                poseStack.translate(x - 4, y - 1);
                            }
//                            poseStack.scale(10, 10, 1);
                            poseStack.scale(0.625f, 0.625f);
//                            poseStack.mulPose(Axis.YP.rotationDegrees(180));
//                            poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                            if (readyArrow.getItem() == Items.FIREWORK_ROCKET) {
                                poseStack.rotate((float) Math.toRadians(-20));
                            } else {
                                poseStack.rotate((float) Math.toRadians(-30));
                            }
                            guiGraphics.item(readyArrow, -8, -8);
                            poseStack.popMatrix();

                            poseStack.pushMatrix();
                            poseStack.scale(scale, scale);
                            if (readyArrow.getItem() == Items.FIREWORK_ROCKET) {
                                poseStack.translate(x + 5, y + 3);
                            } else {
                                poseStack.translate(x + 1, y + 4);
                            }
                            poseStack.scale(0.625f, 0.625f);
//                            poseStack.mulPose(Axis.YP.rotationDegrees(180));
//                            poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                            if (readyArrow.getItem() == Items.FIREWORK_ROCKET) {
                                poseStack.rotate((float) Math.toRadians(20));
                            } else {
                                poseStack.rotate((float) Math.toRadians(30));
                            }
//                            renderItem(guiGraphics, i, readyArrow);
                            guiGraphics.item(readyArrow, -8, -8);
                            poseStack.popMatrix();
                        }

                        int count = readyArrow.getCount();

                        for (int j = i + 1; j < readyArrows.size(); ++j) {
                            ItemStack nextArrow = readyArrows.get(j);
                            if (nextArrow.equals(readyArrow) && ItemStack.isSameItemSameComponents(nextArrow, readyArrow)) {
                                count += nextArrow.getCount();
                                skips.add(j);
                            } else {
                                break;
                            }
                        }

                        poseStack.pushMatrix();
                        poseStack.scale(scale, scale);
                        if (player.isCreative() || readyArrow.is(TagInit.QUIVER_ITEMS) && EnchantmentHelper.getItemEnchantmentLevel(Minecraft.getInstance().level.registryAccess().lookup(Registries.ENCHANTMENT).get().getOrThrow(Enchantments.INFINITY), playerHand) > 0 && !(readyArrow.getItem() instanceof FireworkRocketItem) && !(readyArrow.getItem() instanceof SpectralArrowItem)) {
                            poseStack.translate(x + 3, y + 5);
//                                guiGraphics.blit(HOTBAR_OFFHAND_LEFT_SPRITE, -6, -4, 24, i == 0 ? 0 : 8, 12, 8, 36, 24);
                            poseStack.translate(-(x + 3), -(y + 5));
                            poseStack.scale(0.49f, 0.49f);
                            guiGraphics.centeredText(Minecraft.getInstance().font, Component.literal(String.valueOf(slot + 1)), Math.round(((x - 8) / 49) * 100), Math.round(((y - 10) / 49) * 100), 0xFF000000 | 16777215);
                        } else {
                            boolean using = player.getUseItemRemainingTicks() > 0 && readyArrow == player.getProjectile(playerHand) && player.getUseItem().getItem() instanceof ProjectileWeaponItem;
                            String displayCount = using ? String.valueOf(count - 1) : String.valueOf(count);
                            int color = i == 0 ? (using ? (count - 1 == 0 ? 16733525 /*red*/ : 16777045 /*yellow*/) : 16777215 /*white*/) : 10066329 /*gray*/;
                            poseStack.translate(0, 0);
                            guiGraphics.centeredText(Minecraft.getInstance().font, Component.literal(displayCount), Math.round(x + 3), Math.round(y + 1), 0xFF000000 | color);
                            poseStack.scale(0.49f, 0.49f);
                            guiGraphics.centeredText(Minecraft.getInstance().font, Component.literal(String.valueOf(slot + 1)), Math.round(((x - 8) / 49) * 100), Math.round(((y - 10) / 49) * 100), 0xFF000000 | 16777215);

                        }
                        poseStack.popMatrix();

                        ++xMultiplier;
                    }
                }
            }
        }
    }
    public static float bezier(float x, float min, float max) {
        return Mth.clamp(((x * x) * (3 - 2 * x)) / (1 / (max - min)) + min, min, max);
    }
}
