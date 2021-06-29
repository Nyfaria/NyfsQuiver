package com.nyfaria.nyfsquiver.common.items;

import java.util.Locale;

import com.nyfaria.nyfsquiver.NQConfig;

public enum QuiverType {

    BASIC(1), IRON(2), COPPER(2), SILVER(3), GOLD(3), DIAMOND(4), NETHERITE(5);

    private int defaultRows;

    QuiverType(int defaultRows){
        this.defaultRows = defaultRows;
    }

    public int getDefaultRows(){
        return this.defaultRows;
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

    public boolean isEnabled(){
    	/*
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
        return false;*/
    	return true;
    }

    public String getRegistryName(){
        return this.name().toLowerCase(Locale.ENGLISH) + "quiver";
    }
}
