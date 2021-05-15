package com.nyfaria.nyfsquiver.common.items;

import java.util.Locale;

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
                return 1; //NQConfig.INSTANCE.basicRows.get();
            case IRON:
                return 2; //NQConfig.INSTANCE.ironRows.get();
            case COPPER:
                return 2; //NQConfig.INSTANCE.copperRows.get();
            case SILVER:
                return 3; //NQConfig.INSTANCE.silverRows.get();
            case GOLD:
                return 3; //NQConfig.INSTANCE.goldRows.get();
            case DIAMOND:
                return 4; //NQConfig.INSTANCE.diamondRows.get();
            case NETHERITE:
                return 5; //NQConfig.INSTANCE.obsidianRows.get();
        }
        return 0;
    }

    public boolean isEnabled(){
//        switch(this){
//            case BASIC:
//                return PUConfig.INSTANCE.basicEnable.get();
//            case IRON:
//                return PUConfig.INSTANCE.ironEnable.get();
//            case COPPER:
//                return PUConfig.INSTANCE.copperEnable.get();
//            case SILVER:
//                return PUConfig.INSTANCE.silverEnable.get();
//            case GOLD:
//                return PUConfig.INSTANCE.goldEnable.get();
//            case DIAMOND:
//                return PUConfig.INSTANCE.diamondEnable.get();
//            case OBSIDIAN:
//                return PUConfig.INSTANCE.obsidianEnable.get();
//        }
//        return false;
        return true;
    }

    public String getRegistryName(){
        return this.name().toLowerCase(Locale.ENGLISH) + "quiver";
    }
}
