package io.github.technicfan.letterboxed.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import io.github.technicfan.letterboxed.Letterboxed;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {
    @ModifyVariable(method = "renderEntityInInventoryFollowsMouse", at = @At("HEAD"), ordinal = 0)
    private static int adjustX1(int x1) {
        return x1 + Letterboxed.leftOff();
    }

    @ModifyVariable(method = "renderEntityInInventoryFollowsMouse", at = @At("HEAD"), ordinal = 2)
    private static int adjustX2(int x2) {
        return x2 + Letterboxed.leftOff();
    }

    @ModifyVariable(method = "renderEntityInInventoryFollowsMouse", at = @At("HEAD"), ordinal = 1)
    private static int adjustY1(int y1) {
        return y1 + Letterboxed.topOff();
    }

    @ModifyVariable(method = "renderEntityInInventoryFollowsMouse", at = @At("HEAD"), ordinal = 3)
    private static int adjustY2(int y2) {
        return y2 + Letterboxed.topOff();
    }

    @ModifyVariable(method = "renderEntityInInventoryFollowsMouse", at = @At("HEAD"), ordinal = 1)
    private static float adjustMouseX(float mouseX) {
        return mouseX + Letterboxed.leftOff();
    }

    @ModifyVariable(method = "renderEntityInInventoryFollowsMouse", at = @At("HEAD"), ordinal = 2)
    private static float adjustMouseY(float mouseY) {
        return mouseY + Letterboxed.topOff();
    }
}
