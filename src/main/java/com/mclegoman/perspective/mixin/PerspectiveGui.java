package com.mclegoman.perspective.mixin;

import com.mclegoman.perspective.util.PerspectiveUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class PerspectiveGui {
    @Shadow public abstract TextRenderer getTextRenderer();
    private static int ZOOM_FADE = 0;
    @Inject(method="render", at=@At("HEAD"))
    private void renderHUD(DrawContext context, float tickDelta, CallbackInfo ci) {
        int ZOOM_AMOUNT = 100 - PerspectiveUtils.ZOOM_LEVEL;
        Text ZOOM_OVERLAY = Text.literal("Zoom Level: " + ZOOM_AMOUNT + "%");
        if (PerspectiveUtils.KEY_ZOOM_IN.isPressed() || PerspectiveUtils.KEY_ZOOM_OUT.isPressed()) ZOOM_FADE = 180;
        else if (ZOOM_FADE > 0) ZOOM_FADE = ZOOM_FADE -1;
        if (ZOOM_FADE > 0) context.drawTextWithShadow(this.getTextRenderer(), ZOOM_OVERLAY, 10, context.getScaledWindowHeight() - 10, 0xFFAA00);
    }
}