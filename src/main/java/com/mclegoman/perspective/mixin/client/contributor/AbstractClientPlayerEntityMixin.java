/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.contributor;

import com.mclegoman.perspective.client.util.TextureHelper;
import com.mclegoman.perspective.client.contributor.ContributorDataloader;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(priority = 100, value = AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {
	@Shadow
	@Nullable
	private PlayerListEntry playerListEntry;

	@Inject(method = "getCapeTexture", at = @At("RETURN"), cancellable = true)
	private void getCapeTexture(CallbackInfoReturnable<Identifier> cir) {
		if (this.playerListEntry != null) {
			Identifier capeTexture = cir.getReturnValue();
			for (List<Object> contributor : ContributorDataloader.registry) {
				if (contributor.get(0).equals(playerListEntry.getProfile().getId().toString())) {
					if ((boolean) contributor.get(3)) {
						capeTexture = TextureHelper.getCapeTexture((String) contributor.get(4), capeTexture);
					}
				}
			}
			cir.setReturnValue(capeTexture);
		}
	}
}