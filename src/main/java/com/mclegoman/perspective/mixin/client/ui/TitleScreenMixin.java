/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.ui;

import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.ui.UIBackground;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = TitleScreen.class)
public abstract class TitleScreenMixin {
	@Inject(method = "renderPanoramaBackground", at = @At(value = "HEAD"), cancellable = true)
	private void perspective$disablePanorama(DrawContext context, float delta, CallbackInfo ci) {
		if (ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "title_screen") == "dirt") ci.cancel();
	}
	@Inject(method = "renderBackground", at = @At(value = "TAIL"))
	private void perspective$renderBackground(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "title_screen").equals("dirt")) UIBackground.Legacy.renderMenu(new DrawContext(ClientData.CLIENT, ClientData.CLIENT.getBufferBuilders().getEntityVertexConsumers()));
	}
}