package com.nyfaria.nyfsquiver.compat;

import com.nyfaria.nyfsquiver.client.renderer.QuiverRenderer;
import com.nyfaria.nyfsquiver.init.ItemInit;
import io.wispforest.accessories.api.client.AccessoriesRendererRegistry;

/**
 * Client side half of the Accessories integration. Like {@link AccessoriesCompat} this class may
 * only be loaded while Accessories is installed.
 */
public final class AccessoriesClientCompat {

    private AccessoriesClientCompat() {}

    /**
     * Makes the quiver render on the wearer's back / hip when equipped in an Accessories slot.
     */
    public static void registerRenderers() {
        AccessoriesRendererRegistry.registerRenderer(ItemInit.QUIVER.get(), QuiverRenderer::new);
    }
}
