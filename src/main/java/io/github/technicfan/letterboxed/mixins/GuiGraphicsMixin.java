package io.github.technicfan.letterboxed.mixins;

import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphics;
import io.github.technicfan.letterboxed.Letterboxed;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
    @Shadow
    private Matrix3x2fStack pose;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void translate(CallbackInfo ci) {
        pose.translate(Letterboxed.leftOff(), Letterboxed.topOff());
    }
}
