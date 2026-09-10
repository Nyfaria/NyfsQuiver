package com.nyfaria.nyfsquiver.platform.services;

import com.nyfaria.nyfsquiver.menu.QuiverMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }
    void openQuiverMenu(MenuProvider provider, Player player, ItemStack stack);
    MenuType<QuiverMenu> registerMenu();

    /**
     * Gets the quiver the player has equipped in a Curios slot.
     * <p>
     * Curios only exists for NeoForge on this Minecraft version, so the default implementation
     * returns an empty stack; loaders without Curios support simply do not override it.
     *
     * @param player The player to look at.
     * @return The equipped quiver stack, or {@link ItemStack#EMPTY} when there is none (or when
     * Curios is not installed).
     */
    default ItemStack getCuriosEquippedQuiver(Player player) {
        return ItemStack.EMPTY;
    }
}