package com.nyfaria.nyfsquiver.init;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.items.QuiverContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerInit {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, NyfsQuiver.MOD_ID);

    public static final RegistryObject<MenuType<QuiverContainer>> QUIVER_CONTAINER = CONTAINERS.register("container",()->IForgeMenuType.create((windowId, inv, data) -> {
        int bagSlot = data.readInt();
        int inventoryIndex = data.readInt();
        int rows = data.readInt();
        int columns = data.readInt();

        return new QuiverContainer(windowId, inv, bagSlot, inventoryIndex, rows, columns);
    }));
}
