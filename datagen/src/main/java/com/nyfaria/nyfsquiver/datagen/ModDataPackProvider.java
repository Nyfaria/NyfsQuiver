package com.nyfaria.nyfsquiver.datagen;

import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.api.QuiverType;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.init.QuiverInit;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDataPackProvider extends DatapackBuiltinEntriesProvider {



    public ModDataPackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, createBuilder(), Set.of("minecraft", Constants.MODID));
    }


    public static void addDamageType(BootstrapContext<DamageType> context) {

    }
    public static RegistrySetBuilder createBuilder() {
        RegistrySetBuilder builder = new RegistrySetBuilder();
        builder.add(QuiverInit.QUIVER_TYPES.key(), QuiverInit::quiverTypesBootstrap);
        builder.add(Registries.DAMAGE_TYPE, ModDataPackProvider::addDamageType);
        builder.add(Registries.ENCHANTMENT, ModDataPackProvider::addEnchantments);
        return builder;
    }

    private static void addEnchantments(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> holdergetter2 = context.lookup(Registries.ITEM);
        TagKey<Item> quivers = TagKey.create(BuiltInRegistries.ITEM.key(), Constants.modLoc("quiver"));
        context.register(Constants.CYCLING, Enchantment.enchantment(
                Enchantment.definition(
                        holdergetter2.getOrThrow(quivers),10,1,
                        Enchantment.constantCost(15),
                        Enchantment.constantCost(64),
                        8,
                        EquipmentSlotGroup.ANY
                )
        ).build(Constants.modLoc("cycling")));
    }
}
