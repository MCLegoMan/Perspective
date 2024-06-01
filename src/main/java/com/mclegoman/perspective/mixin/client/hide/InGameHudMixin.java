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
import net.minecraft.client.render.RenderTickCounter;
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
	@Shadow
	@Final
	private static Identifier CROSSHAIR_ATTACK_INDICATOR_FULL_TEXTURE;

	@Shadow
	@Final
	private static Identifier CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_TEXTURE;

	@Shadow
	@Final
	private static Identifier CROSSHAIR_ATTACK_INDICATOR_PROGRESS_TEXTURE;

	@Inject(at = @At("HEAD"), method = "renderCrosshair", cancellable = true)
	private void perspective$renderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		if (ClientData.minecraft.world != null) {
			if ((ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_crosshair").equals("true")) || (ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_crosshair").equals("dynamic"))) {
				HitResult crosshairTarget = ClientData.minecraft.crosshairTarget;
				boolean hide_crosshair = (ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_crosshair").equals("true"));
				if (crosshairTarget != null) {
					if ((ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_crosshair").equals("dynamic")))
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
										context.drawGuiTexture(CROSSHAIR_ATTACK_INDICATOR_FULL_TEXTURE, k, j, 16, 16);
									} else if (cooldownProgress < 1.0F) {
										int l = (int) (cooldownProgress * 17.0F);
										context.drawGuiTexture(CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_TEXTURE, k, j, 16, 4);
										context.drawGuiTexture(CROSSHAIR_ATTACK_INDICATOR_PROGRESS_TEXTURE, 16, 4, 0, 0, k, j, l, 4);
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