/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.zoom;

import com.mclegoman.luminance.client.shaders.Uniforms;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.zoom.Zoom;
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
	@Shadow private double cursorDeltaY;
	@Shadow private double cursorDeltaX;
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSpectator()Z"), method = "onMouseScroll", cancellable = true)
	private void perspective$onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
		if (Zoom.isZooming() || Keybindings.adjustAlpha.isPressed()) {
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
				if (Zoom.isZooming()) Zoom.zoom(scrollAmount, (int) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_increment_size"));
				if (Keybindings.adjustAlpha.isPressed()) Uniforms.adjust(scrollAmount);
				ci.cancel();
			}
		}
	}
	@Inject(at = @At("HEAD"), method = "onMouseButton", cancellable = true)
	private void perspective$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
		if (Zoom.isZooming() || Keybindings.adjustAlpha.isPressed()) {
			if (button == 2) {
				if (Zoom.isZooming()) Zoom.reset();
				if (Keybindings.adjustAlpha.isPressed()) Uniforms.reset();
				ci.cancel();
			}
		}
	}
	@Inject(method = "updateMouse", at = @At(value = "HEAD"))
	private void perspective$updateMouse(double timeDelta, CallbackInfo ci) {
		if (ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_scale_mode").equals("scaled")) {
			this.cursorDeltaX *= Math.max(Zoom.getMultiplier(), 0.001);
			this.cursorDeltaY *= Math.max(Zoom.getMultiplier(), 0.001);
		}
	}
}