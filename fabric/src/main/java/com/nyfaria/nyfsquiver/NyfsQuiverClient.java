package com.nyfaria.nyfsquiver;

import com.nyfaria.nyfsquiver.client.ClientUtil;
import com.nyfaria.nyfsquiver.network.PacketInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class NyfsQuiverClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PacketInit.loadClass();
        ClientUtil.renderRegistration();
    }
}
