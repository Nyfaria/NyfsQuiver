package com.nyfaria.nyfsquiver;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import com.nyfaria.nyfsquiver.common.items.QuiverType;

public class NQConfig {

    static{
        Pair<NQConfig,ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(NQConfig::new);
        CONFIG_SPEC = pair.getRight();
        INSTANCE = pair.getLeft();
    }

    public static final ForgeConfigSpec CONFIG_SPEC;
    public static final NQConfig INSTANCE;

    //public ForgeConfigSpec.BooleanValue allowBagInBag;
    //public ForgeConfigSpec.IntValue maxBagInBagLayer;

    public ForgeConfigSpec.BooleanValue basicEnable;
    public ForgeConfigSpec.IntValue basicRows;
    public ForgeConfigSpec.BooleanValue ironEnable;
    public ForgeConfigSpec.IntValue ironRows;
    public ForgeConfigSpec.BooleanValue copperEnable;
    public ForgeConfigSpec.IntValue copperRows;
    public ForgeConfigSpec.BooleanValue silverEnable;
    public ForgeConfigSpec.IntValue silverRows;
    public ForgeConfigSpec.BooleanValue goldEnable;
    public ForgeConfigSpec.IntValue goldRows;
    public ForgeConfigSpec.BooleanValue diamondEnable;
    public ForgeConfigSpec.IntValue diamondRows;
    public ForgeConfigSpec.BooleanValue obsidianEnable;
    public ForgeConfigSpec.IntValue obsidianRows;
    //public static ForgeConfigSpec.IntValue x;
    //public static ForgeConfigSpec.IntValue y;

    private NQConfig(ForgeConfigSpec.Builder builder){
//        this.basicEnable = builder.worldRestart().comment("Enable the basic quiver?").define("enableBasic", true);
        this.basicRows = builder.worldRestart().comment("How many rows does the basic quiver have?").defineInRange("rowsBasic", QuiverType.BASIC.getDefaultRows(), 1, 13);
//        this.ironEnable = builder.worldRestart().comment("Enable the iron quiver?").define("enableIron", true);
        this.ironRows = builder.worldRestart().comment("How many rows does the iron quiver have?").defineInRange("rowsIron", QuiverType.IRON.getDefaultRows(), 1, 13);
//        this.copperEnable = builder.worldRestart().comment("Enable the copper quiver?").define("enableCopper", true);
        this.copperRows = builder.worldRestart().comment("How many rows does the copper quiver have?").defineInRange("rowsCopper", QuiverType.COPPER.getDefaultRows(), 1, 13);
//        this.silverEnable = builder.worldRestart().comment("Enable the silver quiver?").define("enableSilver", true);
        this.silverRows = builder.worldRestart().comment("How many rows does the silver quiver have?").defineInRange("rowsSilver", QuiverType.SILVER.getDefaultRows(), 1, 13);
//        this.goldEnable = builder.worldRestart().comment("Enable the gold quiver?").define("enableGold", true);
        this.goldRows = builder.worldRestart().comment("How many rows does the gold quiver have?").defineInRange("rowsGold", QuiverType.GOLD.getDefaultRows(), 1, 13);
//        this.diamondEnable = builder.worldRestart().comment("Enable the diamond quiver?").define("enableDiamond", true);
        this.diamondRows = builder.worldRestart().comment("How many rows does the diamond quiver have?").defineInRange("rowsDiamond", QuiverType.DIAMOND.getDefaultRows(), 1, 13);
//        this.obsidianEnable = builder.worldRestart().comment("Enable the obsidian quiver?").define("enableObsidian", true);
        this.obsidianRows = builder.worldRestart().comment("How many rows does the Netherite quiver have?").defineInRange("rowsNetherite", QuiverType.NETHERITE.getDefaultRows(), 1, 13);
        //x = builder.defineInRange("xPos",76,-1000,1000);
        //y = builder.defineInRange("yPos",24,-1000,1000);
    }

}