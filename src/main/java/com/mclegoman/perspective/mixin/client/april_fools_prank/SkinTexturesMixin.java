/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.april_fools_prank;

import com.mclegoman.perspective.client.april_fools_prank.AprilFoolsPrank;
import com.mclegoman.perspective.client.april_fools_prank.AprilFoolsPrankDataLoader;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 10000, value = SkinTextures.class)
public abstract class SkinTexturesMixin {
    @Inject(at = @At("RETURN"), method = "capeTexture", cancellable = true)
    private void perspective$getCape(CallbackInfoReturnable<Identifier> cir) {
        try {
            if (AprilFoolsPrank.isPrankEnabled() && AprilFoolsPrank.isAprilFools() && AprilFoolsPrankDataLoader.shouldDisplayCape) cir.setReturnValue(new Identifier(Data.PERSPECTIVE_VERSION.getID(), "textures/prank/cape.png"));
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to set April Fools getCape.", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
}