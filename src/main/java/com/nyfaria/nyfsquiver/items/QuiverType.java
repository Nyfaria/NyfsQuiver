package com.nyfaria.nyfsquiver.items;

import com.nyfaria.nyfsquiver.config.NQConfig;
import net.minecraftforge.common.IExtensibleEnum;

public enum QuiverType implements IExtensibleEnum {

    BASIC(1, 9, true, false),
    IRON(2,9, true, false),
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
            case GOLD:
                return NQConfig.INSTANCE.goldRows.get();
            case DIAMOND:
                return NQConfig.INSTANCE.diamondRows.get();
            case NETHERITE:
                return NQConfig.INSTANCE.obsidianRows.get();
        }
        return defaultRows;
    }

    public int getColumns(){
        switch(this){
            case BASIC:
                return NQConfig.INSTANCE.basicColumns.get();
            case IRON:
                return NQConfig.INSTANCE.ironColumns.get();
            case GOLD:
                return NQConfig.INSTANCE.goldColumns.get();
            case DIAMOND:
                return NQConfig.INSTANCE.diamondColumns.get();
            case NETHERITE:
                return NQConfig.INSTANCE.obsidianColumns.get();
        }
        return defaultColumns;
    }
    public static QuiverType create(String name, int defaultRows, int defaultColumns, boolean enabled, boolean fireProof){
        throw new IllegalStateException("Enum not extended");
    }
}
