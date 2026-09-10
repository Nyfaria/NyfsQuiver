package com.nyfaria.nyfsquiver;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.entity.player.*;

public class NyfsQuiver implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonClass.init();
        // Fabric registers items eagerly, so the accessory integration can be set up right away
        CommonClass.postInit();
    }
}