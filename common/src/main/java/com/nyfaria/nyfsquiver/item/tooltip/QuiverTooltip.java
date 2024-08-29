package com.nyfaria.nyfsquiver.item.tooltip;

import com.nyfaria.nyfsquiver.api.QuiverType;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class QuiverTooltip implements TooltipComponent {
    private final NonNullList<ItemStack> items;
    private final int weight;
    private final QuiverType quiverType;

    public QuiverTooltip(NonNullList<ItemStack> itemStacks, int weight, QuiverType quiverType) {
        this.items = itemStacks;
        this.weight = weight;
        this.quiverType = quiverType;
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public int getWeight() {
        return this.weight;
    }


    public QuiverType getQuiverType() {
        return quiverType;
    }
}
