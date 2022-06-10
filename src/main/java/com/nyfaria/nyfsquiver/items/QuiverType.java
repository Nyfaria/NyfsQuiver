package com.nyfaria.nyfsquiver.items;

import com.nyfaria.nyfsquiver.config.NQConfig;
import net.minecraftforge.common.IExtensibleEnum;

public enum QuiverType implements IExtensibleEnum {

    BASIC(1, 9, true, false, 576),
    IRON(2, 9, true, false, 1152),
    COPPER(2, 7, true, false, 896),
    GOLD(3, 9, true, false, 1728),
    DIAMOND(4, 9, true, false, 2304),
    NETHERITE(5, 9, true, true, 2880);

    public boolean enabled;
    public boolean fireProof;
    private final int defaultRows;
    private final int defaultColumns;
    private final int defaultDurability;

    QuiverType(int defaultRows, int defaultColumns, boolean enabled, boolean fireProof, int defaultDurability) {
        this.defaultRows = defaultRows;
        this.defaultColumns = defaultColumns;
        this.enabled = enabled;
        this.fireProof = fireProof;
        this.defaultDurability = defaultDurability;

    }

    public static QuiverType create(String name, int defaultRows, int defaultColumns, boolean enabled, boolean fireProof, int durability) {
        throw new IllegalStateException("Enum not extended");
    }

    public int getDefaultRows() {
        return this.defaultRows;
    }

    public int getDefaultColumns() {
        return this.defaultColumns;
    }

    public boolean getFireProof() {
        return this.fireProof;
    }

    public int getDefaultDurability() {
        return defaultDurability;
    }

    public int getRows() {
        switch (this) {
            case BASIC:
                return NQConfig.INSTANCE.basicRows.get();
            case IRON:
                return NQConfig.INSTANCE.ironRows.get();
            case COPPER:
                return NQConfig.INSTANCE.copperRows.get();
            case GOLD:
                return NQConfig.INSTANCE.goldRows.get();
            case DIAMOND:
                return NQConfig.INSTANCE.diamondRows.get();
            case NETHERITE:
                return NQConfig.INSTANCE.obsidianRows.get();
        }
        return defaultRows;
    }

    public int getColumns() {
        switch (this) {
            case BASIC:
                return NQConfig.INSTANCE.basicColumns.get();
            case IRON:
                return NQConfig.INSTANCE.ironColumns.get();
            case COPPER:
                return NQConfig.INSTANCE.copperColumns.get();
            case GOLD:
                return NQConfig.INSTANCE.goldColumns.get();
            case DIAMOND:
                return NQConfig.INSTANCE.diamondColumns.get();
            case NETHERITE:
                return NQConfig.INSTANCE.obsidianColumns.get();
        }
        return defaultColumns;
    }
}
