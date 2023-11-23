/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.hide;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hud.DebugHUD;
import com.mclegoman.perspective.client.hud.HUD;
import com.mclegoman.perspective.client.overlays.HUDOverlays;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.shaders.ShaderDataLoader;
import com.mclegoman.perspective.client.shaders.ShaderRegistryValue;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(priority = 10000, value = InGameHud.class)
public abstract class InGameHudMixin {
    @Inject(at = @At("HEAD"), method = "renderCrosshair", cancellable = true)
    private void perspective$renderCrosshair(DrawContext context, CallbackInfo ci) {
        try {
            if (((boolean) ConfigHelper.getConfig("hide_crosshair"))) {
                if (ClientData.CLIENT.player != null) {
                    if (ClientData.CLIENT.options.getAttackIndicator().getValue() == AttackIndicator.CROSSHAIR) {
                        float cooldownProgress = ClientData.CLIENT.player.getAttackCooldownProgress(0.0F);
                        boolean cooldownProgressFull = false;
                        if (ClientData.CLIENT.targetedEntity instanceof LivingEntity && cooldownProgress >= 1.0F) {
                            cooldownProgressFull = ClientData.CLIENT.player.getAttackCooldownProgressPerTick() > 5.0F;
                            cooldownProgressFull &= ClientData.CLIENT.targetedEntity.isAlive();
                        }
                        int x = ClientData.CLIENT.getWindow().getScaledWidth() / 2 - 8;
                        int y = ClientData.CLIENT.getWindow().getScaledHeight() / 2 - 7 + 16;
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
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to InGameHUD$renderCrosshair.", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
}