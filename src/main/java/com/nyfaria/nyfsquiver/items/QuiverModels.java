package com.nyfaria.nyfsquiver.items;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class QuiverModels {
    public static ResourceLocation BASIC_QUIVER = new ResourceLocation(NyfsQuiver.MODID, "back/basic_quiver");
    public static ResourceLocation IRON_QUIVER = new ResourceLocation(NyfsQuiver.MODID, "back/iron_quiver");
    public static ResourceLocation COPPER_QUIVER = new ResourceLocation(NyfsQuiver.MODID, "back/copper_quiver");
    public static ResourceLocation GOLD_QUIVER = new ResourceLocation(NyfsQuiver.MODID, "back/gold_quiver");
    public static ResourceLocation DIAMOND_QUIVER = new ResourceLocation(NyfsQuiver.MODID, "back/diamond_quiver");
    public static ResourceLocation NETHERITE_QUIVER = new ResourceLocation(NyfsQuiver.MODID, "back/netherite_quiver");
    public static ResourceLocation BASIC_QUIVER_NOARROWS = new ResourceLocation(NyfsQuiver.MODID, "back/basic_quiver_noarrows");
    public static ResourceLocation IRON_QUIVER_NOARROWS = new ResourceLocation(NyfsQuiver.MODID, "back/iron_quiver_noarrows");
    public static ResourceLocation COPPER_QUIVER_NOARROWS = new ResourceLocation(NyfsQuiver.MODID, "back/copper_quiver_noarrows");
    public static ResourceLocation GOLD_QUIVER_NOARROWS = new ResourceLocation(NyfsQuiver.MODID, "back/gold_quiver_noarrows");
    public static ResourceLocation DIAMOND_QUIVER_NOARROWS = new ResourceLocation(NyfsQuiver.MODID, "back/diamond_quiver_noarrows");
    public static ResourceLocation NETHERITE_QUIVER_NOARROWS = new ResourceLocation(NyfsQuiver.MODID, "back/netherite_quiver_noarrows");


    public static Map<QuiverType, ResourceLocation> ARROWS;
    public static Map<QuiverType, ResourceLocation> NOARROWS;

    public static void init() {

        ARROWS = Map.of(
                QuiverType.BASIC, BASIC_QUIVER,
                QuiverType.IRON, IRON_QUIVER,
                QuiverType.COPPER, COPPER_QUIVER,
                QuiverType.GOLD, GOLD_QUIVER,
                QuiverType.DIAMOND, DIAMOND_QUIVER,
                QuiverType.NETHERITE, NETHERITE_QUIVER
        );

        NOARROWS = Map.of(
                QuiverType.BASIC, BASIC_QUIVER_NOARROWS,
                QuiverType.IRON, IRON_QUIVER_NOARROWS,
                QuiverType.COPPER, COPPER_QUIVER_NOARROWS,
                QuiverType.GOLD, GOLD_QUIVER_NOARROWS,
                QuiverType.DIAMOND, DIAMOND_QUIVER_NOARROWS,
                QuiverType.NETHERITE, NETHERITE_QUIVER_NOARROWS
        );

    }

    public static ResourceLocation getQuiverModel(ItemStack itemStack, boolean hasArrows) {
        QuiverItem quiverItem = (QuiverItem) itemStack.getItem();
        if (hasArrows) {
            return ARROWS.get(quiverItem.type);
        }
        return NOARROWS.get(quiverItem.type);
    }
}
