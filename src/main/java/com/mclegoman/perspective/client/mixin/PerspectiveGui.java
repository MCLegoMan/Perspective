/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.mixin;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.client.util.PerspectiveZoomUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
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
    private void renderHUD(MatrixStack context, float tickDelta, CallbackInfo ci) {
        if (PerspectiveZoomUtils.OVERLAY > 0) MultilineText.create(client.textRenderer, Text.translatable("overlay.perspective.zoom", Text.literal((100 - PerspectiveConfig.ZOOM_LEVEL) + "%")), client.getWindow().getScaledWidth() / 2 - 100).drawCenterWithShadow(context, client.getWindow().getScaledWidth() / 2, 16, 9, 0xFFFFFF);
    }
}