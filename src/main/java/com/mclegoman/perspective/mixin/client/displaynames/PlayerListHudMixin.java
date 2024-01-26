/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.displaynames;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.displaynames.DisplayNamesDataLoader;
import com.mclegoman.perspective.common.util.Couple;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(priority = 10000, value = PlayerListHud.class)
public abstract class PlayerListHudMixin {
	@Inject(method = "getPlayerName", at = @At(value = "RETURN"), cancellable = true)
	private void perspective$getDisplayName(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
		if (!DisplayNamesDataLoader.REGISTRY.isEmpty()) {
			for (Couple<UUID, Text> player : DisplayNamesDataLoader.REGISTRY) {
				if (player.getFirst().equals(entry.getProfile().getId())) {
					if (ClientData.CLIENT.world != null) cir.setReturnValue(Team.decorateName(entry.getScoreboardTeam(), player.getSecond()));
				}
			}
		}
	}
}