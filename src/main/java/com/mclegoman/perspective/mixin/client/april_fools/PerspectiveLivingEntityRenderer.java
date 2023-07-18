/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.april_fools;

import com.mclegoman.perspective.client.april_fools.PerspectiveAprilFoolsUtils;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public class PerspectiveLivingEntityRenderer {
    @Inject(at = @At("RETURN"), method = "shouldFlipUpsideDown", cancellable = true)
    private static void shouldFlipUpsideDown(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        try {
            if (entity instanceof PlayerEntity) {
                if (PerspectiveAprilFoolsUtils.isPrankEnabled() && PerspectiveAprilFoolsUtils.isAprilFools()) {
                    cir.setReturnValue(true);
                }
            }
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to set April Fools shouldFlipUpsideDown.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
    }
}