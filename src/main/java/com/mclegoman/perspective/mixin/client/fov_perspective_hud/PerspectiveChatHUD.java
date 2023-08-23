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
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = ChatHud.class)
public abstract class PerspectiveChatHUD {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void perspective$renderChat(DrawContext context, int currentTick, int mouseX, int mouseY, CallbackInfo ci) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to HideHUD$renderChat.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
}