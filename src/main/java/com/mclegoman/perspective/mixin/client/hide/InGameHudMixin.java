/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.hide;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 10000, value = InGameHud.class)
public abstract class InGameHudMixin {
    @Inject(at = @At("HEAD"), method = "renderCrosshair", cancellable = true)
    private void perspective$renderCrosshair(DrawContext context, CallbackInfo ci) {
        if (ClientData.CLIENT.world != null) {
            if ((ConfigHelper.getConfig("hide_crosshair").equals("true")) || (ConfigHelper.getConfig("hide_crosshair").equals("dynamic"))) {
                HitResult crosshairTarget = ClientData.CLIENT.crosshairTarget;
                boolean hide_crosshair = (ConfigHelper.getConfig("hide_crosshair").equals("true"));
                if (crosshairTarget != null) {
                    if ((ConfigHelper.getConfig("hide_crosshair").equals("dynamic"))) hide_crosshair = (crosshairTarget.getType() == HitResult.Type.BLOCK) ? ClientData.CLIENT.world.getBlockState(((BlockHitResult)crosshairTarget).getBlockPos()).isAir() : crosshairTarget.getType() != HitResult.Type.ENTITY;
                    if (hide_crosshair) {
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
                                    context.drawTexture(new Identifier("hud/crosshair_attack_indicator_full"), x, y, 0, 0, 16, 16);
                                } else if (cooldownProgress < 1.0F) {
                                    context.drawTexture(new Identifier("hud/crosshair_attack_indicator_background"), x, y, 0, 0, 16, 4);
                                    context.drawTexture(new Identifier("hud/crosshair_attack_indicator_progress"), 16, 4, 0, 0, x, y, (int) (cooldownProgress * 17.0F), 4);
                                }
                            }
                            ci.cancel();
                        }
                    }
                }
            }
        }
    }
}