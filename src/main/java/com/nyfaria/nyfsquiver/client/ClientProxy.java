package com.nyfaria.nyfsquiver.client;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.network.NetworkHooks;

import org.lwjgl.glfw.GLFW;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.common.CommonProxy;
import com.nyfaria.nyfsquiver.common.items.QuiverItem;
import com.nyfaria.nyfsquiver.common.items.QuiverType;
import com.nyfaria.nyfsquiver.core.init.ContainerTypeInit;
import com.nyfaria.nyfsquiver.core.interfaces.QuiverContainerScreen;
import com.nyfaria.nyfsquiver.core.interfaces.QuiverRenameScreen;
import com.nyfaria.nyfsquiver.packets.PacketNextSlot;
import com.nyfaria.nyfsquiver.packets.PacketPreviousSlot;

import net.java.games.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings.Type;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Created 2/7/2020 by SuperMartijn642
 */
public class ClientProxy extends CommonProxy {

    private static KeyBinding NEXT_SLOT_KEY;
    private static KeyBinding PREVIOUS_SLOT_KEY;
    @Override
    public void init(){
        ScreenManager.register(ContainerTypeInit.container, QuiverContainerScreen::new);

        NEXT_SLOT_KEY = new KeyBinding("keys.nyfsquiver.nextslot", 82/*'['*/, "keys.category.nyfsquiver");
        PREVIOUS_SLOT_KEY = new KeyBinding("keys.nyfsquiver.previousslot", 81/*'['*/, "keys.category.nyfsquiver");
        ClientRegistry.registerKeyBinding(NEXT_SLOT_KEY);
        ClientRegistry.registerKeyBinding(PREVIOUS_SLOT_KEY);
        MinecraftForge.EVENT_BUS.addListener(ClientProxy::onKey);
    }

    @Override
    public PlayerEntity getClientPlayer(){
        return Minecraft.getInstance().player;
    }

    public static void openScreen(String defaultName, String name){
        Minecraft.getInstance().setScreen(new QuiverRenameScreen(defaultName, name));
    }

    public static void onKey(InputEvent.KeyInputEvent e){
        if(NEXT_SLOT_KEY != null && NEXT_SLOT_KEY.matches(e.getKey(), e.getScanCode()) && Minecraft.getInstance().level != null && (e.getAction() == GLFW.GLFW_PRESS))
            NyfsQuiver.CHANNEL.sendToServer(new PacketNextSlot(1));
        if(PREVIOUS_SLOT_KEY != null && PREVIOUS_SLOT_KEY.matches(e.getKey(), e.getScanCode()) && Minecraft.getInstance().level != null && (e.getAction() == GLFW.GLFW_PRESS))
            NyfsQuiver.CHANNEL.sendToServer(new PacketPreviousSlot(-1));

    }

    public static ITextComponent getKeyBindCharacter(){
        return NEXT_SLOT_KEY == null || NEXT_SLOT_KEY.getKey().getValue() == -1 ? null : new TranslationTextComponent(NEXT_SLOT_KEY.getKey().getName());
    }
}