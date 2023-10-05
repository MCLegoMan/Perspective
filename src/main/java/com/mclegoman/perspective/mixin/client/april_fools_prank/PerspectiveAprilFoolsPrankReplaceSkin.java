/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.april_fools_prank;

import com.mclegoman.perspective.client.april_fools_prank.PerspectiveAprilFoolsPrank;
import com.mclegoman.perspective.client.april_fools_prank.PerspectiveAprilFoolsPrankDataLoader;
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
@Mixin(priority = 10000, value = PlayerEntityRenderer.class)
public class PerspectiveAprilFoolsPrankReplaceSkin {
    private boolean isSLIM;
    @Inject(at = @At("RETURN"), method = "<init>")
    private void perspective$init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci){
        isSLIM = slim;
    }
    @Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
    private void perspective$getSkin(Entity entity, CallbackInfoReturnable<Identifier> cir) {
        try {
            if (entity instanceof PlayerEntity) {
                if (PerspectiveAprilFoolsPrank.isPrankEnabled() && PerspectiveAprilFoolsPrank.isAprilFools()) {
                    if (PerspectiveAprilFoolsPrankDataLoader.REGISTRY.size() > 0) {
                        int index = Math.floorMod(entity.getUuid().getLeastSignificantBits(), PerspectiveAprilFoolsPrankDataLoader.REGISTRY.size());
                        String type;
                        if (isSLIM) type = "slim";
                        else type = "wide";
                        cir.setReturnValue(new Identifier(PerspectiveData.PERSPECTIVE_VERSION.getID(), "textures/april_fools_prank/" + type + "/" + PerspectiveAprilFoolsPrankDataLoader.REGISTRY.get(index).toLowerCase() + ".png"));
                    }
                }
            }
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to set April Fools getSkin.", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
}