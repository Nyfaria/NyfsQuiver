package com.nyfaria.nyfsquiver.events;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.core.interfaces.QuiverContainerScreen;
import com.nyfaria.nyfsquiver.core.interfaces.QuiverRenameScreen;
import com.nyfaria.nyfsquiver.init.ContainerInit;
import com.nyfaria.nyfsquiver.items.QuiverModels;
import com.nyfaria.nyfsquiver.packets.PacketNextSlot;
import com.nyfaria.nyfsquiver.packets.PacketOpenQuiver;
import com.nyfaria.nyfsquiver.packets.PacketPreviousSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.common.CuriosRegistry;

@Mod.EventBusSubscriber(modid = NyfsQuiver.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {

    private static KeyBinding NEXT_SLOT_KEY;
    private static KeyBinding PREVIOUS_SLOT_KEY;
    private static KeyBinding OPEN_QUIVER;

    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent e){
        ScreenManager.register(ContainerInit.QUIVER_CONTAINER.get(), QuiverContainerScreen::new);

        NEXT_SLOT_KEY = new KeyBinding("keys.nyfsquiver.nextslot", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM,GLFW.GLFW_KEY_RIGHT_BRACKET, "keys.category.nyfsquiver");
        PREVIOUS_SLOT_KEY = new KeyBinding("keys.nyfsquiver.previousslot", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM,GLFW.GLFW_KEY_LEFT_BRACKET, "keys.category.nyfsquiver");
        OPEN_QUIVER = new KeyBinding("keys.nyfsquiver.openquiver", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_B, "keys.category.nyfsquiver");
        ClientRegistry.registerKeyBinding(NEXT_SLOT_KEY);
        ClientRegistry.registerKeyBinding(PREVIOUS_SLOT_KEY);
        ClientRegistry.registerKeyBinding(OPEN_QUIVER);
        MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onKey);

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

    public static ITextComponent getKeyBindCharacter(){
        return NEXT_SLOT_KEY == null || NEXT_SLOT_KEY.getKey().getValue() == -1 ? null : new TranslationTextComponent(NEXT_SLOT_KEY.getKey().getName());
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event){

        ModelLoader.addSpecialModel(QuiverModels.BASIC_QUIVER);
        ModelLoader.addSpecialModel(QuiverModels.IRON_QUIVER);
        ModelLoader.addSpecialModel(QuiverModels.GOLD_QUIVER);
        ModelLoader.addSpecialModel(QuiverModels.DIAMOND_QUIVER);
        ModelLoader.addSpecialModel(QuiverModels.NETHERITE_QUIVER);
        ModelLoader.addSpecialModel(QuiverModels.BASIC_QUIVER_NOARROWS);
        ModelLoader.addSpecialModel(QuiverModels.IRON_QUIVER_NOARROWS);
        ModelLoader.addSpecialModel(QuiverModels.GOLD_QUIVER_NOARROWS);
        ModelLoader.addSpecialModel(QuiverModels.DIAMOND_QUIVER_NOARROWS);
        ModelLoader.addSpecialModel(QuiverModels.NETHERITE_QUIVER_NOARROWS);
    }

    @SubscribeEvent
    public static void stitchTextures(TextureStitchEvent.Pre evt) {
        if (evt.getMap().location().equals(PlayerContainer.BLOCK_ATLAS)) {

            evt.addSprite(new ResourceLocation(NyfsQuiver.MOD_ID, "gui/basic_quiver"));
            evt.addSprite(new ResourceLocation(NyfsQuiver.MOD_ID, "gui/equipmentslot"));

        }
    }
}
