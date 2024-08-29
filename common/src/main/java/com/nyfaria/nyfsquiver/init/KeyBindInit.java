package com.nyfaria.nyfsquiver.init;

import com.mojang.blaze3d.platform.InputConstants;
import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.network.c2s.NextSlotPacket;
import com.nyfaria.nyfsquiver.network.c2s.OpenEquippedQuiverPacket;
import commonnetwork.api.Network;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class KeyBindInit {
    public static KeyMapping OPEN_SCREEN = new KeyMapping("key." + Constants.MODID + ".open_quiver", InputConstants.KEY_V, "key.categories." + Constants.MODID);
    public static KeyMapping NEXT_SLOT = new KeyMapping("key." + Constants.MODID + ".next_slot", InputConstants.KEY_RBRACKET, "key.categories." + Constants.MODID);
    public static KeyMapping PREV_SLOT = new KeyMapping("key." + Constants.MODID + ".prev_slot", InputConstants.KEY_LBRACKET, "key.categories." + Constants.MODID);

    public static void onKeyInput(int key, int scanCode, int action, int modifiers) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;
        if (mc.screen != null)
            return;
        if (action != GLFW.GLFW_REPEAT) {
            if (didPress(key, scanCode, action, OPEN_SCREEN)) {
                Network.getNetworkHandler().sendToServer(new OpenEquippedQuiverPacket());
            }
            if (didPress(key, scanCode, action, NEXT_SLOT)) {
                Network.getNetworkHandler().sendToServer(new NextSlotPacket(false));
            }
            if (didPress(key, scanCode, action, PREV_SLOT)) {
                Network.getNetworkHandler().sendToServer(new NextSlotPacket(true));
            }

        }
    }

    private static boolean didPress(int key, int scanCode, int action, KeyMapping keyBinding) {
        return action == GLFW.GLFW_PRESS && isKey(key, scanCode, keyBinding);
    }

    private static boolean isKey(int key, int scanCode, KeyMapping keyBinding) {
        return keyBinding.matches(key, scanCode);
    }
}
