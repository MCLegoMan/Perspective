/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.fov_perspective_hud;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mclegoman.perspective.client.util.PerspectiveHideHUD;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = ChatHud.class)
public abstract class PerspectiveChatHUD {
    @ModifyReturnValue(at = @At("RETURN"), method = "isChatHidden")
    private boolean perspective$isChatHidden(boolean isChatHidden) {
        return PerspectiveHideHUD.shouldHideHUD() || isChatHidden;
    }
}