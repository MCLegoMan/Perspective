package com.mclegoman.perspective.mixin;

import com.mclegoman.perspective.config.PerspectiveConfig;
import com.mclegoman.perspective.data.PerspectiveData;
import com.mclegoman.perspective.util.PerspectiveUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class PerspectiveGui {
    @Shadow public abstract TextRenderer getTextRenderer();
    @Shadow @Final private MinecraftClient client;
    @Inject(method="render", at=@At("HEAD"))
    private void renderHUD(DrawContext context, float tickDelta, CallbackInfo ci) {
        int ZOOM_AMOUNT = 100 - PerspectiveConfig.ZOOM_LEVEL;
        Text ZOOM_OVERLAY = Text.literal("Zoom Level: " + ZOOM_AMOUNT + "%");
        if (PerspectiveUtils.SHOW_OVERLAY > 0) {
            PerspectiveUtils.SHOW_OVERLAY = PerspectiveUtils.SHOW_OVERLAY -1;
            context.drawTextWithShadow(this.getTextRenderer(), ZOOM_OVERLAY, (context.getScaledWindowWidth() / 2) - (client.textRenderer.getWidth(ZOOM_OVERLAY) / 2), 8, 0xFFAA00);
        }
    }
}