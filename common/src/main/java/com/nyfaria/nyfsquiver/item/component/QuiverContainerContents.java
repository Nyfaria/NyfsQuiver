package com.nyfaria.nyfsquiver.item.component;

import com.google.common.collect.Iterables;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Stream;

public final class QuiverContainerContents {
    public static final Codec<QuiverContainerContents> CODEC = QuiverContainerContents.Slot.CODEC
            .listOf()
            .xmap(QuiverContainerContents::fromSlots, QuiverContainerContents::asSlots);
    public static final StreamCodec<RegistryFriendlyByteBuf, QuiverContainerContents> STREAM_CODEC = ItemStack.OPTIONAL_STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(QuiverContainerContents::new, p_333580_ -> p_333580_.items);
    private final NonNullList<ItemStack> items;
    private final int hashCode;

    private QuiverContainerContents(NonNullList<ItemStack> pItems) {
        if (pItems.size() > 256) {
            throw new IllegalArgumentException("Got " + pItems.size() + " items, but maximum is 256");
        } else {
            this.items = pItems;
            this.hashCode = ItemStack.hashStackList(pItems);
        }
    }

    private QuiverContainerContents(int pSize) {
        this(NonNullList.withSize(pSize, ItemStack.EMPTY));
    }

    private QuiverContainerContents(List<ItemStack> p_332487_) {
        this(p_332487_.size());

        for (int i = 0; i < p_332487_.size(); i++) {
            this.items.set(i, p_332487_.get(i));
        }
    }

    private static QuiverContainerContents fromSlots(List<QuiverContainerContents.Slot> p_334537_) {
        OptionalInt optionalint = p_334537_.stream().mapToInt(QuiverContainerContents.Slot::index).max();

        QuiverContainerContents itemcontainercontents = new QuiverContainerContents(optionalint.getAsInt() + 1);

        for (QuiverContainerContents.Slot itemcontainercontents$slot : p_334537_) {
            itemcontainercontents.items.set(itemcontainercontents$slot.index(), itemcontainercontents$slot.item());
        }

        return itemcontainercontents;
    }

    public static QuiverContainerContents fromItems(List<ItemStack> pItems) {
        QuiverContainerContents itemcontainercontents = new QuiverContainerContents(pItems.size());
        for (int j = 0; j < pItems.size(); j++) {
            itemcontainercontents.items.set(j, pItems.get(j).copy());
        }
        return itemcontainercontents;
    }

    private static int findLastNonEmptySlot(List<ItemStack> pItems) {
        for (int i = pItems.size() - 1; i >= 0; i--) {
            if (!pItems.get(i).isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    private List<QuiverContainerContents.Slot> asSlots() {
        List<QuiverContainerContents.Slot> list = new ArrayList<>();

        for (int i = 0; i < this.items.size(); i++) {
            ItemStack itemstack = this.items.get(i);
            list.add(new QuiverContainerContents.Slot(i, itemstack));
        }

        return list;
    }

    public void copyInto(NonNullList<ItemStack> pList) {
        for (int i = 0; i < pList.size(); i++) {
            ItemStack itemstack = i < this.items.size() ? this.items.get(i) : ItemStack.EMPTY;
            pList.set(i, itemstack.copy());
        }
    }

    public ItemStack copyOne() {
        return this.items.isEmpty() ? ItemStack.EMPTY : this.items.get(0).copy();
    }

    public Stream<ItemStack> stream() {
        return this.items.stream().map(ItemStack::copy);
    }


    public NonNullList<ItemStack> items() {
        return this.items;
    }

    public Iterable<ItemStack> copy() {
        return Iterables.transform(this.items(), ItemStack::copy);
    }

    @Override
    public boolean equals(Object pOther) {
        if (this == pOther) {
            return true;
        } else {
            if (pOther instanceof QuiverContainerContents itemcontainercontents && ItemStack.listMatches(this.items, itemcontainercontents.items)) {
                return true;
            }

            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    static record Slot(int index, ItemStack item) {
        public static final Codec<QuiverContainerContents.Slot> CODEC = RecordCodecBuilder.create(
                p_327964_ -> p_327964_.group(
                                Codec.intRange(0, 255).fieldOf("slot").forGetter(QuiverContainerContents.Slot::index),
                                ItemStack.CODEC.fieldOf("item").forGetter(QuiverContainerContents.Slot::item)
                        )
                        .apply(p_327964_, QuiverContainerContents.Slot::new)
        );
    }
}