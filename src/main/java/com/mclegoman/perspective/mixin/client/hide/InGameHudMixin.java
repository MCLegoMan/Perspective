/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.hide;

import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = InGameHud.class)
public abstract class InGameHudMixin {
	@Shadow @Final private static Identifier ICONS;

	@Inject(at = @At("HEAD"), method = "renderCrosshair", cancellable = true)
	private void perspective$renderCrosshair(DrawContext context, CallbackInfo ci) {
		if (ClientData.minecraft.world != null) {
			if ((ConfigHelper.getConfig("hide_crosshair").equals("true")) || (ConfigHelper.getConfig("hide_crosshair").equals("dynamic"))) {
				HitResult crosshairTarget = ClientData.minecraft.crosshairTarget;
				boolean hide_crosshair = (ConfigHelper.getConfig("hide_crosshair").equals("true"));
				if (crosshairTarget != null) {
					if ((ConfigHelper.getConfig("hide_crosshair").equals("dynamic")))
						hide_crosshair = (crosshairTarget.getType() == HitResult.Type.BLOCK) ? ClientData.minecraft.world.getBlockState(((BlockHitResult) crosshairTarget).getBlockPos()).isAir() : crosshairTarget.getType() != HitResult.Type.ENTITY;
					if (hide_crosshair) {
						if (ClientData.minecraft.options.getAttackIndicator().getValue() == AttackIndicator.CROSSHAIR) {
							if (ClientData.minecraft.player != null) {
								if (!ClientData.minecraft.gameRenderer.isRenderingPanorama()) {
									float cooldownProgress = ClientData.minecraft.player.getAttackCooldownProgress(0.0F);
									boolean cooldownProgressFull = false;
									if (ClientData.minecraft.targetedEntity instanceof LivingEntity && cooldownProgress >= 1.0F) {
										cooldownProgressFull = ClientData.minecraft.player.getAttackCooldownProgressPerTick() > 5.0F;
										cooldownProgressFull &= ClientData.minecraft.targetedEntity.isAlive();
									}
									int j = ClientData.minecraft.getWindow().getScaledHeight() / 2 - 7 + 16;
									int k = ClientData.minecraft.getWindow().getScaledWidth() / 2 - 8;
									if (cooldownProgressFull) {
										context.drawTexture(ICONS, k, j, 68, 94, 16, 16);
									} else if (cooldownProgress < 1.0F) {
										int l = (int)(cooldownProgress * 17.0F);
										context.drawTexture(ICONS, k, j, 36, 94, 16, 4);
										context.drawTexture(ICONS, k, j, 52, 94, l, 4);
									}
								}
							}
						}
						ci.cancel();
					}
				}
			}
		}
	}
}