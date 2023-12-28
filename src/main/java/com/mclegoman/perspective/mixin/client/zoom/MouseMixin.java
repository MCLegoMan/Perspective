/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.zoom;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.zoom.Zoom;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 10000, value = Mouse.class)
public abstract class MouseMixin {
	@Shadow
	private double eventDeltaVerticalWheel;
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSpectator()Z"), method = "onMouseScroll", cancellable = true)
	private void perspective$onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
		if (Zoom.isZooming()) {
			boolean discreteMouseScroll = ClientData.CLIENT.options.getDiscreteMouseScroll().getValue();
			double mouseWheelSensitivity = ClientData.CLIENT.options.getMouseWheelSensitivity().getValue();
			double calculatedScroll = (discreteMouseScroll ? Math.signum(vertical) : vertical) * mouseWheelSensitivity;
			if (this.eventDeltaVerticalWheel != 0.0 && Math.signum(calculatedScroll) != Math.signum(this.eventDeltaVerticalWheel)) {
				this.eventDeltaVerticalWheel = 0.0;
			}
			this.eventDeltaVerticalWheel += calculatedScroll;
			int scrollAmount = (int) this.eventDeltaVerticalWheel;
			this.eventDeltaVerticalWheel -= scrollAmount;
			if (scrollAmount != 0) {
				Zoom.zoom(scrollAmount > 0, (int) ConfigHelper.getConfig("zoom_increment_size"));
				ci.cancel();
			}
		}
	}
	@Inject(at = @At("HEAD"), method = "onMouseButton", cancellable = true)
	private void perspective$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
		if (Zoom.isZooming()) {
			if (button == 2) {
				Zoom.reset();
				ci.cancel();
			}
		}
	}
	@ModifyExpressionValue(method = "updateMouse(D)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/Perspective;isFirstPerson()Z"))
	private boolean perspective$isFirstPerson(boolean isFirstPerson) {
		return ConfigHelper.getConfig("zoom_camera_mode").equals("spyglass") ? (isFirstPerson || Zoom.isZooming()) : isFirstPerson;
	}
	@ModifyExpressionValue(method = "updateMouse(D)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingSpyglass()Z"))
	private boolean perspective$isUsingSpyglass(boolean isUsingSpyglass) {
		return ConfigHelper.getConfig("zoom_camera_mode").equals("spyglass") ? (isUsingSpyglass || Zoom.isZooming()) : isUsingSpyglass;
	}
}