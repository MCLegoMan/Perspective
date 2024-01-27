/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.displaynames;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.displaynames.DisplayNames;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 10000, value = PlayerListHud.class)
public abstract class PlayerListHudMixin {
	@Inject(method = "getPlayerName", at = @At(value = "RETURN"), cancellable = true)
	private void perspective$getDisplayName(PlayerListEntry playerListEntry, CallbackInfoReturnable<Text> cir) {
		if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.EXPERIMENTAL, "displaynames")) {
			cir.setReturnValue(Team.decorateName(playerListEntry.getScoreboardTeam(), DisplayNames.getDisplayName(playerListEntry.getProfile().getId())));
		}
	}
}