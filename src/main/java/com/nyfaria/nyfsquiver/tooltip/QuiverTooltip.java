package com.nyfaria.nyfsquiver.tooltip;

import com.nyfaria.nyfsquiver.items.QuiverType;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class QuiverTooltip implements TooltipComponent {
    private final NonNullList<ItemStack> items;
    private final int weight;
    private final QuiverType quiverType;

    public QuiverTooltip(NonNullList<ItemStack> p_150677_, int p_150678_, QuiverType quiverType) {
        this.items = p_150677_;
        this.weight = p_150678_;
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
