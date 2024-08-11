/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.show_death_coordinates;

import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = DeathScreen.class)
public class DeathScreenMixin {
	@Inject(method = "render", at = @At("TAIL"))
	private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "show_death_coordinates") && ClientData.minecraft.player != null) context.drawCenteredTextWithShadow(ClientData.minecraft.textRenderer, Text.translatable("gui.perspective.show_death_coordinates.message", (int) ClientData.minecraft.player.getPos().getX(), (int) ClientData.minecraft.player.getPos().getY(), (int) ClientData.minecraft.player.getPos().getZ()), ClientData.minecraft.getWindow().getScaledWidth() / 2, 115, 16777215);
	}
}
