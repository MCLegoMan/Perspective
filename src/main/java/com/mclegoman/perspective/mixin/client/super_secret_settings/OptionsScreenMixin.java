/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.super_secret_settings;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.translation.Translation;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
		if ((boolean) ConfigHelper.getConfig("super_secret_settings_options_screen") && ClientData.CLIENT.world != null) this.addDrawableChild(ButtonWidget.builder(Translation.getConfigTranslation("shaders", new Formatting[]{Shader.getRandomColor()}), (button) -> Shader.cycle(true, !shouldReverse, true, false, false)).dimensions(this.width / 2 + 5, this.height / 6 + 17, 150, 20).build());
	}
	@Unique
	private boolean shouldReverse;
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		shouldReverse = ClientData.CLIENT.options.sneakKey.isPressed();
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
}