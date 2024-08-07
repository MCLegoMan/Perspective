/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.zoom;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.config.ConfigHelper;
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
		if (Zoom.canZoom() && Zoom.isZooming()) {
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
				Zoom.zoom(scrollAmount, (int) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_increment_size"));
				ci.cancel();
			}
		}
	}
	@Inject(at = @At("HEAD"), method = "onMouseButton", cancellable = true)
	private void perspective$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
		if (Zoom.canZoom() && Zoom.isZooming()) {
			if (button == 2) {
				Zoom.reset();
				ci.cancel();
			}
		}
	}
	@Inject(method = "updateMouse", at = @At(value = "HEAD"))
	private void perspective$updateMouse(double timeDelta, CallbackInfo ci) {
		if (Zoom.canZoom()) {
			if (ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_scale_mode").equals("scaled") && ClientData.minecraft.player != null) {
			/* more zoom options #8
			     3: a change in yaw has less effect at extreme pitches
			       when really zoomed in and looking steeply up or down, moving your mouse in a circle moves the camera in a 0 shape, this works fine in normal gameplay, but when youre really zoomed in it feels a bit bad
			       the solution is yaw_delta /= cos(pitch), however this causes a division by 0 when looking directly up or down (which is expected if you think about it - its impossible to get lateral movment from yaw at this point, you can only change roll)
			       so a reasonable solution is just to do max(cos(pitch), 0.1)
			       this corrected yaw effect should "fade in" as you get more zoomed in, this can be done by raising the clamp, but a lerp may be more reasonable
			       solution: probably add it onto the 2nd button */
				// We should use Zoom.getMultiplier to "fade in" the yaw correction.
				this.cursorDeltaX *= Math.max(Zoom.getMultiplier(), 0.001);
				this.cursorDeltaY *= Math.max(Zoom.getMultiplier(), 0.001);
			}
		}
	}
}