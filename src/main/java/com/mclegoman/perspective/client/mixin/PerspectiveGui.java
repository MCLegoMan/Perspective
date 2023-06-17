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
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.Reader;

@Mixin(InGameHud.class)
public abstract class PerspectiveGui {
    @Shadow @Final private MinecraftClient client;

    @Shadow public abstract TextRenderer getFontRenderer();

    @Inject(method="render", at=@At("TAIL"))
    private void renderHUD(float tickDelta, CallbackInfo ci) {
        if (PerspectiveZoomUtils.OVERLAY > 0) {
            String TEXT =  (100 - PerspectiveConfig.ZOOM_LEVEL) + "%";
            double WIDTH = new Window(this.client).getScaledWidth();
            getFontRenderer().drawWithShadow(I18n.translate("overlay.perspective.zoom", TEXT), (float)WIDTH / 2 - (float)getFontRenderer().getStringWidth(I18n.translate("overlay.perspective.zoom", TEXT)) / 2, 18, 0xFFAA00);
        }
    }
}