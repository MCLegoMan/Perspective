/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.developer;

import com.mclegoman.perspective.client.april_fools_prank.AprilFoolsPrank;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.contributor.ContributorDataloader;
import com.mclegoman.perspective.client.util.Cape;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
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

	@Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
	private void getSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {
		if (!((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "allow_april_fools") && AprilFoolsPrank.isAprilFools())) {
			if (this.playerListEntry != null) {
				for (List<Object> DEVELOPER : ContributorDataloader.REGISTRY) {
					if (DEVELOPER.get(0).equals(playerListEntry.getProfile().getId().toString())) {
						if ((boolean) DEVELOPER.get(3)) {
							SkinTextures currentSkinTextures = cir.getReturnValue();
							Identifier capeTexture = Cape.getCapeTexture((String) DEVELOPER.get(4), currentSkinTextures.capeTexture());
							cir.setReturnValue(new SkinTextures(currentSkinTextures.texture(), currentSkinTextures.textureUrl(), capeTexture, capeTexture, currentSkinTextures.model(), currentSkinTextures.secure()));
						}
					}
				}
			}
		}
	}
}