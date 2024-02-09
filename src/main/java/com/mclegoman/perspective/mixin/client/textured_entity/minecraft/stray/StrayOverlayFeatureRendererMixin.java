/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.stray;

import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.StrayOverlayFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.StrayEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 10000, value = StrayOverlayFeatureRenderer.class)
public class StrayOverlayFeatureRendererMixin {
	@Mutable
	@Shadow @Final private static Identifier SKIN;
	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/mob/MobEntity;FFFFFF)V")
	private void perspective$getTexture(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, MobEntity entity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		if (entity instanceof StrayEntity)
			SKIN = TexturedEntity.getTexture(entity, "minecraft:stray", TexturedEntity.Affix.SUFFIX, "_overlay", new Identifier("textures/entity/skeleton/stray_overlay.png"));
	}
}