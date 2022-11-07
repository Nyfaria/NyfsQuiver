package com.nyfaria.nyfsquiver.items;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.config.NQConfig;
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
    public static ResourceLocation BASIC_QUIVER_HIP = new ResourceLocation(NyfsQuiver.MODID, "hip/basic_quiver");
    public static ResourceLocation IRON_QUIVER_HIP = new ResourceLocation(NyfsQuiver.MODID, "hip/iron_quiver");
    public static ResourceLocation COPPER_QUIVER_HIP = new ResourceLocation(NyfsQuiver.MODID, "hip/copper_quiver");
    public static ResourceLocation GOLD_QUIVER_HIP = new ResourceLocation(NyfsQuiver.MODID, "hip/gold_quiver");
    public static ResourceLocation DIAMOND_QUIVER_HIP = new ResourceLocation(NyfsQuiver.MODID, "hip/diamond_quiver");
    public static ResourceLocation NETHERITE_QUIVER_HIP = new ResourceLocation(NyfsQuiver.MODID, "hip/netherite_quiver");
    public static ResourceLocation BASIC_QUIVER_HIP_NOARROWS = new ResourceLocation(NyfsQuiver.MODID, "hip/basic_quiver_noarrows");
    public static ResourceLocation IRON_QUIVER_HIP_NOARROWS = new ResourceLocation(NyfsQuiver.MODID, "hip/iron_quiver_noarrows");
    public static ResourceLocation COPPER_QUIVER_HIP_NOARROWS = new ResourceLocation(NyfsQuiver.MODID, "hip/copper_quiver_noarrows");
    public static ResourceLocation GOLD_QUIVER_HIP_NOARROWS = new ResourceLocation(NyfsQuiver.MODID, "hip/gold_quiver_noarrows");
    public static ResourceLocation DIAMOND_QUIVER_HIP_NOARROWS = new ResourceLocation(NyfsQuiver.MODID, "hip/diamond_quiver_noarrows");
    public static ResourceLocation NETHERITE_QUIVER_HIP_NOARROWS = new ResourceLocation(NyfsQuiver.MODID, "hip/netherite_quiver_noarrows");

    public static Map<QuiverType, ResourceLocation> ARROWS;
    public static Map<QuiverType, ResourceLocation> ARROWS_HIP;
    public static Map<QuiverType, ResourceLocation> NOARROWS;
    public static Map<QuiverType, ResourceLocation> NOARROWS_HIP;

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

        ARROWS_HIP = Map.of(
                QuiverType.BASIC, BASIC_QUIVER_HIP,
                QuiverType.IRON, IRON_QUIVER_HIP,
                QuiverType.COPPER, COPPER_QUIVER_HIP,
                QuiverType.GOLD, GOLD_QUIVER_HIP,
                QuiverType.DIAMOND, DIAMOND_QUIVER_HIP,
                QuiverType.NETHERITE, NETHERITE_QUIVER_HIP
        );
        NOARROWS_HIP = Map.of(
                QuiverType.BASIC, BASIC_QUIVER_HIP_NOARROWS,
                QuiverType.IRON, IRON_QUIVER_HIP_NOARROWS,
                QuiverType.COPPER, COPPER_QUIVER_HIP_NOARROWS,
                QuiverType.GOLD, GOLD_QUIVER_HIP_NOARROWS,
                QuiverType.DIAMOND, DIAMOND_QUIVER_HIP_NOARROWS,
                QuiverType.NETHERITE, NETHERITE_QUIVER_HIP_NOARROWS
        );


    }

    public static ResourceLocation getQuiverModel(ItemStack itemStack, boolean hasArrows, boolean isHip) {
        QuiverItem quiverItem = (QuiverItem) itemStack.getItem();
        if (hasArrows) {
            return isHip ? ARROWS_HIP.get(quiverItem.type) : ARROWS.get(quiverItem.type);
        }
        return isHip ? NOARROWS_HIP.get(quiverItem.type) : NOARROWS.get(quiverItem.type);
    }
}
