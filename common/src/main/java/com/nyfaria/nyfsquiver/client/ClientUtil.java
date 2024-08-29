package com.nyfaria.nyfsquiver.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.nyfaria.nyfsquiver.client.renderer.QuiverRenderer;
import com.nyfaria.nyfsquiver.init.ItemInit;
import io.wispforest.accessories.api.client.AccessoriesRendererRegistry;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class ClientUtil {

    public static boolean isShiftDown() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT);
    }

    public static void renderRegistration(){
        AccessoriesRendererRegistry.registerRenderer(ItemInit.QUIVER.get(), QuiverRenderer::new);
    }
}
