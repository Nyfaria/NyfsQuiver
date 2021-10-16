package com.nyfaria.nyfsquiver.common.items;

import java.util.Locale;

import com.nyfaria.nyfsquiver.NQConfig;

public enum QuiverType {

    BASIC(1, 9, true, false),
    IRON(2,9, true, false),
    COPPER(2,9, true, false),
    SILVER(3,9, true, false),
    GOLD(3,9, true, false),
    DIAMOND(4,9, true, false),
    NETHERITE(5,9, true, true);

    private int defaultRows;
    private int defaultColumns;
    public boolean enabled;
    public boolean fireProof;

    QuiverType(int defaultRows, int defaultColumns, boolean enabled, boolean fireProof){
        this.defaultRows = defaultRows;
        this.defaultColumns = defaultColumns;
        this.enabled = enabled;
        this.fireProof = fireProof;

    }

    public int getDefaultRows(){
        return this.defaultRows;
    }
    public int getDefaultColumns(){
        return this.defaultColumns;
    }
    public boolean getFireProof(){
        return this.fireProof;
    }

    public int getRows(){
        switch(this){
            case BASIC:
                return NQConfig.INSTANCE.basicRows.get();
            case IRON:
                return NQConfig.INSTANCE.ironRows.get();
            case COPPER:
                return NQConfig.INSTANCE.copperRows.get();
            case SILVER:
                return NQConfig.INSTANCE.silverRows.get();
            case GOLD:
                return NQConfig.INSTANCE.goldRows.get();
            case DIAMOND:
                return NQConfig.INSTANCE.diamondRows.get();
            case NETHERITE:
                return NQConfig.INSTANCE.obsidianRows.get();
        }
        return 0;
    }

    public int getColumns(){
        switch(this){
            case BASIC:
                return NQConfig.INSTANCE.basicColumns.get();
            case IRON:
                return NQConfig.INSTANCE.ironColumns.get();
            case COPPER:
                return NQConfig.INSTANCE.copperColumns.get();
            case SILVER:
                return NQConfig.INSTANCE.silverColumns.get();
            case GOLD:
                return NQConfig.INSTANCE.goldColumns.get();
            case DIAMOND:
                return NQConfig.INSTANCE.diamondColumns.get();
            case NETHERITE:
                return NQConfig.INSTANCE.obsidianColumns.get();
        }
        return 0;
    }
    public boolean isEnabled(){
        switch(this){
            case BASIC:
                return NQConfig.INSTANCE.basicEnable.get();
            case IRON:
                return NQConfig.INSTANCE.ironEnable.get();
            case COPPER:
                return NQConfig.INSTANCE.copperEnable.get();
            case SILVER:
                return NQConfig.INSTANCE.silverEnable.get();
            case GOLD:
                return NQConfig.INSTANCE.goldEnable.get();
            case DIAMOND:
                return NQConfig.INSTANCE.diamondEnable.get();
            case NETHERITE:
                return NQConfig.INSTANCE.obsidianEnable.get();
        }
        return false;
    }

    public String getRegistryName(){
        return this.name().toLowerCase(Locale.ENGLISH) + "quiver";
    }
}
