package com.nyfaria.nyfsquiver.config;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum Anchor {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    HOTBAR_LEFT,
    HOTBAR_RIGHT;


    public double getX() {
        switch(this){
            case TOP_LEFT, BOTTOM_LEFT -> {
                return 12;
            }
            case TOP_RIGHT, BOTTOM_RIGHT -> {
                return Minecraft.getInstance().getWindow().getGuiScaledWidth() - 12;
            }
            case HOTBAR_LEFT -> {
                return (Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2d) - 91 - 12;
            }
            case HOTBAR_RIGHT -> {
                return (Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2d) + 91 + 12;
            }
        }
        return 0;
    }

    public double getY() {
        switch(this){
            case TOP_LEFT, TOP_RIGHT -> {
                return 12;
            }
            case BOTTOM_LEFT, BOTTOM_RIGHT, HOTBAR_LEFT, HOTBAR_RIGHT -> {
                return Minecraft.getInstance().getWindow().getGuiScaledHeight() - 12;
            }
        }
        return 0;
    }
}
