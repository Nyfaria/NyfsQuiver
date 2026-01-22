package com.nyfaria.nyfsquiver;

import com.nyfaria.nyfsquiver.client.ClientUtil;
import com.nyfaria.nyfsquiver.client.QuiverHud;
import com.nyfaria.nyfsquiver.client.screen.QuiverScreen;
import com.nyfaria.nyfsquiver.client.tooltip.ClientQuiverTooltip;
import com.nyfaria.nyfsquiver.config.NQConfigClient;
import com.nyfaria.nyfsquiver.init.KeyBindInit;
import com.nyfaria.nyfsquiver.init.MenuInit;
import com.nyfaria.nyfsquiver.item.tooltip.QuiverTooltip;
import com.nyfaria.nyfsquiver.network.PacketInit;
import fuzs.forgeconfigapiport.fabric.impl.core.ForgeConfigRegistryImpl;
import fuzs.forgeconfigapiport.fabric.impl.core.NeoForgeConfigRegistryImpl;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.gui.screens.MenuScreens;
import net.neoforged.fml.config.ModConfig;

public class NyfsQuiverClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NeoForgeConfigRegistryImpl.INSTANCE.register(Constants.MODID,ModConfig.Type.CLIENT, NQConfigClient.CLIENT_SPEC);
        PacketInit.loadClass();
        ClientUtil.renderRegistration();
        MenuScreens.register(MenuInit.QUIVER_MENU.get(), QuiverScreen::new);
        TooltipComponentCallback.EVENT.register((context) -> {
            if(context instanceof QuiverTooltip tooltip)
                return new ClientQuiverTooltip(tooltip);
            return null;
        });
        HudRenderCallback.EVENT.register(new QuiverHud()::render);
        KeyBindingHelper.registerKeyBinding(KeyBindInit.OPEN_SCREEN);
        KeyBindingHelper.registerKeyBinding(KeyBindInit.NEXT_SLOT);
        KeyBindingHelper.registerKeyBinding(KeyBindInit.PREV_SLOT);
    }
}
