/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.april_fools_prank;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mclegoman.perspective.client.april_fools_prank.PerspectiveAprilFoolsPrank;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = PlayerListEntry.class)
public class PerspectiveAprilFoolsPrankReplaceCape {
    @ModifyReturnValue(at = @At("RETURN"), method = "getCapeTexture")
    private Identifier perspective$getCape(Identifier getCapeTexture) {
        return PerspectiveAprilFoolsPrank.isPrankEnabled() && PerspectiveAprilFoolsPrank.isAprilFools() ? new Identifier(PerspectiveData.ID, "textures/april_fools_prank/cape.png") : getCapeTexture;
    }
}