/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.api;

import com.mclegoman.perspective.common.data.PerspectiveData;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(PlayerListEntry.class)
public abstract class PerspectivePlayerListEntry {
    @Shadow public abstract GameProfile getProfile();

    @Inject(at = @At("RETURN"), method = "getCapeTexture", cancellable = true)
    private void getCapeTexture(CallbackInfoReturnable<Identifier> cir) {
        try {
            // TODO: Replace with API
            if (this.getProfile().getName().equals("MCLegoMan")) {
                cir.setReturnValue(new Identifier(PerspectiveData.ID, "textures/api/capes/" + "marketplace" + ".png"));
            }
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to set API Cape texture.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
    }
}