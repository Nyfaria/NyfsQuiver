package com.nyfaria.nyfsquiver.init;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.items.QuiverContainer;
import com.nyfaria.nyfsquiver.items.QuiverInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerInit {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, NyfsQuiver.MOD_ID);

    public static final RegistryObject<ContainerType<QuiverContainer>> QUIVER_CONTAINER = CONTAINERS.register("container",()-> IForgeContainerType.create((windowId, inv, data) -> {
        int bagSlot = data.readInt();
        int rows = data.readInt();
        int columns = data.readInt();

        QuiverInventory  quiverInventory = new QuiverInventory(true,rows,columns);
        quiverInventory.deserializeNBT(data.readNbt());
        return new QuiverContainer(windowId, inv, bagSlot, quiverInventory, rows, columns);
    }));
}
