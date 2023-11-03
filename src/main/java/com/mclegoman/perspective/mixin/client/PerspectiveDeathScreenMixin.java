/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public class PerspectiveDeathScreenMixin {
	@Inject(method = "render", at = @At("TAIL"))
	private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
		if ((boolean) PerspectiveConfigHelper.getConfig("show_death_coordinates")) context.drawCenteredTextWithShadow(PerspectiveClientData.CLIENT.textRenderer, Text.translatable("gui.perspective.show_death_coordinates.message", PerspectiveClientData.CLIENT.player.getBlockX(), PerspectiveClientData.CLIENT.player.getBlockY(), PerspectiveClientData.CLIENT.player.getBlockZ()), PerspectiveClientData.CLIENT.getWindow().getScaledWidth() / 2, 115, 16777215);
	}
}
