/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.hud;

import com.mclegoman.perspective.client.april_fools_prank.AprilFoolsPrank;
import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.overlays.HUDOverlays;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.shaders.ShaderDataLoader;
import com.mclegoman.perspective.client.shaders.ShaderRegistryValue;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.HUD;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(priority = 10000, value = InGameHud.class)
public abstract class InGameHudMixin {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void perspective$render(DrawContext context, float tickDelta, CallbackInfo ci) {
        try {
            if (HUD.shouldHideHUD()) ci.cancel();
            float h = HUDOverlays.REMAINING - tickDelta;
            int l = (int)(h * 255.0F / 20.0F);
            if (l > 255) l = 255;
            if (l > 10) {
                context.getMatrices().push();
                context.getMatrices().translate((float)(ClientData.CLIENT.getWindow().getScaledWidth() / 2), 27, 0.0F);
                int k = 16777215;
                int m = l << 24 & -16777216;
                int n = ClientData.CLIENT.textRenderer.getWidth(HUDOverlays.MESSAGE);
                context.drawTextWithShadow(ClientData.CLIENT.textRenderer, HUDOverlays.MESSAGE, -n / 2, -4, k | m);
                context.getMatrices().pop();
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to InGameHUD$render.", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    @Inject(at = @At("RETURN"), method = "render")
    private void perspective$renderOverlays(DrawContext context, float tickDelta, CallbackInfo ci) {
        try {
            if (!HUD.shouldHideHUD()) {
                if (!ClientData.CLIENT.getDebugHud().shouldShowDebugHud()) {
                    if (HUD.DEBUG) {
                        context.fill(1, 1, ClientData.CLIENT.getWindow().getScaledWidth() - 1, ClientData.CLIENT.getWindow().getScaledHeight() - 32, -1873784752);
                        int y = 2;
                        int x = 2;
                        List<Text> debugText = new ArrayList<>();
                        debugText.add(Text.literal(Data.PERSPECTIVE_VERSION.getName() + " " + Data.PERSPECTIVE_VERSION.getFriendlyString()));
                        debugText.add(Text.literal(""));
                        debugText.add(Text.literal("isAprilFools(): " + AprilFoolsPrank.isAprilFools()));
                        debugText.addAll(ConfigHelper.getDebugConfigText());
                        for (Text text : debugText) {
                            if (y > ClientData.CLIENT.getWindow().getScaledHeight() - 2 - 32 - 9) {
                                y = 2;
                                x += 256;
                            }
                            context.drawTextWithShadow(ClientData.CLIENT.textRenderer, text, x, y, 0xffffff);
                            y = HUD.addY(y);
                        }
                    } else {
                        if ((boolean) ConfigHelper.getConfig("version_overlay")) context.drawTextWithShadow(ClientData.CLIENT.textRenderer, Translation.getTranslation("version_overlay", new Object[]{SharedConstants.getGameVersion().getName()}), 2, 2, 0xffffff);
                    }
                }
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to InGameHUD$renderOverlays.", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    @Inject(at = @At("HEAD"), method = "renderCrosshair", cancellable = true)
    private void perspective$renderCrosshair(DrawContext context, CallbackInfo ci) {
        try {
            if (((boolean) ConfigHelper.getConfig("hide_crosshair")) || (Shader.shouldRenderShader() && (boolean) Objects.requireNonNull(ShaderDataLoader.get((int) ConfigHelper.getConfig("super_secret_settings"), ShaderRegistryValue.HIDE_CROSSHAIR)))) {
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