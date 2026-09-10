package com.nyfaria.nyfsquiver.events;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.client.ClientUtil;
import com.nyfaria.nyfsquiver.client.QuiverHud;
import com.nyfaria.nyfsquiver.client.screen.QuiverScreen;
import com.nyfaria.nyfsquiver.client.tooltip.ClientQuiverTooltip;
import com.nyfaria.nyfsquiver.compat.curios.CuriosCompat;
import com.nyfaria.nyfsquiver.compat.curios.client.CuriosClientCompat;
import com.nyfaria.nyfsquiver.config.NQConfigClient;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.init.KeyBindInit;
import com.nyfaria.nyfsquiver.init.MenuInit;
import com.nyfaria.nyfsquiver.init.TagInit;
import com.nyfaria.nyfsquiver.item.QuiverItem;
import com.nyfaria.nyfsquiver.item.tooltip.QuiverTooltip;
import com.nyfaria.nyfsquiver.menu.QuiverContainer;
import com.nyfaria.nyfsquiver.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onKeyBinds(RegisterKeyMappingsEvent event) {
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
    public static void clientSetup(FMLClientSetupEvent event) {
        ClientUtil.renderRegistration();
        if (Services.PLATFORM.isModLoaded(CuriosCompat.MOD_ID)) {
            CuriosClientCompat.registerRenderers();
        }
    }

    @SubscribeEvent
    public static void onExtraModels(ModelEvent.RegisterAdditional event) {
        Set<ResourceLocation> locs = Minecraft.getInstance().getResourceManager().listResources("models", loc -> loc.getPath().contains("/quiver/") && loc.getPath().endsWith(".json"))
                .keySet();
        for (ResourceLocation s : locs) {
            String path = s.getPath().substring("models/".length(), s.getPath().length() - ".json".length());
            event.register(ModelResourceLocation.standalone(Constants.loc(s.getNamespace(), path)));
        }
    }

    @SubscribeEvent
    public static void onOverlayRegister(RegisterGuiLayersEvent event) {
        event.registerAboveAll(Constants.modLoc("ability_overlay"), new QuiverHud()::render);
    }
    public static float bezier(float x, float min, float max) {
        return Mth.clamp(((x * x) * (3 - 2 * x)) / (1 / (max - min)) + min, min, max);
    }
}
