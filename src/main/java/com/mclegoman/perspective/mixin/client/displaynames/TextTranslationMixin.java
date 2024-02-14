/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.displaynames;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.displaynames.DisplayNames;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mixin(priority = 100, value = TranslatableTextContent.class)
public abstract class TextTranslationMixin {
	@Shadow @Final private String key;
	@Inject(method = "getArg", at = @At(value = "RETURN"), cancellable = true)
	private void overrideDisplayName(int index, CallbackInfoReturnable<StringVisitable> cir) {
		if (cir.getReturnValue() != null && cir.getReturnValue() instanceof Text) {
			if (ClientData.CLIENT.world != null) {
				if (this.key.startsWith("chat.type")) {
					if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.EXPERIMENTAL, "displaynames")) {
						HoverEvent hoverEvent = ((Text) cir.getReturnValue()).getStyle().getHoverEvent();
						if (hoverEvent != null && hoverEvent.getValue(HoverEvent.Action.SHOW_ENTITY) != null) {
							if (Objects.requireNonNull(hoverEvent.getValue(HoverEvent.Action.SHOW_ENTITY)).entityType == EntityType.PLAYER) {
								List<AbstractClientPlayerEntity> players = ClientData.CLIENT.world.getPlayers();
								UUID uuid = Objects.requireNonNull(hoverEvent.getValue(HoverEvent.Action.SHOW_ENTITY)).uuid;
								if (!players.isEmpty()) {
									PlayerEntity player = ClientData.CLIENT.world.getPlayerByUuid(uuid);
									if (player != null)
										cir.setReturnValue(Team.decorateName(player.getScoreboardTeam(), DisplayNames.getDisplayName(uuid)).setStyle(((Text) cir.getReturnValue()).getStyle()));
								}
							}
						}
					}
				}
			}
		}
	}
}