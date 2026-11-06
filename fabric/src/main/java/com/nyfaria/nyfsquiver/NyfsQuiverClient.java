package com.nyfaria.nyfsquiver;

import com.nyfaria.nyfsquiver.client.QuiverHud;
import com.nyfaria.nyfsquiver.client.TrinketQuiverRenderer;
import com.nyfaria.nyfsquiver.client.model.HasArrowsProperty;
import com.nyfaria.nyfsquiver.client.model.QuiverTypeProperty;
import com.nyfaria.nyfsquiver.client.screen.QuiverScreen;
import com.nyfaria.nyfsquiver.client.tooltip.ClientQuiverTooltip;
import com.nyfaria.nyfsquiver.config.NQConfigClient;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.init.KeyBindInit;
import com.nyfaria.nyfsquiver.init.MenuInit;
import com.nyfaria.nyfsquiver.item.tooltip.QuiverTooltip;
import com.nyfaria.nyfsquiver.network.PacketInit;
import eu.pb4.trinkets.api.client.TrinketRendererRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.neoforged.fml.config.ModConfig;

public class NyfsQuiverClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ConfigRegistry.INSTANCE.register(Constants.MODID, ModConfig.Type.CLIENT, NQConfigClient.CLIENT_SPEC);
        PacketInit.loadClass();
        SelectItemModelProperties.ID_MAPPER.put(Constants.modLoc("quiver_type"), QuiverTypeProperty.TYPE);
        ConditionalItemModelProperties.ID_MAPPER.put(Constants.modLoc("has_arrows"), HasArrowsProperty.MAP_CODEC);
        TrinketRendererRegistry.registerRenderer(ItemInit.QUIVER.get(), new TrinketQuiverRenderer());
        MenuScreens.register(MenuInit.QUIVER_MENU.get(), QuiverScreen::new);
        ClientTooltipComponentCallback.EVENT.register(component -> component instanceof QuiverTooltip tooltip ? new ClientQuiverTooltip(tooltip) : null);
        HudElementRegistry.addLast(Constants.modLoc("ability_overlay"), new QuiverHud()::extract);
        KeyMapping.Category.register(KeyBindInit.CATEGORY.id());
        KeyMappingHelper.registerKeyMapping(KeyBindInit.OPEN_SCREEN);
        KeyMappingHelper.registerKeyMapping(KeyBindInit.NEXT_SLOT);
        KeyMappingHelper.registerKeyMapping(KeyBindInit.PREV_SLOT);
    }
}
