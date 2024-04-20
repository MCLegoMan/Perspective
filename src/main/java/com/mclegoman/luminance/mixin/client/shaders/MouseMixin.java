/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.mixin.client.shaders;

import com.mclegoman.luminance.client.shaders.Uniforms;
import com.mclegoman.luminance.client.data.ClientData;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = Mouse.class)
public abstract class MouseMixin {
	@Shadow
	private double eventDeltaVerticalWheel;
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSpectator()Z"), method = "onMouseScroll", cancellable = true)
	private void perspective$onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
		if (Uniforms.updatingAlpha()) {
			boolean discreteMouseScroll = ClientData.minecraft.options.getDiscreteMouseScroll().getValue();
			double mouseWheelSensitivity = ClientData.minecraft.options.getMouseWheelSensitivity().getValue();
			double calculatedScroll = (discreteMouseScroll ? Math.signum(vertical) : vertical) * mouseWheelSensitivity;
			if (this.eventDeltaVerticalWheel != 0.0 && Math.signum(calculatedScroll) != Math.signum(this.eventDeltaVerticalWheel)) {
				this.eventDeltaVerticalWheel = 0.0;
			}
			this.eventDeltaVerticalWheel += calculatedScroll;
			int scrollAmount = (int) this.eventDeltaVerticalWheel;
			this.eventDeltaVerticalWheel -= scrollAmount;
			if (scrollAmount != 0) {
				if (Uniforms.updatingAlpha()) Uniforms.adjustAlpha(scrollAmount);
				ci.cancel();
			}
		}
	}
	@Inject(at = @At("HEAD"), method = "onMouseButton", cancellable = true)
	private void perspective$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
		if (Uniforms.updatingAlpha()) {
			if (button == 2) {
				if (Uniforms.updatingAlpha()) Uniforms.resetAlpha();
				ci.cancel();
			}
		}
	}
}