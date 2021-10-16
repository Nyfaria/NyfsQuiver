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

    public ForgeConfigSpec.BooleanValue basicEnable;
    public ForgeConfigSpec.BooleanValue ironEnable;
    public ForgeConfigSpec.BooleanValue copperEnable;
    public ForgeConfigSpec.BooleanValue silverEnable;
    public ForgeConfigSpec.BooleanValue goldEnable;
    public ForgeConfigSpec.BooleanValue diamondEnable;
    public ForgeConfigSpec.BooleanValue obsidianEnable;
    public ForgeConfigSpec.BooleanValue basicFireproof;
    public ForgeConfigSpec.BooleanValue ironFireproof;
    public ForgeConfigSpec.BooleanValue copperFireproof;
    public ForgeConfigSpec.BooleanValue silverFireproof;
    public ForgeConfigSpec.BooleanValue goldFireproof;
    public ForgeConfigSpec.BooleanValue diamondFireproof;
    public ForgeConfigSpec.BooleanValue obsidianFireproof;
    public ForgeConfigSpec.IntValue basicRows;
    public ForgeConfigSpec.IntValue ironRows;
    public ForgeConfigSpec.IntValue copperRows;
    public ForgeConfigSpec.IntValue silverRows;
    public ForgeConfigSpec.IntValue goldRows;
    public ForgeConfigSpec.IntValue diamondRows;
    public ForgeConfigSpec.IntValue obsidianRows;
    public ForgeConfigSpec.IntValue basicColumns;
    public ForgeConfigSpec.IntValue ironColumns;
    public ForgeConfigSpec.IntValue copperColumns;
    public ForgeConfigSpec.IntValue silverColumns;
    public ForgeConfigSpec.IntValue goldColumns;
    public ForgeConfigSpec.IntValue diamondColumns;
    public ForgeConfigSpec.IntValue obsidianColumns;
    public static ForgeConfigSpec.IntValue x;
    public static ForgeConfigSpec.IntValue y;

    private NQConfig(ForgeConfigSpec.Builder builder){
        this.basicEnable = builder.worldRestart().comment("Enable the basic quiver?").define("enableBasic", true);
        this.ironEnable = builder.worldRestart().comment("Enable the iron quiver?").define("enableIron", true);
        this.copperEnable = builder.worldRestart().comment("Enable the copper quiver?").define("enableCopper", true);
        this.silverEnable = builder.worldRestart().comment("Enable the silver quiver?").define("enableSilver", true);
        this.goldEnable = builder.worldRestart().comment("Enable the gold quiver?").define("enableGold", true);
        this.diamondEnable = builder.worldRestart().comment("Enable the diamond quiver?").define("enableDiamond", true);
        this.obsidianEnable = builder.worldRestart().comment("Enable the obsidian quiver?").define("enableObsidian", true);

        this.basicFireproof = builder.worldRestart().comment("Fireproof the basic quiver?").define("fireproofBasic", true);
        this.ironFireproof = builder.worldRestart().comment("Fireproof the iron quiver?").define("fireproofIron", true);
        this.copperFireproof = builder.worldRestart().comment("Fireproof the copper quiver?").define("fireproofCopper", true);
        this.silverFireproof = builder.worldRestart().comment("Fireproof the silver quiver?").define("fireproofSilver", true);
        this.goldFireproof = builder.worldRestart().comment("Fireproof the gold quiver?").define("fireproofGold", true);
        this.diamondFireproof = builder.worldRestart().comment("Fireproof the diamond quiver?").define("fireproofDiamond", true);
        this.obsidianFireproof = builder.worldRestart().comment("Fireproof the obsidian quiver?").define("fireproofObsidian", true);

        this.basicRows = builder.worldRestart().comment("How many rows does the basic quiver have?").defineInRange("rowsBasic", QuiverType.BASIC.getDefaultRows(), 1, 13);
        this.ironRows = builder.worldRestart().comment("How many rows does the iron quiver have?").defineInRange("rowsIron", QuiverType.IRON.getDefaultRows(), 1, 13);
        this.copperRows = builder.worldRestart().comment("How many rows does the copper quiver have?").defineInRange("rowsCopper", QuiverType.COPPER.getDefaultRows(), 1, 13);
        this.silverRows = builder.worldRestart().comment("How many rows does the silver quiver have?").defineInRange("rowsSilver", QuiverType.SILVER.getDefaultRows(), 1, 13);
        this.goldRows = builder.worldRestart().comment("How many rows does the gold quiver have?").defineInRange("rowsGold", QuiverType.GOLD.getDefaultRows(), 1, 13);
        this.diamondRows = builder.worldRestart().comment("How many rows does the diamond quiver have?").defineInRange("rowsDiamond", QuiverType.DIAMOND.getDefaultRows(), 1, 13);
        this.obsidianRows = builder.worldRestart().comment("How many rows does the Netherite quiver have?").defineInRange("rowsNetherite", QuiverType.NETHERITE.getDefaultRows(), 1, 13);


        this.basicColumns = builder.worldRestart().comment("How many columns does the basic quiver have?").defineInRange("columnsBasic", QuiverType.BASIC.getDefaultColumns(), 1, 13);
        this.ironColumns = builder.worldRestart().comment("How many columns does the iron quiver have?").defineInRange("columnsIron", QuiverType.IRON.getDefaultColumns(), 1, 13);
        this.copperColumns = builder.worldRestart().comment("How many columns does the copper quiver have?").defineInRange("columnsCopper", QuiverType.COPPER.getDefaultColumns(), 1, 13);
        this.silverColumns = builder.worldRestart().comment("How many columns does the silver quiver have?").defineInRange("columnsSilver", QuiverType.SILVER.getDefaultColumns(), 1, 13);
        this.goldColumns = builder.worldRestart().comment("How many columns does the gold quiver have?").defineInRange("columnsGold", QuiverType.GOLD.getDefaultColumns(), 1, 13);
        this.diamondColumns = builder.worldRestart().comment("How many columns does the diamond quiver have?").defineInRange("columnsDiamond", QuiverType.DIAMOND.getDefaultColumns(), 1, 13);
        this.obsidianColumns = builder.worldRestart().comment("How many columns does the Netherite quiver have?").defineInRange("columnsNetherite", QuiverType.NETHERITE.getDefaultColumns(), 1, 13);
        
        
        x = builder.defineInRange("xPos",76,-1000,1000);
        y = builder.defineInRange("yPos",24,-1000,1000);
    }

}