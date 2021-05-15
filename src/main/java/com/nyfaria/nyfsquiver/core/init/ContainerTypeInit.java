package com.nyfaria.nyfsquiver.core.init;

import com.nyfaria.nyfsquiver.common.containers.QuiverContainer;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerTypeInit {

	@ObjectHolder("nyfsquiver:container")
    public static ContainerType<QuiverContainer> container;
}
