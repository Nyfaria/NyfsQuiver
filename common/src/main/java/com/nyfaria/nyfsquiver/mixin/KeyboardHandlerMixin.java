package com.nyfaria.nyfsquiver.mixin;

import com.nyfaria.nyfsquiver.init.KeyBindInit;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
    @Inject(method="keyPress", at=@At("TAIL"))
    public void keyPress(long pWindowPointer, int pKey, int pScanCode, int pAction, int pModifiers, CallbackInfo ci) {
        KeyBindInit.onKeyInput(pKey, pScanCode, pAction, pModifiers);
    }
}
