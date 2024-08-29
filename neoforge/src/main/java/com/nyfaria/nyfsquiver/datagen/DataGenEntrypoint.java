package com.nyfaria.nyfsquiver.datagen;

import com.nyfaria.nyfsquiver.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenEntrypoint {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        boolean includeServer = event.includeServer();
        boolean includeClient = event.includeClient();
        CompletableFuture<HolderLookup.Provider> provider = generator.addProvider(includeServer, new ModDataPackProvider(packOutput, event.getLookupProvider())).getRegistryProvider();
        generator.addProvider(includeServer, new ModRecipeProvider(packOutput, provider));
        generator.addProvider(includeServer, new ModLootTableProvider(packOutput, provider));
        generator.addProvider(includeServer, new ModSoundProvider(packOutput, existingFileHelper));
        generator.addProvider(includeServer, new ModTagProvider.ModBlockTags(packOutput, provider, existingFileHelper));
        generator.addProvider(includeServer, new ModTagProvider.ModItemTags(packOutput, provider, existingFileHelper));
        generator.addProvider(includeServer, new ModTagProvider.ModEnchantTags(packOutput, provider, existingFileHelper));
        generator.addProvider(includeClient, new ModItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(includeClient, new ModBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(includeClient, new ModLangProvider(packOutput));

    }
}
