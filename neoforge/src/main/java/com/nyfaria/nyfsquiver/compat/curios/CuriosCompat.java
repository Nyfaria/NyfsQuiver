package com.nyfaria.nyfsquiver.compat.curios;

import com.nyfaria.nyfsquiver.init.ItemInit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

/**
 * Every Curios API reference of the mod is contained in this class, which is only ever loaded
 * while Curios is installed (see {@link com.nyfaria.nyfsquiver.platform.NeoForgePlatformHelper}).
 * <p>
 * The dedicated slot itself is data driven:
 * <ul>
 *     <li>{@code data/nyfsquiver/curios/slots/quiver.json} registers the slot type (size 1)</li>
 *     <li>{@code data/nyfsquiver/curios/entities/quiver.json} gives it to players / armor stands /
 *     skeletons</li>
 *     <li>{@code data/curios/tags/item/quiver.json} allows the mod's quiver items in that slot</li>
 * </ul>
 */
public final class CuriosCompat {

    public static final String MOD_ID = "curios";

    /** Identifier of the dedicated slot; also the item tag name (curios:quiver). */
    public static final String QUIVER_SLOT = "quiver";

    private CuriosCompat() {}

    /**
     * @return the quiver equipped in the mod's Curios slot, or an empty stack when the player
     * wears none.
     */
    public static ItemStack getEquippedQuiver(Player player) {
        Optional<ICuriosItemHandler> handler = CuriosApi.getCuriosInventory(player);
        if (handler.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return handler.get()
                .findFirstCurio(ItemInit.QUIVER.get())
                .map(SlotResult::stack)
                .orElse(ItemStack.EMPTY);
    }
}
