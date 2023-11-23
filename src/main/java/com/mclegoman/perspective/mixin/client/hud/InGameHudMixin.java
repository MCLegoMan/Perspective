/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.hud;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hud.DebugHUD;
import com.mclegoman.perspective.client.overlays.HUDOverlays;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.shaders.ShaderDataLoader;
import com.mclegoman.perspective.client.shaders.ShaderRegistryValue;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.hud.HUD;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(priority = 10000, value = InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
    @Shadow private int scaledHeight;

    @Shadow private int scaledWidth;

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void perspective$render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        try {
            if (HUD.shouldHideHUD()) ci.cancel();
            float h = HUDOverlays.REMAINING - tickDelta;
            int l = (int)(h * 255.0F / 20.0F);
            if (l > 255) l = 255;
            if (l > 10) {
                matrices.push();
                matrices.translate((float)(ClientData.CLIENT.getWindow().getScaledWidth() / 2), 27, 0.0F);
                int k = 16777215;
                int m = l << 24 & -16777216;
                int n = ClientData.CLIENT.textRenderer.getWidth(HUDOverlays.MESSAGE);
                ClientData.CLIENT.textRenderer.drawWithShadow(matrices, HUDOverlays.MESSAGE, ((float) -n / 2), -4, k | m);
                matrices.pop();
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to InGameHUD$render.", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    @Inject(at = @At("RETURN"), method = "render")
    private void perspective$renderOverlays(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        try {
            if (!HUD.shouldHideHUD()) {
                if (!ClientData.CLIENT.options.debugEnabled) {
                    if (!DebugHUD.debugType.equals(DebugHUD.Type.NONE)) {
                        DebugHUD.renderDebugHUD(matrices);
                    }
                    if ((boolean) ConfigHelper.getConfig("version_overlay")) ClientData.CLIENT.textRenderer.drawWithShadow(matrices, Translation.getTranslation("version_overlay", new Object[]{SharedConstants.getGameVersion().getName()}), 2, 2, 0xffffff);
                }
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to InGameHUD$renderOverlays.", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    @Inject(at = @At("HEAD"), method = "renderCrosshair", cancellable = true)
    private void perspective$renderCrosshair(MatrixStack matrices, CallbackInfo ci) {
        try {
            if (((boolean) ConfigHelper.getConfig("hide_crosshair")) || (Shader.shouldRenderShader() && (boolean) Objects.requireNonNull(ShaderDataLoader.get((int) ConfigHelper.getConfig("super_secret_settings"), ShaderRegistryValue.HIDE_CROSSHAIR)))) {
                if (ClientData.CLIENT.player != null) {
                    if (ClientData.CLIENT.options.getAttackIndicator().getValue() == AttackIndicator.CROSSHAIR) {
                        float f = ClientData.CLIENT.player.getAttackCooldownProgress(0.0F);
                        boolean bl = false;
                        if (ClientData.CLIENT.targetedEntity != null && ClientData.CLIENT.targetedEntity instanceof LivingEntity && f >= 1.0F) {
                            bl = ClientData.CLIENT.player.getAttackCooldownProgressPerTick() > 5.0F;
                            bl &= ClientData.CLIENT.targetedEntity.isAlive();
                        }

                        int j = this.scaledHeight / 2 - 7 + 16;
                        int k = this.scaledWidth / 2 - 8;
                        if (bl) {
                            this.drawTexture(matrices, k, j, 68, 94, 16, 16);
                        } else if (f < 1.0F) {
                            int l = (int)(f * 17.0F);
                            this.drawTexture(matrices, k, j, 36, 94, 16, 4);
                            this.drawTexture(matrices, k, j, 52, 94, l, 4);
                        }
                    }
                    ci.cancel();
                }
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to InGameHUD$renderCrosshair.", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
}