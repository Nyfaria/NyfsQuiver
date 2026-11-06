package com.nyfaria.nyfsquiver.platform;

import com.nyfaria.nyfsquiver.init.DataComponentInit;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.menu.QuiverMenu;
import com.nyfaria.nyfsquiver.platform.services.IPlatformHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLEnvironment.isProduction();
    }

    @Override
    public void openQuiverMenu(MenuProvider provider, Player player, ItemStack stack) {
        player.openMenu(provider,(rfbb)->{
            ItemStack.STREAM_CODEC.encode(rfbb,stack);
        });
    }

    @Override
    public MenuType<QuiverMenu> registerMenu() {
        return IMenuTypeExtension.create(QuiverMenu::new);
    }

    @Override
    public ItemStack getEquippedQuiver(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .flatMap(handler -> handler.findFirstCurio(ItemInit.QUIVER.get()))
                .map(SlotResult::stack)
                .orElse(ItemStack.EMPTY);
    }
}
