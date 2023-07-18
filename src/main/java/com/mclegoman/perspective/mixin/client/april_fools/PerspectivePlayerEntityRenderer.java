/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.april_fools;

import com.mclegoman.perspective.client.april_fools.PerspectiveAprilFoolsDataLoader;
import com.mclegoman.perspective.client.april_fools.PerspectiveAprilFoolsUtils;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public class PerspectivePlayerEntityRenderer {
    private boolean isSLIM;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci){
        isSLIM = slim;
    }

    @Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
    private void getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
        try {
            if (entity instanceof PlayerEntity) {
                if (PerspectiveAprilFoolsUtils.isPrankEnabled() && PerspectiveAprilFoolsUtils.isAprilFools()) {
                    if (PerspectiveAprilFoolsDataLoader.REGISTRY.size() > 0) {
                        int index = Math.floorMod(entity.getUuid().getLeastSignificantBits(), PerspectiveAprilFoolsDataLoader.REGISTRY.size());
                        String type;
                        if (isSLIM) type = "slim";
                        else type = "wide";
                        cir.setReturnValue(new Identifier(PerspectiveData.ID, "textures/april_fools/" + type + "/" + PerspectiveAprilFoolsDataLoader.REGISTRY.get(index).toLowerCase() + ".png"));
                    }
                }
            }
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to set April Fools getTexture.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
    }
}