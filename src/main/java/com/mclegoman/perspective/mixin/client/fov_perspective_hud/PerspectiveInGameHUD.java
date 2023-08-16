/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.fov_perspective_hud;

import com.mclegoman.perspective.client.util.PerspectiveHideHUD;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import net.minecraft.entity.JumpingMount;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = InGameHud.class)
public abstract class PerspectiveInGameHUD {
    @Inject(at = @At("HEAD"), method = "renderCrosshair", cancellable = true)
    private void perspective$renderCrosshair(DrawContext context, CallbackInfo ci) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to HideHUD$renderCrosshair.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(at = @At("HEAD"), method = "renderHeldItemTooltip", cancellable = true)
    private void perspective$renderHeldItemTooltip(DrawContext context, CallbackInfo ci) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to HideHUD$renderHeldItemTooltip.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(at = @At("HEAD"), method = "renderExperienceBar", cancellable = true)
    private void perspective$renderExperienceBar(DrawContext context, int x, CallbackInfo ci) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to HideHUD$renderExperienceBar.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(at = @At("HEAD"), method = "renderMountHealth", cancellable = true)
    private void perspective$renderMountHealth(DrawContext context, CallbackInfo ci) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to HideHUD$renderMountHealth.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(at = @At("HEAD"), method = "renderDemoTimer", cancellable = true)
    private void perspective$renderDemoTimer(DrawContext context, CallbackInfo ci) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to HideHUD$renderDemoTimer.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(at = @At("HEAD"), method = "renderHealthBar", cancellable = true)
    private void perspective$renderHealthBar(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to HideHUD$renderHealthBar.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(at = @At("HEAD"), method = "renderMountJumpBar", cancellable = true)
    private void perspective$renderMountJumpBar(JumpingMount mount, DrawContext context, int x, CallbackInfo ci) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to HideHUD$renderMountJumpBar.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(at = @At("HEAD"), method = "renderStatusBars", cancellable = true)
    private void perspective$renderStatusBars(DrawContext context, CallbackInfo ci) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to HideHUD$renderStatusBars.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(at = @At("HEAD"), method = "renderAutosaveIndicator", cancellable = true)
    private void perspective$renderAutosaveIndicator(DrawContext context, CallbackInfo ci) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to HideHUD$renderAutosaveIndicator.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(at = @At("HEAD"), method = "renderHotbar", cancellable = true)
    private void perspective$renderHotbar(float tickDelta, DrawContext context, CallbackInfo ci) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to HideHUD$renderHotbar.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(at = @At("HEAD"), method = "renderHotbarItem", cancellable = true)
    private void perspective$renderHotbarItem(DrawContext context, int x, int y, float f, PlayerEntity player, ItemStack stack, int seed, CallbackInfo ci) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to HideHUD$renderHotbarItem.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(at = @At("HEAD"), method = "renderScoreboardSidebar", cancellable = true)
    private void perspective$renderScoreboardSidebar(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to HideHUD$renderScoreboardSidebar.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(at = @At("HEAD"), method = "renderStatusEffectOverlay", cancellable = true)
    private void perspective$renderStatusEffectOverlay(DrawContext context, CallbackInfo ci) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to HideHUD$renderStatusEffectOverlay.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(at = @At("HEAD"), method = "renderVignetteOverlay", cancellable = true)
    private void perspective$renderVignetteOverlay(DrawContext context, Entity entity, CallbackInfo ci) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to HideHUD$renderVignetteOverlay.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(at = @At("HEAD"), method = "renderSpyglassOverlay", cancellable = true)
    private void perspective$renderSpyglassOverlay(DrawContext context, float scale, CallbackInfo ci) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to HideHUD$renderVignetteOverlay.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
}