package com.nyfaria.nyfsquiver;

import com.nyfaria.nyfsquiver.api.QuiverType;
import com.nyfaria.nyfsquiver.init.QuiverInit;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

    public static final String MODID = "nyfsquiver";
    public static final String MOD_NAME = "Nyfs Quivers";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static final ResourceKey<QuiverType> LEATHER_QUIVER = QuiverInit.createKey(modLoc("leather_quiver"));
    public static final ResourceKey<QuiverType> IRON_QUIVER = QuiverInit.createKey(modLoc("iron_quiver"));
    public static final ResourceKey<QuiverType> GOLD_QUIVER = QuiverInit.createKey(modLoc("gold_quiver"));
    public static final ResourceKey<QuiverType> DIAMOND_QUIVER = QuiverInit.createKey(modLoc("diamond_quiver"));
    public static final ResourceKey<QuiverType> NETHERITE_QUIVER = QuiverInit.createKey(modLoc("netherite_quiver"));
    public static final ResourceKey<Enchantment> CYCLING = ResourceKey.create(Registries.ENCHANTMENT, modLoc("cycling"));


    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static ResourceLocation mcLoc(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    public static ResourceLocation loc(String path) {
        return ResourceLocation.parse(path);
    }

    public static ResourceLocation loc(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }
}
