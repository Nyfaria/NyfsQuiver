package com.nyfaria.nyfsquiver;

import com.nyfaria.nyfsquiver.client.ClientUtil;
import com.nyfaria.nyfsquiver.client.model.QuiverModel;
import com.nyfaria.nyfsquiver.network.PacketInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

public class NyfsQuiverModels implements ModelLoadingPlugin {

    @Override
    public void onInitializeModelLoader(Context pluginContext) {
//        pluginContext.modifyModelOnLoad().register((original,context)->{
//            if(context.topLevelId().toString().contains("quiver")){
//                return new QuiverModel(original, context.loader());
//            } else {
//                return original;
//            }
//        });
    }
}
