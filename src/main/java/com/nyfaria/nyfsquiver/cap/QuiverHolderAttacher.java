package com.nyfaria.nyfsquiver.cap;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

public class QuiverHolderAttacher extends CapabilityAttacher {
    public static final Capability<QuiverHolder> QUIVER_HOLDER_CAPQUIVER = getCapability(new CapabilityToken<>() {
    });
    public static final ResourceLocation QUIVER_HOLDER_RL = new ResourceLocation(NyfsQuiver.MODID, "quiver_holder");

    @Nullable
    public static QuiverHolder getQuiverHolderUnwrap(ItemStack player) {
        return getQuiverHolder(player).orElse(null);
    }

    public static LazyOptional<QuiverHolder> getQuiverHolder(ItemStack player) {
        return player.getCapability(QUIVER_HOLDER_CAPQUIVER);
    }

    private static void attach(AttachCapabilitiesEvent<ItemStack> event, ItemStack player) {
        if (player.getItem() instanceof QuiverItem) {
            genericAttachCapability(event, new QuiverHolder(player), QUIVER_HOLDER_CAPQUIVER, QUIVER_HOLDER_RL);
        }
    }

    public static void register() {
        CapabilityAttacher.registerAttacher(ItemStack.class, QuiverHolderAttacher::attach, QuiverHolderAttacher::getQuiverHolder);
    }

}
