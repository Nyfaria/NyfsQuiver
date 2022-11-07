package com.nyfaria.nyfsquiver.config;

import com.nyfaria.nyfsquiver.items.QuiverType;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class NQConfig {

    public static final ForgeConfigSpec CONFIG_SPEC;
    public static final NQConfig INSTANCE;
    //    public ForgeConfigSpec.IntValue basicDurability;
//    public ForgeConfigSpec.IntValue ironDurability;
//    public ForgeConfigSpec.IntValue copperDurability;
//    public ForgeConfigSpec.IntValue goldDurability;
//    public ForgeConfigSpec.IntValue diamondDurability;
//    public ForgeConfigSpec.IntValue obsidianDurability;
    public static ForgeConfigSpec.IntValue x;
    public static ForgeConfigSpec.IntValue y;

    static {
        Pair<NQConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(NQConfig::new);
        CONFIG_SPEC = pair.getRight();
        INSTANCE = pair.getLeft();
    }

    public ForgeConfigSpec.BooleanValue requireQuiver;
    public ForgeConfigSpec.BooleanValue meldingEnchantTable;
    public ForgeConfigSpec.BooleanValue meldingBooks;
    public ForgeConfigSpec.BooleanValue cyclingEnchantTable;
    public ForgeConfigSpec.BooleanValue cyclingBooks;
    public ForgeConfigSpec.BooleanValue quinfinityEnchantTable;
    public ForgeConfigSpec.BooleanValue quinfinityBooks;
    public ForgeConfigSpec.BooleanValue quiverOnHip;
    public ForgeConfigSpec.IntValue basicRows;
    public ForgeConfigSpec.IntValue ironRows;
    public ForgeConfigSpec.IntValue copperRows;
    public ForgeConfigSpec.IntValue goldRows;
    public ForgeConfigSpec.IntValue diamondRows;
    public ForgeConfigSpec.IntValue obsidianRows;
    public ForgeConfigSpec.IntValue basicColumns;
    public ForgeConfigSpec.IntValue ironColumns;
    public ForgeConfigSpec.IntValue copperColumns;
    public ForgeConfigSpec.IntValue goldColumns;
    public ForgeConfigSpec.IntValue diamondColumns;
    public ForgeConfigSpec.IntValue obsidianColumns;

    private NQConfig(ForgeConfigSpec.Builder builder) {

        this.requireQuiver = builder.worldRestart().comment("Should Quivers be Required for Bow Usage?").define("requireQuiver", false);
        this.meldingEnchantTable = builder.worldRestart().comment("Should Melding show up in the Enchanting Table?").define("meldingEnchantTable", true);
        this.meldingBooks = builder.worldRestart().comment("Should Melding show up on Books?").define("meldingBooks", true);
        this.cyclingEnchantTable = builder.worldRestart().comment("Should Cycling show up in the Enchanting Table?").define("cyclingEnchantTable", true);
        this.cyclingBooks = builder.worldRestart().comment("Should Cycling show up on Books?").define("cyclingBooks", true);
        this.quinfinityEnchantTable = builder.worldRestart().comment("Should Quinfinity show up in the Enchanting Table?").define("quinfinityEnchantTable", true);
        this.quinfinityBooks = builder.worldRestart().comment("Should Quinfinity show up on Books?").define("quinfinityBooks", true);
        this.quiverOnHip = builder.worldRestart().comment("Should the Quiver be Rendered at the Hip?").define("quiverOnHip", true);

        this.basicRows = builder.worldRestart().comment("How many rows does the basic quiver have?").defineInRange("rowsBasic", QuiverType.BASIC.getDefaultRows(), 1, 13);
        this.ironRows = builder.worldRestart().comment("How many rows does the iron quiver have?").defineInRange("rowsIron", QuiverType.IRON.getDefaultRows(), 1, 13);
        this.copperRows = builder.worldRestart().comment("How many rows does the copper quiver have?").defineInRange("rowsCopper", QuiverType.COPPER.getDefaultRows(), 1, 13);
        this.goldRows = builder.worldRestart().comment("How many rows does the gold quiver have?").defineInRange("rowsGold", QuiverType.GOLD.getDefaultRows(), 1, 13);
        this.diamondRows = builder.worldRestart().comment("How many rows does the diamond quiver have?").defineInRange("rowsDiamond", QuiverType.DIAMOND.getDefaultRows(), 1, 13);
        this.obsidianRows = builder.worldRestart().comment("How many rows does the Netherite quiver have?").defineInRange("rowsNetherite", QuiverType.NETHERITE.getDefaultRows(), 1, 13);


        this.basicColumns = builder.worldRestart().comment("How many columns does the basic quiver have?").defineInRange("columnsBasic", QuiverType.BASIC.getDefaultColumns(), 1, 13);
        this.ironColumns = builder.worldRestart().comment("How many columns does the iron quiver have?").defineInRange("columnsIron", QuiverType.IRON.getDefaultColumns(), 1, 13);
        this.copperColumns = builder.worldRestart().comment("How many columns does the copper quiver have?").defineInRange("columnsCopper", QuiverType.COPPER.getDefaultColumns(), 1, 13);
        this.goldColumns = builder.worldRestart().comment("How many columns does the gold quiver have?").defineInRange("columnsGold", QuiverType.GOLD.getDefaultColumns(), 1, 13);
        this.diamondColumns = builder.worldRestart().comment("How many columns does the diamond quiver have?").defineInRange("columnsDiamond", QuiverType.DIAMOND.getDefaultColumns(), 1, 13);
        this.obsidianColumns = builder.worldRestart().comment("How many columns does the Netherite quiver have?").defineInRange("columnsNetherite", QuiverType.NETHERITE.getDefaultColumns(), 1, 13);

//        this.basicDurability = builder.worldRestart().comment("How many durability does the basic quiver have?").defineInRange("durabilityBasic", QuiverType.BASIC.getDefaultDurability(), 1, 10000);
//        this.ironDurability = builder.worldRestart().comment("How many durability does the iron quiver have?").defineInRange("durabilityIron", QuiverType.IRON.getDefaultDurability(), 1, 10000);
//        this.copperDurability = builder.worldRestart().comment("How many durability does the copper quiver have?").defineInRange("durabilityCopper", QuiverType.COPPER.getDefaultDurability(), 1, 10000);
//        this.goldDurability = builder.worldRestart().comment("How many durability does the gold quiver have?").defineInRange("durabilityGold", QuiverType.GOLD.getDefaultDurability(), 1, 10000);
//        this.diamondDurability = builder.worldRestart().comment("How many durability does the diamond quiver have?").defineInRange("durabilityDiamond", QuiverType.DIAMOND.getDefaultDurability(), 1, 10000);
//        this.obsidianDurability = builder.worldRestart().comment("How many durability does the Netherite quiver have?").defineInRange("durabilityNetherite", QuiverType.NETHERITE.getDefaultDurability(), 1, 10000);
//

        x = builder.defineInRange("xPos", 76, -1000, 1000);
        y = builder.defineInRange("yPos", 24, -1000, 1000);
    }

}