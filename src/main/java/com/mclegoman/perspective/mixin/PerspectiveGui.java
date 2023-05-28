package com.mclegoman.perspective.mixin;

import com.mclegoman.perspective.config.PerspectiveConfig;
import com.mclegoman.perspective.data.PerspectiveData;
import com.mclegoman.perspective.util.PerspectiveUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
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
    @Shadow @Final private MinecraftClient client;
    @Inject(method="render", at=@At("HEAD"))
    private void renderHUD(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (PerspectiveUtils.SHOW_OVERLAY > 0) {
            PerspectiveUtils.SHOW_OVERLAY = PerspectiveUtils.SHOW_OVERLAY -1;
            showOverlay(context, client);
        }
    }
    private static void showOverlay(DrawContext context, MinecraftClient client) {
        MultilineText OVERLAY = MultilineText.create(client.textRenderer, Text.translatable("overlay.perspective.zoom", Text.literal((100 - PerspectiveConfig.ZOOM_LEVEL) + "%")), context.getScaledWindowWidth() / 2 - 100);
        OVERLAY.drawCenterWithShadow(context, context.getScaledWindowWidth() / 2, 16, 9, 0xFFFFFF);
    }
}