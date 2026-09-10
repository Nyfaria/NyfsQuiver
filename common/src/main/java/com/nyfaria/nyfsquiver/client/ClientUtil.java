package com.nyfaria.nyfsquiver.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.nyfaria.nyfsquiver.compat.AccessoriesClientCompat;
import com.nyfaria.nyfsquiver.platform.Services;
import com.nyfaria.nyfsquiver.util.QuiverEquipment;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class ClientUtil {

    public static boolean isShiftDown() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT);
    }

    /**
     * Registers the renderer that draws the quiver on the wearer. Accessories is a soft
     * dependency, so its renderer is only registered (and its classes only loaded) when the mod
     * is installed. NeoForge adds its Curios renderer in its own client setup.
     */
    public static void renderRegistration(){
        if (Services.PLATFORM.isModLoaded(QuiverEquipment.ACCESSORIES_ID)) {
            AccessoriesClientCompat.registerRenderers();
        }
    }
}
