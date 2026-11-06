package com.nyfaria.nyfsquiver;

import com.nyfaria.nyfsquiver.api.QuiverType;
import com.nyfaria.nyfsquiver.init.QuiverInit;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
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


    public static Identifier modLoc(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }

    public static Identifier mcLoc(String path) {
        return Identifier.withDefaultNamespace(path);
    }

    public static Identifier loc(String path) {
        return Identifier.parse(path);
    }

    public static Identifier loc(String namespace, String path) {
        return Identifier.fromNamespaceAndPath(namespace, path);
    }
}
