/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.contributor;

import com.mclegoman.perspective.client.events.AprilFoolsPrank;
import com.mclegoman.perspective.client.events.AprilFoolsPrankDataLoader;
import com.mclegoman.luminance.common.util.Couple;
import com.mclegoman.perspective.client.contributor.ContributorData;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.contributor.ContributorDataLoader;
import com.mclegoman.perspective.client.util.TextureHelper;
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

@Mixin(priority = 100, value = AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {
	@Shadow
	@Nullable
	private PlayerListEntry playerListEntry;

	@Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
	private void getSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {
		if (this.playerListEntry != null) {
			boolean isAprilFools = (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "allow_april_fools") && AprilFoolsPrank.isAprilFools() && !AprilFoolsPrankDataLoader.registry.isEmpty();
			SkinTextures currentSkinTextures = cir.getReturnValue();
			Identifier skinTexture = currentSkinTextures.texture();
			Identifier capeTexture = currentSkinTextures.capeTexture();
			SkinTextures.Model model = currentSkinTextures.model();
			String uuid = playerListEntry.getProfile().getId().toString();
			if (isAprilFools) {
				Couple<Identifier, Boolean> skin = AprilFoolsPrankDataLoader.registry.get(AprilFoolsPrank.getAprilFoolsIndex(this.playerListEntry.getProfile().getId().getLeastSignificantBits(), AprilFoolsPrankDataLoader.registry.size()));
				skinTexture = skin.getFirst();
				model = skin.getSecond() ? SkinTextures.Model.SLIM : SkinTextures.Model.WIDE;
				uuid = AprilFoolsPrankDataLoader.contributor;
			}
			for (ContributorData developer : ContributorDataLoader.registry) {
				if (developer.getUuid().equals(uuid)) {
					if (developer.getShouldReplaceCape()) {
						capeTexture = TextureHelper.getTexture(developer.getCapeTexture(), capeTexture);
					}
				}
			}
			cir.setReturnValue(new SkinTextures(skinTexture, currentSkinTextures.textureUrl(), capeTexture, capeTexture, model, currentSkinTextures.secure()));
		}
	}
}