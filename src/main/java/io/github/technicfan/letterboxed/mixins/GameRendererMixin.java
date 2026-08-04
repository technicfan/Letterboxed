package io.github.technicfan.letterboxed.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

//? if 26.2
/*import net.minecraft.client.gui.Gui;*/
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import io.github.technicfan.letterboxed.Letterboxed;

//? if <26.2 {
@Mixin(GameRenderer.class)
//?} else
/*@Mixin(Gui.class)*/
public class GameRendererMixin {
    //? if 1.21.11 {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/GuiRenderer;render(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V", shift = At.Shift.BEFORE))
    //?} else if 26.1 {
    /*@Inject(method = "extractGui", at = @At("TAIL"))*/
    //?} else if 26.2
    /*@Inject(method = "extractRenderState", at = @At("TAIL"))*/
    private void drawLetterboxes(CallbackInfo ci, @Local GuiGraphics guiGraphics) {
        guiGraphics.pose().pushMatrix().translate(-Letterboxed.leftOff(), -Letterboxed.topOff());
        if (Letterboxed.widthOff != 0) {
            guiGraphics.fill(0, 0, Letterboxed.leftOff(), Letterboxed.guiScaledHeight, 0xff000000);
            guiGraphics.fill(Letterboxed.guiScaledWidth - Letterboxed.rightOff(), 0, Letterboxed.guiScaledWidth, Letterboxed.guiScaledHeight, 0xff000000);
        }
        if (Letterboxed.heightOff != 0) {
            guiGraphics.fill(0, 0, Letterboxed.guiScaledWidth, Letterboxed.topOff(), 0xff000000);
            guiGraphics.fill(0, Letterboxed.guiScaledHeight, Letterboxed.guiScaledWidth, Letterboxed.guiScaledHeight - Letterboxed.bottomOff(), 0xff000000);
        }
        guiGraphics.pose().popMatrix();
    }
}
