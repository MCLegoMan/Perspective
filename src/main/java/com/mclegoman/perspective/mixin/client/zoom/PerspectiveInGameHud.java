/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.zoom;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.perspective.PerspectivePerspective;
import com.mclegoman.perspective.client.zoom.PerspectiveZoom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class PerspectiveInGameHud {
    @Shadow @Final private MinecraftClient client;
    @Inject(method="render", at=@At("HEAD"), cancellable = true)
    private void renderHUD(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (PerspectiveZoom.OVERLAY > 0) {
            MultilineText.create(client.textRenderer, Text.translatable("gui.perspective.overlay.zoom", Text.literal((int)PerspectiveConfigHelper.getConfig("zoom_level") + "%")), context.getScaledWindowWidth() / 2 - 100).drawCenterWithShadow(context, context.getScaledWindowWidth() / 2, 16, 9, 0xFFFFFF);
        }
        if ((PerspectiveZoom.isZooming() || PerspectivePerspective.isHoldingPerspective()) && (boolean)PerspectiveConfigHelper.getConfig("hide_hud")) ci.cancel();
    }
}