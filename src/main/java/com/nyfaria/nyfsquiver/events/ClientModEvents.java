package com.nyfaria.nyfsquiver.events;

import com.mojang.blaze3d.platform.InputConstants;
import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.core.interfaces.QuiverContainerScreen;
import com.nyfaria.nyfsquiver.core.interfaces.QuiverRenameScreen;
import com.nyfaria.nyfsquiver.curios.QuiverRenderer;
import com.nyfaria.nyfsquiver.init.ContainerInit;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.items.QuiverModels;
import com.nyfaria.nyfsquiver.packets.PacketNextSlot;
import com.nyfaria.nyfsquiver.packets.PacketOpenQuiver;
import com.nyfaria.nyfsquiver.packets.PacketPreviousSlot;
import com.nyfaria.nyfsquiver.tooltip.ClientQuiverTooltip;
import com.nyfaria.nyfsquiver.tooltip.QuiverTooltip;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod.EventBusSubscriber(modid = NyfsQuiver.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
    private static KeyMapping NEXT_SLOT_KEY;
    private static KeyMapping PREVIOUS_SLOT_KEY;
    private static KeyMapping OPEN_QUIVER;


    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent e) {
        MenuScreens.register(ContainerInit.QUIVER_CONTAINER.get(), QuiverContainerScreen::new);
    }

    @SubscribeEvent
    public static void OnKeyBindEvent(RegisterKeyMappingsEvent event) {
        NEXT_SLOT_KEY = new KeyMapping("keys.nyfsquiver.nextslot", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_RBRACKET, "keys.category.nyfsquiver");
        PREVIOUS_SLOT_KEY = new KeyMapping("keys.nyfsquiver.previousslot", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_LBRACKET, "keys.category.nyfsquiver");
        OPEN_QUIVER = new KeyMapping("keys.nyfsquiver.openquiver", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_B, "keys.category.nyfsquiver");

        event.register(NEXT_SLOT_KEY);
        event.register(PREVIOUS_SLOT_KEY);
        event.register(OPEN_QUIVER);
        MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onKey);
    }

    @SubscribeEvent
    public static void onTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(QuiverTooltip.class, ClientQuiverTooltip::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent event) {

        CuriosRendererRegistry.register(ItemInit.BASIC_QUIVER.get(), QuiverRenderer::new);
        CuriosRendererRegistry.register(ItemInit.IRON_QUIVER.get(), QuiverRenderer::new);
        CuriosRendererRegistry.register(ItemInit.COPPER_QUIVER.get(), QuiverRenderer::new);
        CuriosRendererRegistry.register(ItemInit.GOLD_QUIVER.get(), QuiverRenderer::new);
        CuriosRendererRegistry.register(ItemInit.DIAMOND_QUIVER.get(), QuiverRenderer::new);
        CuriosRendererRegistry.register(ItemInit.NETHERITE_QUIVER.get(), QuiverRenderer::new);

    }


    public static void openScreen(String defaultName, String name) {
        Minecraft.getInstance().setScreen(new QuiverRenameScreen(defaultName, name));
    }

    public static void onKey(InputEvent.Key e) {
        if (NEXT_SLOT_KEY != null && NEXT_SLOT_KEY.matches(e.getKey(), e.getScanCode()) && Minecraft.getInstance().level != null && (e.getAction() == GLFW.GLFW_PRESS && NEXT_SLOT_KEY.isConflictContextAndModifierActive()))
            NyfsQuiver.CHANNEL.sendToServer(new PacketNextSlot(1));
        if (PREVIOUS_SLOT_KEY != null && PREVIOUS_SLOT_KEY.matches(e.getKey(), e.getScanCode()) && Minecraft.getInstance().level != null && (e.getAction() == GLFW.GLFW_PRESS && PREVIOUS_SLOT_KEY.isConflictContextAndModifierActive()))
            NyfsQuiver.CHANNEL.sendToServer(new PacketPreviousSlot(-1));
        if (OPEN_QUIVER != null && OPEN_QUIVER.matches(e.getKey(), e.getScanCode()) && Minecraft.getInstance().level != null && (e.getAction() == GLFW.GLFW_PRESS && OPEN_QUIVER.isConflictContextAndModifierActive()))
            NyfsQuiver.CHANNEL.sendToServer(new PacketOpenQuiver());
    }

    @SubscribeEvent
    public static void onModelRegister(ModelEvent.RegisterAdditional event) {
        event.register(QuiverModels.BASIC_QUIVER);
        event.register(QuiverModels.IRON_QUIVER);
        event.register(QuiverModels.COPPER_QUIVER);
        event.register(QuiverModels.GOLD_QUIVER);
        event.register(QuiverModels.DIAMOND_QUIVER);
        event.register(QuiverModels.NETHERITE_QUIVER);
        event.register(QuiverModels.BASIC_QUIVER_NOARROWS);
        event.register(QuiverModels.IRON_QUIVER_NOARROWS);
        event.register(QuiverModels.COPPER_QUIVER_NOARROWS);
        event.register(QuiverModels.GOLD_QUIVER_NOARROWS);
        event.register(QuiverModels.DIAMOND_QUIVER_NOARROWS);
        event.register(QuiverModels.NETHERITE_QUIVER_NOARROWS);
    }
}
