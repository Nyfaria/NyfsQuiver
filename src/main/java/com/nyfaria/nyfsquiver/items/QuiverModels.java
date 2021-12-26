package com.nyfaria.nyfsquiver.items;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.nyfaria.nyfsquiver.NyfsQuiver;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class QuiverModels {
    public static ResourceLocation BASIC_QUIVER = new ResourceLocation(NyfsQuiver.MOD_ID,"back/basic_quiver");
    public static ResourceLocation IRON_QUIVER = new ResourceLocation(NyfsQuiver.MOD_ID,"back/iron_quiver");
    public static ResourceLocation GOLD_QUIVER = new ResourceLocation(NyfsQuiver.MOD_ID,"back/gold_quiver");
    public static ResourceLocation DIAMOND_QUIVER = new ResourceLocation(NyfsQuiver.MOD_ID,"back/diamond_quiver");
    public static ResourceLocation NETHERITE_QUIVER = new ResourceLocation(NyfsQuiver.MOD_ID,"back/netherite_quiver");
    public static ResourceLocation BASIC_QUIVER_NOARROWS = new ResourceLocation(NyfsQuiver.MOD_ID,"back/basic_quiver_noarrows");
    public static ResourceLocation IRON_QUIVER_NOARROWS = new ResourceLocation(NyfsQuiver.MOD_ID,"back/iron_quiver_noarrows");
    public static ResourceLocation GOLD_QUIVER_NOARROWS = new ResourceLocation(NyfsQuiver.MOD_ID,"back/gold_quiver_noarrows");
    public static ResourceLocation DIAMOND_QUIVER_NOARROWS = new ResourceLocation(NyfsQuiver.MOD_ID,"back/diamond_quiver_noarrows");
    public static ResourceLocation NETHERITE_QUIVER_NOARROWS = new ResourceLocation(NyfsQuiver.MOD_ID,"back/netherite_quiver_noarrows");



    public static Map<QuiverType,ResourceLocation> ARROWS;
    public static Map<QuiverType,ResourceLocation> NOARROWS;

    public static void init(){

        ARROWS = ImmutableMap.of(
                QuiverType.BASIC,BASIC_QUIVER,
                QuiverType.IRON,IRON_QUIVER,
                QuiverType.GOLD,GOLD_QUIVER,
                QuiverType.DIAMOND,DIAMOND_QUIVER,
                QuiverType.NETHERITE,NETHERITE_QUIVER
        );

        NOARROWS = ImmutableMap.of(
                QuiverType.BASIC,BASIC_QUIVER_NOARROWS,
                QuiverType.IRON,IRON_QUIVER_NOARROWS,
                QuiverType.GOLD,GOLD_QUIVER_NOARROWS,
                QuiverType.DIAMOND,DIAMOND_QUIVER_NOARROWS,
                QuiverType.NETHERITE,NETHERITE_QUIVER_NOARROWS
        );

    }

    public static ResourceLocation getQuiverModel(ItemStack itemStack, boolean hasArrows){
        QuiverItem quiverItem = (QuiverItem)itemStack.getItem();
        if(hasArrows){
            return ARROWS.get(quiverItem.type);
        }
        return NOARROWS.get(quiverItem.type);
    }
}
