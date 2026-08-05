package io.github.technicfan.letterboxed.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.MouseHandler;
import io.github.technicfan.letterboxed.Letterboxed;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Shadow
    private double xpos, ypos;

    @Inject(method = "Lnet/minecraft/client/MouseHandler;getScaledXPos(Lcom/mojang/blaze3d/platform/Window;)D", at = @At("HEAD"), cancellable = true)
    private void xpos(CallbackInfoReturnable<Double> cir, @Local Window w) {
        cir.setReturnValue(MouseHandler.getScaledXPos(w, xpos - Letterboxed.leftFull()));
        cir.cancel();
    }

    @Inject(method = "Lnet/minecraft/client/MouseHandler;getScaledYPos(Lcom/mojang/blaze3d/platform/Window;)D", at = @At("HEAD"), cancellable = true)
    private void ypos(CallbackInfoReturnable<Double> cir, @Local Window w) {
        cir.setReturnValue(MouseHandler.getScaledXPos(w, ypos - Letterboxed.topFull()));
        cir.cancel();
    }

    @Redirect(method = {"grabMouse", "releaseMouse"}, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;getScreenHeight()I"))
    private int realHeight(Window w) {
        return Letterboxed.height;
    }

    @Redirect(method = {"grabMouse", "releaseMouse"}, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;getScreenWidth()I"))
    private int realWidth(Window w) {
        return Letterboxed.width;
    }
}
