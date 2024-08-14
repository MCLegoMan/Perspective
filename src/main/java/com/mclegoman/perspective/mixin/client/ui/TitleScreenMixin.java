/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.ui;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.ui.UIBackground;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = TitleScreen.class)
public abstract class TitleScreenMixin {
	@Inject(method = "renderPanoramaBackground", at = @At(value = "HEAD"), cancellable = true)
	private void perspective$disablePanorama(DrawContext context, float delta, CallbackInfo ci) {
		if (ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "title_screen").equals("dirt")) {
			RenderSystem.enableBlend();
			context.drawTexture(UIBackground.getUiBackgroundTextureFromConfig(), 0, 0, 0, 0.0F, 0.0F, ClientData.minecraft.getWindow().getScaledWidth(), ClientData.minecraft.getWindow().getScaledHeight(), 32, 32);
			context.drawTexture(Identifier.of(Data.version.getID(), "textures/gui/uibackground_menu_background.png"), 0, 0, 0, 0.0F, 0.0F, ClientData.minecraft.getWindow().getScaledWidth(), ClientData.minecraft.getWindow().getScaledHeight(), 32, 32);
			RenderSystem.disableBlend();
			ci.cancel();
		}
	}
}