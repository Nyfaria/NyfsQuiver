package com.nyfaria.nyfsquiver.platform;

import com.nyfaria.nyfsquiver.codec.QuiverFabricCodec;
import com.nyfaria.nyfsquiver.menu.QuiverContainer;
import com.nyfaria.nyfsquiver.menu.QuiverMenu;
import com.nyfaria.nyfsquiver.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public void openQuiverMenu(MenuProvider provider, Player player, ItemStack stack) {
        ExtendedScreenHandlerFactory<QuiverFabricCodec> type = new ExtendedScreenHandlerFactory<>(){

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return new QuiverMenu(i, inventory, new QuiverContainer(stack));
            }

            @Override
            public Component getDisplayName() {
                return Component.literal("Quiver");
            }

            @Override
            public QuiverFabricCodec getScreenOpeningData(ServerPlayer player) {
                return new QuiverFabricCodec(stack);
            }

        };
        player.openMenu(type);
    }

    @Override
    public MenuType<QuiverMenu> registerMenu() {
        return new ExtendedScreenHandlerType<>(QuiverMenu::new, QuiverFabricCodec.CODEC);
    }

}
