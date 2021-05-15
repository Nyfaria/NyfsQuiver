package com.nyfaria.nyfsquiver.compat;

import com.nyfaria.nyfsquiver.compat.curios.CuriosOff;
import com.nyfaria.nyfsquiver.compat.curios.CuriosOn;

import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;

public class Compatibility {

    public static CuriosOff CURIOS;

    public static void init(){
        CURIOS = ModList.get().isLoaded(CuriosApi.MODID) ? new CuriosOn() : new CuriosOff();
    }

}
