package io.github.technicfan.letterboxed.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.MouseHandler;
import io.github.technicfan.letterboxed.Letterboxed;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @ModifyVariable(method = "getScaledYPos", at = @At("HEAD"))
    private static double fixScaledY(double ypos) {
        return ypos - Letterboxed.heightOff;
    }

    @ModifyVariable(method = "getScaledXPos", at = @At("HEAD"))
    private static double fixScaledX(double xpos) {
        return xpos - Letterboxed.widthOff;
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
