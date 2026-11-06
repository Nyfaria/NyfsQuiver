package com.nyfaria.nyfsquiver.events;

import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.client.CurioQuiverRenderer;
import com.nyfaria.nyfsquiver.client.QuiverHud;
import com.nyfaria.nyfsquiver.client.model.HasArrowsProperty;
import com.nyfaria.nyfsquiver.client.model.QuiverTypeProperty;
import com.nyfaria.nyfsquiver.client.screen.QuiverScreen;
import com.nyfaria.nyfsquiver.client.tooltip.ClientQuiverTooltip;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.init.KeyBindInit;
import com.nyfaria.nyfsquiver.init.MenuInit;
import com.nyfaria.nyfsquiver.item.tooltip.QuiverTooltip;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterSelectItemModelPropertyEvent;
import top.theillusivec4.curios.api.client.ICurioRenderer;

@EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onKeyBinds(RegisterKeyMappingsEvent event) {
        event.registerCategory(KeyBindInit.CATEGORY);
        event.register(KeyBindInit.OPEN_SCREEN);
        event.register(KeyBindInit.NEXT_SLOT);
        event.register(KeyBindInit.PREV_SLOT);
    }

    @SubscribeEvent
    public static void onTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(QuiverTooltip.class, ClientQuiverTooltip::new);
    }

    @SubscribeEvent
    public static void registerScreen(RegisterMenuScreensEvent event) {
        event.register(MenuInit.QUIVER_MENU.get(), QuiverScreen::new);
    }

    @SubscribeEvent
    public static void registerSelectProperties(RegisterSelectItemModelPropertyEvent event) {
        event.register(Constants.modLoc("quiver_type"), QuiverTypeProperty.TYPE);
    }

    @SubscribeEvent
    public static void registerConditionalProperties(RegisterConditionalItemModelPropertyEvent event) {
        event.register(Constants.modLoc("has_arrows"), HasArrowsProperty.MAP_CODEC);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ICurioRenderer.register(ItemInit.QUIVER.get(), CurioQuiverRenderer::new);
    }

    @SubscribeEvent
    public static void onOverlayRegister(RegisterGuiLayersEvent event) {
        event.registerAboveAll(Constants.modLoc("ability_overlay"), new QuiverHud()::extract);
    }
}
