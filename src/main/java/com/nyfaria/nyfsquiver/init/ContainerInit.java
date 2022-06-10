package com.nyfaria.nyfsquiver.init;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.items.QuiverContainer;
import com.nyfaria.nyfsquiver.items.QuiverInventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerInit {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, NyfsQuiver.MODID);

    public static final RegistryObject<MenuType<QuiverContainer>> QUIVER_CONTAINER = CONTAINERS.register("container", () -> IForgeMenuType.create((windowId, inv, data) -> {
        int bagSlot = data.readInt();
        int rows = data.readInt();
        int columns = data.readInt();

        QuiverInventory quiverInventory = new QuiverInventory(true, rows, columns);
        quiverInventory.deserializeNBT(data.readNbt());
        return new QuiverContainer(windowId, inv, bagSlot, quiverInventory, rows, columns);
    }));
}
