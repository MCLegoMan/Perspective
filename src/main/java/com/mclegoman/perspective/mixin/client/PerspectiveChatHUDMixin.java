/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client;

import com.mclegoman.perspective.client.util.PerspectiveHideHUD;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = ChatHud.class)
public abstract class PerspectiveChatHUDMixin {
    @Inject(at = @At("RETURN"), method = "isChatHidden", cancellable = true)
    private void perspective$isChatHidden(CallbackInfoReturnable<Boolean> cir) {
        if (PerspectiveHideHUD.shouldHideHUD()) cir.setReturnValue(true);
    }
}