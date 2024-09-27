/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.ambience.fluid;

import com.mclegoman.perspective.client.ambience.particles.Particles;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.config.ConfigHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow @Final protected Random random;
	@Shadow public abstract double getX();
	@Shadow public abstract double getY();
	@Shadow public abstract double getZ();
	@Shadow public abstract int getBlockY();
	@Inject(method = "onSwimmingStart", at = @At("TAIL"))
	protected void perspective$onSwimmingStart(CallbackInfo ci) {
		if ((boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.experimental, "ambience") && (int) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "ripple_density") > 0) {
			if (ClientData.minecraft.world != null) {
				Particles.addRipple(ClientData.minecraft.world, new Vec3d(this.getX(), this.getBlockY() + 0.92F, this.getZ()));
			}
		}
	}
}
