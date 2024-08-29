package com.nyfaria.nyfsquiver;

import com.nyfaria.nyfsquiver.init.KeyBindInit;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class NyfsQuiver implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonClass.init();
        KeyBindingHelper.registerKeyBinding(KeyBindInit.OPEN_SCREEN);
    }
}
