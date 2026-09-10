package com.nyfaria.nyfsquiver.compat.curios.client;

import com.nyfaria.nyfsquiver.init.ItemInit;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

/**
 * Client side half of the Curios integration; only loaded while Curios is installed. Must be
 * called from {@code FMLClientSetupEvent} so that Curios can pick the renderer up in
 * {@code EntityRenderersEvent.AddLayers}.
 */
public final class CuriosClientCompat {

    private CuriosClientCompat() {}

    public static void registerRenderers() {
        CuriosRendererRegistry.register(ItemInit.QUIVER.get(), CuriosQuiverRenderer::new);
    }
}
