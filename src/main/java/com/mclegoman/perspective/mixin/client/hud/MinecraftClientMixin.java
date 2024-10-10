/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.hud;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.util.Mouse;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = MinecraftClient.class)
public abstract class MinecraftClientMixin {
	@Inject(at = @At("RETURN"), method = "render")
	private void perspective$render(CallbackInfo ci) {
		if (Mouse.ProcessCPS.shouldProcessCPS()) {
			Mouse.updateLeftClick(ClientData.minecraft.mouse.wasLeftButtonClicked());
			Mouse.updateMiddleClick(ClientData.minecraft.mouse.wasMiddleButtonClicked());
			Mouse.updateRightClick(ClientData.minecraft.mouse.wasRightButtonClicked());
		} else Mouse.clearQueues();
	}
}