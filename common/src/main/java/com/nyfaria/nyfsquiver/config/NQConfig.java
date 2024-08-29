package com.nyfaria.nyfsquiver.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class NQConfig {
    public static final ModConfigSpec CONFIG_SPEC;
    public static final NQConfig INSTANCE;
    static {
        Pair<NQConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(NQConfig::new);
        CONFIG_SPEC = pair.getRight();
        INSTANCE = pair.getLeft();
    }
    public ModConfigSpec.BooleanValue requireQuiver;
    public ModConfigSpec.BooleanValue meldingEnchantTable;
    public ModConfigSpec.BooleanValue meldingBooks;
    public ModConfigSpec.BooleanValue cyclingEnchantTable;
    public ModConfigSpec.BooleanValue cyclingBooks;
    public ModConfigSpec.BooleanValue quinfinityEnchantTable;
    public ModConfigSpec.BooleanValue quinfinityBooks;

    private NQConfig(ModConfigSpec.Builder builder) {
        this.requireQuiver = builder.worldRestart().comment("Should Quivers be Required for Bow Usage?").define("requireQuiver", false);
        this.meldingEnchantTable = builder.worldRestart().comment("Should Melding show up in the Enchanting Table?").define("meldingEnchantTable", true);
        this.meldingBooks = builder.worldRestart().comment("Should Melding show up on Books?").define("meldingBooks", true);
        this.cyclingEnchantTable = builder.worldRestart().comment("Should Cycling show up in the Enchanting Table?").define("cyclingEnchantTable", true);
        this.cyclingBooks = builder.worldRestart().comment("Should Cycling show up on Books?").define("cyclingBooks", true);
        this.quinfinityEnchantTable = builder.worldRestart().comment("Should Quinfinity show up in the Enchanting Table?").define("quinfinityEnchantTable", true);
        this.quinfinityBooks = builder.worldRestart().comment("Should Quinfinity show up on Books?").define("quinfinityBooks", true);
    }
}
