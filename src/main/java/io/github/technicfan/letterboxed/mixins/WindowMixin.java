package io.github.technicfan.letterboxed.mixins;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.platform.Window;

import io.github.technicfan.letterboxed.Letterboxed;

@Mixin(Window.class)
public final class WindowMixin {
    @Shadow
    private long handle;
    @Shadow
    private int guiScale, guiScaledWidth, guiScaledHeight, framebufferHeight, framebufferWidth;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        Letterboxed.fakeWidth = framebufferWidth;
        Letterboxed.fakeHeight = framebufferHeight;
        Letterboxed.guiScaledWidth = guiScaledWidth;
        Letterboxed.guiScaledHeight = guiScaledHeight;
    }

    @Inject(method = {"getScreenWidth"}, at = @At("TAIL"), cancellable = true)
    private void overrideWidth(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Letterboxed.fakeWidth);
        cir.cancel();
    }

    @Inject(method = {"getGuiScaledWidth"}, at = @At("TAIL"), cancellable = true)
    private void overrideGuiWidth(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Letterboxed.fakeGuiWidth);
        cir.cancel();
    }

    @Inject(method = {"getScreenHeight"}, at = @At("TAIL"), cancellable = true)
    private void overrideHeight(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Letterboxed.fakeHeight);
        cir.cancel();
    }

    @Inject(method = {"getGuiScaledHeight"}, at = @At("TAIL"), cancellable = true)
    private void overrideGuiHeight(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Letterboxed.fakeGuiHeight);
        cir.cancel();
    }

    @Inject(method = "setGuiScale", at = @At("TAIL"))
    private void applyScale(CallbackInfo ci) {
        Letterboxed.guiScale = guiScale;
        Letterboxed.guiScaledHeight = guiScaledHeight;
        Letterboxed.guiScaledWidth = guiScaledWidth;
        Letterboxed.applyScale(handle);
    }

    @Redirect(method = {"onFramebufferResize", "refreshFramebufferSize"}, at = @At(value = "FIELD", target = "Lcom/mojang/blaze3d/platform/Window;framebufferHeight:I", opcode = Opcodes.PUTFIELD))
    private void framebufferHeight(Window w, int framebufferHeight) {
        this.framebufferHeight = framebufferHeight;
        Letterboxed.height = framebufferHeight;
        Letterboxed.fixRatio(handle);
    }

    @Redirect(method = {"onFramebufferResize", "refreshFramebufferSize"}, at = @At(value = "FIELD", target = "Lcom/mojang/blaze3d/platform/Window;framebufferWidth:I", opcode = Opcodes.PUTFIELD))
    private void framebufferWidth(Window w, int framebufferWidth) {
        this.framebufferWidth = framebufferWidth;
        Letterboxed.width = framebufferWidth;
        Letterboxed.fixRatio(handle);
    }
}
