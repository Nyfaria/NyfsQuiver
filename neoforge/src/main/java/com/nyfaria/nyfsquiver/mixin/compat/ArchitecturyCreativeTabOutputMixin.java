package com.nyfaria.nyfsquiver.mixin.compat;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Compatibility patch for Architectury 13.0.11 (NeoForge).
 * <p>
 * Architectury's {@code CreativeTabOutput#accept(ItemStack, TabVisibility)} is implemented as
 * {@code acceptAfter(ItemStack.EMPTY, ...)}, and its NeoForge implementation forwards the empty
 * anchor straight to {@code BuildCreativeModeTabContentsEvent#insertAfter}, which rejects it:
 * <pre>
 * java.lang.IllegalArgumentException: Itemstack 0 minecraft:air does not exist in tab's list
 * </pre>
 * Every mod that puts its item into a tab through {@code CreativeTabRegistry.modify(...)} therefore
 * crashes the game as soon as a tab is built - for example LitematicaTool 2.0.0, which turns the
 * otherwise harmless vanilla {@code minecraft:tools_and_utilities} tab into a hard crash.
 * <p>
 * Architectury's own code handles the empty anchor by appending the entry, so that is exactly what
 * this patch restores. It is only applied when Architectury is installed: the mixin config is
 * {@code required = false} and targets an Architectury class directly.
 */
@Mixin(targets = "dev.architectury.registry.forge.CreativeTabRegistryImpl$3", remap = false)
public class ArchitecturyCreativeTabOutputMixin {

    @Shadow(remap = false)
    @Final
    private BuildCreativeModeTabContentsEvent val$event;

    @Inject(method = "acceptAfter", at = @At("HEAD"), cancellable = true, remap = false)
    private void nyfsquiver$appendWhenAnchorIsEmpty(ItemStack after, ItemStack stack,
                                                    CreativeModeTab.TabVisibility visibility,
                                                    CallbackInfo ci) {
        if (after.isEmpty()) {
            this.val$event.accept(stack, visibility);
            ci.cancel();
        }
    }
}
