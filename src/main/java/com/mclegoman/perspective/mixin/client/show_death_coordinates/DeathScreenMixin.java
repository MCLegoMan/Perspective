/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.show_death_coordinates;

import com.mclegoman.perspective.client.hud.Overlays;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DeathScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = DeathScreen.class)
public class DeathScreenMixin {
	@Inject(method = "render", at = @At("TAIL"))
	private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "show_death_coordinates") && ClientData.minecraft.player != null) context.drawCenteredTextWithShadow(ClientData.minecraft.textRenderer, Translation.getTranslation(Data.version.getID(), "show_death_coordinates", new Object[]{Overlays.getEntityPositionTextTitle(), Overlays.getEntityPositionTextDescription(ClientData.minecraft.player.getPos())}), ClientData.minecraft.getWindow().getScaledWidth() / 2, 115, 16777215);
	}
}
