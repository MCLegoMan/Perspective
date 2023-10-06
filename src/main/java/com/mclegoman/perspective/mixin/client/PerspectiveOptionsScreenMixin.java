/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.screen.config.shaders.PerspectiveShadersConfigScreen;
import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = OptionsScreen.class)
public class PerspectiveOptionsScreenMixin extends Screen {
    protected PerspectiveOptionsScreenMixin(Text title) {
        super(title);
    }
    @Inject(method = "init", at = @At("TAIL"))
    private void perspective$renderOptions(CallbackInfo ci) {
        try {
            if ((boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_options_screen")) {
                this.addDrawableChild(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("shaders"), (button) -> {
                    PerspectiveClientData.CLIENT.setScreen(new PerspectiveShadersConfigScreen(PerspectiveClientData.CLIENT.currentScreen, true, false));
                }).dimensions(this.width / 2 + 5, this.height / 6 + 17, 150, 20).build());
            }
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to add super secret settings to the options screen.", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
}