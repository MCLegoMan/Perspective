/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.hide;

import com.mclegoman.perspective.client.hide.Hide;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(priority = 100, value = WorldRenderer.class)
public abstract class WorldRendererMixin {
	@ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawCuboidShapeOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/shape/VoxelShape;DDDFFFF)V"), method = "drawBlockOutline")
	private void perspective$drawBlockOutline(Args args) {
		// We check if the value is not 0.4F, as 0.4F is the default behaviour.
		// We use args.size() - 1, as alpha is the final argument.
		if (Hide.getBlockOutlineLevel() != 0.4F) args.set(args.size() - 1, Hide.getBlockOutlineLevel());
		// These values change what colour the outline is.
		// We use args.size() - (4,3,2), as these are the values for r,g,b.
		//args.set(args.size() - 4, 1.0F);
		//args.set(args.size() - 3, 1.0F);
		//args.set(args.size() - 2, 1.0F);
	}
}