package com.nyfaria.nyfsquiver;


import com.nyfaria.nyfsquiver.config.NQConfigClient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(Constants.MODID)
public class NyfsQuiver {


    public NyfsQuiver(ModContainer container, IEventBus eventBus) {
        Constants.LOG.info("Hello NeoForge world!");
        CommonClass.init();
//        container.registerConfig(ModConfig.Type.COMMON, NQConfig.CONFIG_SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, NQConfigClient.CLIENT_SPEC);

    }
}