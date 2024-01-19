/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.april_fools_prank;

import com.mclegoman.perspective.client.april_fools_prank.AprilFoolsPrank;
import com.mclegoman.perspective.client.april_fools_prank.AprilFoolsPrankDataLoader;
import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.contributor.ContributorDataloader;
import com.mclegoman.perspective.client.util.Cape;
import com.mclegoman.perspective.common.data.Data;
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

@Mixin(priority = 10000, value = AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {
	@Shadow
	@Nullable
	private PlayerListEntry playerListEntry;

	@Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
	private void getSkinTexture(CallbackInfoReturnable<SkinTextures> cir) {
		if (((boolean) ConfigHelper.getConfig("allow_april_fools") && AprilFoolsPrank.isAprilFools())) {
			if (this.playerListEntry != null) {
				SkinTextures currentSkinTextures = cir.getReturnValue();
				Identifier skinTexture = currentSkinTextures.texture();
				Identifier capeTexture = currentSkinTextures.capeTexture();
				if (AprilFoolsPrankDataLoader.shouldDisplayCape) {
					for (List<Object> DEVELOPER : ContributorDataloader.REGISTRY) {
						if (DEVELOPER.get(0).equals(AprilFoolsPrankDataLoader.contributor)) {
							if ((boolean) DEVELOPER.get(3)) {
								capeTexture = Cape.getCapeTexture((String) DEVELOPER.get(4), currentSkinTextures.capeTexture());
								break;
							}
						}
					}
				}
				if (AprilFoolsPrankDataLoader.REGISTRY.size() > 0)
					skinTexture = new Identifier(Data.PERSPECTIVE_VERSION.getID(), "textures/entity/player/slim/" + AprilFoolsPrankDataLoader.REGISTRY.get(Math.floorMod(this.playerListEntry.getProfile().getId().getLeastSignificantBits(), AprilFoolsPrankDataLoader.REGISTRY.size())).toLowerCase() + ".png");
				cir.setReturnValue(new SkinTextures(skinTexture, currentSkinTextures.textureUrl(), capeTexture, capeTexture, SkinTextures.Model.SLIM, currentSkinTextures.secure()));
			}
		}
	}
}