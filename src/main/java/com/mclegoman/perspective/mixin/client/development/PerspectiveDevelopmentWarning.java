/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.development;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.client.screen.PerspectiveDevelopmentWarningScreen;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class PerspectiveDevelopmentWarning {
    private static boolean DEVELOPMENT_WARNING;
    @Shadow public abstract void setScreen(@Nullable Screen screen);
    @Inject(method="setScreen", at=@At("RETURN"), cancellable = true)
    private void perspective$setScreen(Screen screen, CallbackInfo ci) {
        if (PerspectiveData.IS_DEVELOPMENT && !DEVELOPMENT_WARNING) {
            DEVELOPMENT_WARNING = true;
            if (PerspectiveConfig.SHOW_DEVELOPMENT_WARNING && screen instanceof TitleScreen) {
                this.setScreen(new PerspectiveDevelopmentWarningScreen(screen, 200, true));
                ci.cancel();
            }
        }
    }
}