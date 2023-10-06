/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.entity_renderer;

import com.mclegoman.perspective.client.textured_entity.PerspectiveTexturedEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.DrownedEntityRenderer;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = DrownedEntityRenderer.class)
public class PerspectiveDrownedEntityRenderer {
    @Inject(at = @At("RETURN"), method = "getTexture", cancellable = true)
    private void perspective$getTexture(ZombieEntity entity, CallbackInfoReturnable<Identifier> cir) {
        if (entity instanceof DrownedEntity) cir.setReturnValue(PerspectiveTexturedEntity.getTexture(entity, "minecraft:drowned", "", cir.getReturnValue()));
    }
}