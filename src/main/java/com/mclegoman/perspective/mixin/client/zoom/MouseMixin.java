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
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = Mouse.class)
public abstract class MouseMixin {
	@Shadow private double eventDeltaVerticalWheel;
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSpectator()Z"), method = "onMouseScroll", cancellable = true)
	private void perspective$onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
		// Zoom.isZooming() checks Zoom.canZoom(), so we don't need to check it again.
		if (Zoom.isZooming()) {
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
		// Zoom.isZooming() checks Zoom.canZoom(), so we don't need to check it again.
		if (Zoom.isZooming()) {
			if (button == 2) {
				Zoom.reset();
				ci.cancel();
			}
		}
	}

	@Inject(method = "updateMouse", at = @At(value = "HEAD"))
	private void perspective$updateTime(double timeDelta, CallbackInfo ci) {
		if (Zoom.canZoom() && Zoom.isSmoothCamera()) Zoom.timeDelta = timeDelta;
	}
	@ModifyVariable(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onUpdateMouse(DD)V"), ordinal = 1)
	private double perspective$updateXSensitivity(double x) {
		// Zoom.isZooming() checks Zoom.canZoom(), so we don't need to check it again.
		if (Zoom.isZooming()) {
			if (Zoom.isSmoothCamera()) x = Zoom.smoothX.smooth(x * Zoom.getMouseSensitivity(), Zoom.timeDelta * Zoom.getMouseSensitivity());
			if (Zoom.isScaled() && ClientData.minecraft.player != null) {
				double angle = MathHelper.cos((ClientData.minecraft.player.getPitch() / 180.0F) * MathHelper.PI);
				x = (x * (1.0F / Math.max((angle < 0) ? angle * -1.0F : angle, (Math.max(Zoom.getMultiplier(), 0.0F) + 1.0F) / 11.0F))) * Zoom.getMultiplier();
			}
		}
		return x;
	}
	@ModifyVariable(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onUpdateMouse(DD)V"), ordinal = 2)
	private double perspective$updateYSensitivity(double y) {
		// Zoom.isZooming() checks Zoom.canZoom(), so we don't need to check it again.
		if (Zoom.isZooming()) {
			if (Zoom.isSmoothCamera()) y = Zoom.smoothY.smooth(y * Zoom.getMouseSensitivity(), Zoom.timeDelta * Zoom.getMouseSensitivity());
			if (Zoom.isScaled()) {
				return y * Zoom.getMultiplier();
			}
		}
		return y;
	}
}