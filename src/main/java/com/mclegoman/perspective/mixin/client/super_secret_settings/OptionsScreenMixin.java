/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.super_secret_settings;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.shaders.ShadersConfigScreen;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 10000, value = OptionsScreen.class)
public class OptionsScreenMixin extends Screen {
	protected OptionsScreenMixin(Text title) {
		super(title);
	}

	@Inject(method = "init", at = @At("TAIL"))
	private void perspective$renderOptions(CallbackInfo ci) {
		try {
			if ((boolean) ConfigHelper.getConfig("super_secret_settings_options_screen")) {
				this.addDrawableChild(ButtonWidget.builder(Translation.getConfigTranslation("shaders"), (button) -> {
					ClientData.CLIENT.setScreen(new ShadersConfigScreen(ClientData.CLIENT.currentScreen, true, false));
				}).dimensions(this.width / 2 + 5, this.height / 6 + 17, 150, 20).build());
			}
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to add super secret settings to the options screen.", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
		}
	}
}