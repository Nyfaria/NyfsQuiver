package com.nyfaria.nyfsquiver.events;

import com.mojang.blaze3d.platform.InputConstants;
import com.nyfaria.nyfsquiver.config.NQConfig_Client;
import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.core.interfaces.QuiverContainerScreen;
import com.nyfaria.nyfsquiver.core.interfaces.QuiverRenameScreen;
import com.nyfaria.nyfsquiver.curios.QuiverRenderer;
import com.nyfaria.nyfsquiver.init.ContainerInit;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.items.QuiverContainer;
import com.nyfaria.nyfsquiver.items.QuiverModels;
import com.nyfaria.nyfsquiver.packets.PacketNextSlot;
import com.nyfaria.nyfsquiver.packets.PacketOpenQuiver;
import com.nyfaria.nyfsquiver.packets.PacketPreviousSlot;
import com.nyfaria.nyfsquiver.tooltip.ClientQuiverTooltip;
import com.nyfaria.nyfsquiver.tooltip.QuiverTooltip;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.CuriosApi;
import net.minecraftforge.client.model.ForgeModelBakery;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod.EventBusSubscriber(modid = NyfsQuiver.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
    @SubscribeEvent
    public static void onModConfigEvent(ModConfigEvent event) {
        if(event.getConfig().getSpec() == NQConfig_Client.CLIENT_SPEC) {
            NQConfig_Client.bakeConfig();
        }
    }

    private static KeyMapping NEXT_SLOT_KEY;
    private static KeyMapping PREVIOUS_SLOT_KEY;
    private static KeyMapping OPEN_QUIVER;
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent e){
        MenuScreens.register(ContainerInit.QUIVER_CONTAINER.get(), QuiverContainerScreen::new);

        NEXT_SLOT_KEY = new KeyMapping("keys.nyfsquiver.nextslot",KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,InputConstants.KEY_RBRACKET, "keys.category.nyfsquiver");
        PREVIOUS_SLOT_KEY = new KeyMapping("keys.nyfsquiver.previousslot", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,InputConstants.KEY_LBRACKET, "keys.category.nyfsquiver");
        OPEN_QUIVER = new KeyMapping("keys.nyfsquiver.openquiver", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_B, "keys.category.nyfsquiver");

        ClientRegistry.registerKeyBinding(NEXT_SLOT_KEY);
        ClientRegistry.registerKeyBinding(PREVIOUS_SLOT_KEY);
        ClientRegistry.registerKeyBinding(OPEN_QUIVER);
        MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onKey);
        MinecraftForgeClient.registerTooltipComponentFactory(QuiverTooltip.class, ClientQuiverTooltip::new);

        CuriosRendererRegistry.register(ItemInit.BASIC_QUIVER.get(), QuiverRenderer::new);
        CuriosRendererRegistry.register(ItemInit.IRON_QUIVER.get(), QuiverRenderer::new);
        CuriosRendererRegistry.register(ItemInit.COPPER_QUIVER.get(), QuiverRenderer::new);
        CuriosRendererRegistry.register(ItemInit.GOLD_QUIVER.get(), QuiverRenderer::new);
        CuriosRendererRegistry.register(ItemInit.DIAMOND_QUIVER.get(), QuiverRenderer::new);
        CuriosRendererRegistry.register(ItemInit.NETHERITE_QUIVER.get(), QuiverRenderer::new);

    }


    public static void openScreen(String defaultName, String name){
        Minecraft.getInstance().setScreen(new QuiverRenameScreen(defaultName, name));
    }

    public static void onKey(InputEvent.KeyInputEvent e){
        if(NEXT_SLOT_KEY != null && NEXT_SLOT_KEY.matches(e.getKey(), e.getScanCode()) && Minecraft.getInstance().level != null && (e.getAction() == GLFW.GLFW_PRESS && NEXT_SLOT_KEY.isConflictContextAndModifierActive()))
            NyfsQuiver.CHANNEL.sendToServer(new PacketNextSlot(1));
        if(PREVIOUS_SLOT_KEY != null && PREVIOUS_SLOT_KEY.matches(e.getKey(), e.getScanCode()) && Minecraft.getInstance().level != null && (e.getAction() == GLFW.GLFW_PRESS  && PREVIOUS_SLOT_KEY.isConflictContextAndModifierActive()))
            NyfsQuiver.CHANNEL.sendToServer(new PacketPreviousSlot(-1));
        if(OPEN_QUIVER != null && OPEN_QUIVER.matches(e.getKey(), e.getScanCode()) && Minecraft.getInstance().level != null && (e.getAction() == GLFW.GLFW_PRESS && OPEN_QUIVER.isConflictContextAndModifierActive()))
            NyfsQuiver.CHANNEL.sendToServer(new PacketOpenQuiver());
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event){
        ForgeModelBakery.addSpecialModel(QuiverModels.BASIC_QUIVER);
        ForgeModelBakery.addSpecialModel(QuiverModels.IRON_QUIVER);
        ForgeModelBakery.addSpecialModel(QuiverModels.COPPER_QUIVER);
        ForgeModelBakery.addSpecialModel(QuiverModels.GOLD_QUIVER);
        ForgeModelBakery.addSpecialModel(QuiverModels.DIAMOND_QUIVER);
        ForgeModelBakery.addSpecialModel(QuiverModels.NETHERITE_QUIVER);
        ForgeModelBakery.addSpecialModel(QuiverModels.BASIC_QUIVER_NOARROWS);
        ForgeModelBakery.addSpecialModel(QuiverModels.IRON_QUIVER_NOARROWS);
        ForgeModelBakery.addSpecialModel(QuiverModels.COPPER_QUIVER_NOARROWS);
        ForgeModelBakery.addSpecialModel(QuiverModels.GOLD_QUIVER_NOARROWS);
        ForgeModelBakery.addSpecialModel(QuiverModels.DIAMOND_QUIVER_NOARROWS);
        ForgeModelBakery.addSpecialModel(QuiverModels.NETHERITE_QUIVER_NOARROWS);
    }

    @SubscribeEvent
    public static void stitchTextures(TextureStitchEvent.Pre evt) {
        if (evt.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS)) {

            evt.addSprite(new ResourceLocation(NyfsQuiver.MOD_ID, "gui/basic_quiver"));
            evt.addSprite(new ResourceLocation(NyfsQuiver.MOD_ID, "gui/equipmentslot"));

        }
    }
}
