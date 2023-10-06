/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.april_fools_prank;

import com.mclegoman.perspective.client.april_fools_prank.PerspectiveAprilFoolsPrank;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = SkinTextures.class)
public class PerspectiveAprilFoolsPrankReplaceCape {
    @Inject(at = @At("RETURN"), method = "capeTexture", cancellable = true)
    private void perspective$getCape(CallbackInfoReturnable<Identifier> cir) {
        try {
            if (PerspectiveAprilFoolsPrank.isPrankEnabled() && PerspectiveAprilFoolsPrank.isAprilFools()) cir.setReturnValue(new Identifier(PerspectiveData.PERSPECTIVE_VERSION.getID(), "textures/april_fools_prank/cape.png"));
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to set April Fools getCape.", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
}