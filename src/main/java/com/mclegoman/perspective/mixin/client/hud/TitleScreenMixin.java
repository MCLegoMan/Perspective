/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.hud;

import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
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
	private void perspective$renderPanoramaBackground(DrawContext context, float delta, CallbackInfo ci) {
		if ((boolean) ConfigHelper.getConfig("dirt_title_screen")) {
			context.setShaderColor(0.25F, 0.25F, 0.25F, 1.0F);
			context.drawTexture(new Identifier("textures/block/dirt.png"), 0, 0, 0, 0.0F, 0.0F, ClientData.minecraft.getWindow().getScaledWidth(), ClientData.minecraft.getWindow().getScaledHeight(), 32, 32);
			context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			ci.cancel();
		}
	}
}