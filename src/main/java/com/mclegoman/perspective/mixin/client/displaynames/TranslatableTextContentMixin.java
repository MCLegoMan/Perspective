/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.displaynames;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.displaynames.DisplayNamesDataLoader;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.common.util.Couple;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Mixin(priority = 10000, value = TranslatableTextContent.class)
public abstract class TranslatableTextContentMixin {
	@Shadow @Final private String key;
	@Shadow @Final private Object[] args;
	@Inject(method = "getArg", at = @At(value = "RETURN"), cancellable = true)
	private void perspective$getArg(int index, CallbackInfoReturnable<StringVisitable> cir) {
		try {
			if (!DisplayNamesDataLoader.REGISTRY.isEmpty()) {
				if (cir.getReturnValue() != null && cir.getReturnValue() instanceof Text) {
					ClientPlayNetworkHandler networkHandler = ClientData.CLIENT.getNetworkHandler();
					if (networkHandler != null) {
						if (this.key.startsWith("chat.type")) {
							if (index != (this.args.length - 1)) {
								if (ClientData.CLIENT.world != null) {
									HoverEvent hoverEvent = ((Text) cir.getReturnValue()).getStyle().getHoverEvent();
									Data.VERSION.sendToLog(Helper.LogType.INFO, "1");
									if (hoverEvent != null && hoverEvent.getValue(HoverEvent.Action.SHOW_ENTITY) != null) {
										Data.VERSION.sendToLog(Helper.LogType.INFO, "2");
										if (Objects.requireNonNull(hoverEvent.getValue(HoverEvent.Action.SHOW_ENTITY)).entityType == EntityType.PLAYER) {
											Data.VERSION.sendToLog(Helper.LogType.INFO, "3");
											List<AbstractClientPlayerEntity> players = ClientData.CLIENT.world.getPlayers();
											UUID uuid = Objects.requireNonNull(hoverEvent.getValue(HoverEvent.Action.SHOW_ENTITY)).uuid;
											if (!players.isEmpty()) {
												for (AbstractClientPlayerEntity playerEntity : players) {
													for (Couple<UUID, Text> player : DisplayNamesDataLoader.REGISTRY) {
														if (uuid.equals(player.getFirst())) {
															Data.VERSION.sendToLog(Helper.LogType.INFO, "4");
															cir.setReturnValue(Team.decorateName(playerEntity.getScoreboardTeam(), player.getSecond()).setStyle(((Text) cir.getReturnValue()).getStyle()));
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.ERROR, "Failed to modify chat message!");
		}
	}
}