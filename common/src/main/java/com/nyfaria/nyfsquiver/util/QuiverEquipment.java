package com.nyfaria.nyfsquiver.util;

import com.nyfaria.nyfsquiver.compat.AccessoriesCompat;
import com.nyfaria.nyfsquiver.platform.Services;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Single entry point for "which quiver is this player wearing?".
 * <p>
 * Both accessory libraries are soft dependencies: Accessories is queried here (through a compat
 * class that is only loaded when the mod is installed) and Curios is queried by the loader
 * specific platform helper, which returns an empty stack when Curios (or the loader's Curios
 * support) is unavailable.
 */
public final class QuiverEquipment {

    public static final String ACCESSORIES_ID = AccessoriesCompat.MOD_ID;
    public static final String CURIOS_ID = "curios";

    private QuiverEquipment() {}

    /**
     * @return the quiver worn by the player, or an empty stack when the player wears none.
     */
    public static ItemStack getEquippedQuiver(@Nullable Player player) {
        if (player == null) {
            return ItemStack.EMPTY;
        }
        if (Services.PLATFORM.isModLoaded(ACCESSORIES_ID)) {
            ItemStack equipped = AccessoriesCompat.getEquippedQuiver(player);
            if (!equipped.isEmpty()) {
                return equipped;
            }
        }
        return Services.PLATFORM.getCuriosEquippedQuiver(player);
    }

    public static boolean isWearingQuiver(@Nullable Player player) {
        return !getEquippedQuiver(player).isEmpty();
    }
}
