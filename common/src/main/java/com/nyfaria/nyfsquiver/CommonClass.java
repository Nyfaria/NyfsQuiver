package com.nyfaria.nyfsquiver;

import com.nyfaria.nyfsquiver.init.ArrowActionInit;
import com.nyfaria.nyfsquiver.init.BlockInit;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.init.MenuInit;
import com.nyfaria.nyfsquiver.init.QuiverInit;
import com.nyfaria.nyfsquiver.init.RecipeSerializerInit;
import com.nyfaria.nyfsquiver.init.TagInit;
import com.nyfaria.nyfsquiver.network.PacketInit;
import io.wispforest.accessories.api.AccessoriesAPI;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.pond.AccessoriesAPIAccess;

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
}