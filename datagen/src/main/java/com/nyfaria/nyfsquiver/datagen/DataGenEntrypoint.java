package com.nyfaria.nyfsquiver.datagen;

import com.nyfaria.nyfsquiver.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Constants.MODID)
public class DataGenEntrypoint {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = generator.addProvider(true, new ModDataPackProvider(packOutput, event.getLookupProvider())).getRegistryProvider();
        generator.addProvider(true, new ModRecipeProvider.Runner(packOutput, provider));
        generator.addProvider(true, new ModLootTableProvider(packOutput, provider));
        generator.addProvider(true, new ModTagProvider.ModItemTags(packOutput, provider));
        generator.addProvider(true, new ModTagProvider.ModEnchantTags(packOutput, provider));
        generator.addProvider(true, new ModLangProvider(packOutput));
    }
}
