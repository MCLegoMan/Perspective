/*
    Perspective
    Contributor(s): MCLegoMan, Nettakrim
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.perspective;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mclegoman.perspective.client.perspective.Perspective;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(priority = 100, value = Camera.class)
public abstract class CameraMixin {
	@Shadow protected abstract float clipToSpace(float f);
	@Shadow private boolean thirdPerson;
	@ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;clipToSpace(F)F"), method = "update")
	private float perspective$update(float original) {
		// TODO: Customizable Hold Perspective Distance Multiplier.
		if (Perspective.isHoldingPerspective() && this.thirdPerson) return this.clipToSpace(original);// * 10.0F);
		return original;
	}
}