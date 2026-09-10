package com.nyfaria.nyfsquiver.events;

import com.nyfaria.nyfsquiver.CommonClass;
import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.api.ArrowAction;
import com.nyfaria.nyfsquiver.api.QuiverType;
import com.nyfaria.nyfsquiver.init.QuiverInit;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber(bus=EventBusSubscriber.Bus.MOD)
public class CommonModEvents {

    @SubscribeEvent
    public static void registryCreate(NewRegistryEvent event) {
    }

    /**
     * Runs once the registries are filled, which is when the accessory integrations of the
     * registered items can be set up.
     */
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(CommonClass::postInit);
    }

}
