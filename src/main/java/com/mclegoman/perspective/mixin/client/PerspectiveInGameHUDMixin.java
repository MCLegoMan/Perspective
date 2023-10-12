/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.overlays.PerspectiveHUDOverlays;
import com.mclegoman.perspective.client.util.PerspectiveHideHUD;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = InGameHud.class)
public abstract class PerspectiveInGameHUDMixin {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void perspective$render(DrawContext context, float tickDelta, CallbackInfo ci) {
        try {
            float h = PerspectiveHUDOverlays.REMAINING - tickDelta;
            int l = (int)(h * 255.0F / 20.0F);
            if (l > 255) l = 255;
            if (l > 10) {
                context.getMatrices().push();
                context.getMatrices().translate((float)(PerspectiveClientData.CLIENT.getWindow().getScaledWidth() / 2), 27, 0.0F);
                int k = 16777215;
                int m = l << 24 & -16777216;
                int n = PerspectiveClientData.CLIENT.textRenderer.getWidth(PerspectiveHUDOverlays.MESSAGE);
                context.drawTextWithShadow(PerspectiveClientData.CLIENT.textRenderer, PerspectiveHUDOverlays.MESSAGE, -n / 2, -4, k | m);
                context.getMatrices().pop();
            }
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to InGameHUD$render.", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    @Inject(at = @At("HEAD"), method = "renderCrosshair", cancellable = true)
    private void perspective$renderCrosshair(DrawContext context, CallbackInfo ci) {
        try {
            if ((boolean) PerspectiveConfigHelper.getConfig("hide_crosshair")) {
                if (PerspectiveClientData.CLIENT.player != null) {
                    if (PerspectiveClientData.CLIENT.options.getAttackIndicator().getValue() == AttackIndicator.CROSSHAIR) {
                        float cooldownProgress = PerspectiveClientData.CLIENT.player.getAttackCooldownProgress(0.0F);
                        boolean cooldownProgressFull = false;
                        if (PerspectiveClientData.CLIENT.targetedEntity instanceof LivingEntity && cooldownProgress >= 1.0F) {
                            cooldownProgressFull = PerspectiveClientData.CLIENT.player.getAttackCooldownProgressPerTick() > 5.0F;
                            cooldownProgressFull &= PerspectiveClientData.CLIENT.targetedEntity.isAlive();
                        }
                        int x = PerspectiveClientData.CLIENT.getWindow().getScaledWidth() / 2 - 8;
                        int y = PerspectiveClientData.CLIENT.getWindow().getScaledHeight() / 2 - 7 + 16;
                        if (cooldownProgressFull) {
                            context.drawGuiTexture(new Identifier("hud/crosshair_attack_indicator_full"), x, y, 16, 16);
                        } else if (cooldownProgress < 1.0F) {
                            context.drawGuiTexture(new Identifier("hud/crosshair_attack_indicator_background"), x, y, 16, 4);
                            context.drawGuiTexture(new Identifier("hud/crosshair_attack_indicator_progress"), 16, 4, 0, 0, x, y, (int) (cooldownProgress * 17.0F), 4);
                        }
                    }
                    ci.cancel();
                }
            }
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to InGameHUD$renderCrosshair.", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
}