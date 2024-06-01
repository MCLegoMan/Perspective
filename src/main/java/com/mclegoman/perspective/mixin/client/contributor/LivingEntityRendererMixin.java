/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.contributor;

import com.mclegoman.perspective.client.april_fools_prank.AprilFoolsPrank;
import com.mclegoman.perspective.client.april_fools_prank.AprilFoolsPrankDataLoader;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.contributor.ContributorDataloader;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(priority = 100, value = LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "shouldFlipUpsideDown", cancellable = true)
	private static void perspective$shouldFlipUpsideDown(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {		
		if (entity instanceof PlayerEntity) {
			boolean shouldFlipUpsideDown = false;
			for (List<Object> contributor : ContributorDataloader.registry) {
				if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "allow_april_fools") &&
						AprilFoolsPrank.isAprilFools() && contributor.get(0).equals(AprilFoolsPrankDataLoader.contributor) && (boolean) contributor.get(2)) shouldFlipUpsideDown = true;
				if (contributor.get(0).equals(((PlayerEntity) entity).getGameProfile().getId().toString()) &&
						(boolean) contributor.get(2)) shouldFlipUpsideDown = !shouldFlipUpsideDown;
			}
			cir.setReturnValue(shouldFlipUpsideDown);
		}
	}
}