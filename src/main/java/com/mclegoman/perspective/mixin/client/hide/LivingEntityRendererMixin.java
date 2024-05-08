/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.hide;

import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hide.Hide;
import com.mclegoman.perspective.client.hide.HideNameTagsDataLoader;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	@Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
	private void perspective$hide_nametag(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		if (ClientData.CLIENT.gameRenderer.isRenderingPanorama() || (boolean) ConfigHelper.getConfig("hide_nametags") || (entity instanceof PlayerEntity && HideNameTagsDataLoader.REGISTRY.contains(String.valueOf((((PlayerEntity) entity).getGameProfile().getId())))))
			cir.setReturnValue(false);
		if (entity instanceof PlayerEntity) {
			if (Hide.shouldHidePlayer((PlayerEntity) entity)) cir.setReturnValue(false);
		}
	}
}