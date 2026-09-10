package com.nyfaria.nyfsquiver;

import com.nyfaria.nyfsquiver.compat.AccessoriesCompat;
import com.nyfaria.nyfsquiver.init.ArrowActionInit;
import com.nyfaria.nyfsquiver.init.BlockInit;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.init.MenuInit;
import com.nyfaria.nyfsquiver.init.QuiverInit;
import com.nyfaria.nyfsquiver.init.RecipeSerializerInit;
import com.nyfaria.nyfsquiver.init.TagInit;
import com.nyfaria.nyfsquiver.network.PacketInit;
import com.nyfaria.nyfsquiver.platform.Services;
import com.nyfaria.nyfsquiver.util.QuiverEquipment;

public class CommonClass {

    public static void init() {
        ArrowActionInit.loadClass();
        DataComponentInit.loadClass();
        QuiverInit.loadClass();
        ItemInit.loadClass();
        BlockInit.loadClass();
        TagInit.loadClass();
        MenuInit.loadClass();
        RecipeSerializerInit.loadClass();
        PacketInit.loadClass();
    }

    /**
     * Accessory integration that needs registered items (the Accessories accessory of the quiver
     * item). Called by each loader once its registry is populated.
     */
    public static void postInit() {
        if (Services.PLATFORM.isModLoaded(QuiverEquipment.ACCESSORIES_ID)) {
            // Only ever touched when Accessories is installed, keeping it a soft dependency.
            AccessoriesCompat.registerAccessory();
        }
    }
}