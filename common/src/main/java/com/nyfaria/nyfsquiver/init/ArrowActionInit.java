package com.nyfaria.nyfsquiver.init;

import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.api.ArrowAction;
import com.nyfaria.nyfsquiver.registration.RegistrationProvider;
import com.nyfaria.nyfsquiver.registration.RegistryObject;
import net.minecraft.core.Registry;

public class ArrowActionInit {
    public static RegistrationProvider<ArrowAction> ARROW_ACTIONS = RegistrationProvider.get(Constants.modLoc("arrow_action"), Constants.MODID);
    public static Registry<ArrowAction> ACTION_REGISTRY = ARROW_ACTIONS.registryBuilder().build();
    public static RegistryObject<ArrowAction,ArrowAction> VANILLA = ARROW_ACTIONS.register("vanilla", ()->(original, level, ammo, shooter, weapon) -> original);

    public static void loadClass() {

    }
}
