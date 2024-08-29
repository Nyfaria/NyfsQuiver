package com.nyfaria.nyfsquiver.init;

import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.menu.QuiverMenu;
import com.nyfaria.nyfsquiver.platform.Services;
import com.nyfaria.nyfsquiver.platform.services.IPlatformHelper;
import com.nyfaria.nyfsquiver.registration.RegistrationProvider;
import com.nyfaria.nyfsquiver.registration.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class MenuInit {
    public static final RegistrationProvider<MenuType<?>> MENUS = RegistrationProvider.get(BuiltInRegistries.MENU, Constants.MODID);

    public static final RegistryObject<MenuType<?>,MenuType<QuiverMenu>> QUIVER_MENU = MENUS.register("quiver", Services.PLATFORM::registerMenu);

    public static void loadClass() {

    }
}
