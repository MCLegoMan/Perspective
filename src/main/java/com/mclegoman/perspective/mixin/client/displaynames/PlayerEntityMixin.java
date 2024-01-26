/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.displaynames;

import com.mclegoman.perspective.client.displaynames.DisplayNamesDataLoader;
import com.mclegoman.perspective.common.util.Couple;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.UUID;

@Mixin(priority = 10000, value = PlayerEntityRenderer.class)
public abstract class PlayerEntityMixin {
	@ModifyArgs(method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IF)V", ordinal = 1, opcode = 2))
	private void overrideDisplayName(Args args) {
		AbstractClientPlayerEntity playerEntity = args.get(0);
		if (!DisplayNamesDataLoader.REGISTRY.isEmpty()) {
			for (Couple<UUID, Text> player : DisplayNamesDataLoader.REGISTRY) {
				if (player.getFirst().equals(playerEntity.getGameProfile().getId())) {
					args.set(1, Team.decorateName(((AbstractClientPlayerEntity)args.get(0)).getScoreboardTeam(), player.getSecond()));
				}
			}
		}
	}
}