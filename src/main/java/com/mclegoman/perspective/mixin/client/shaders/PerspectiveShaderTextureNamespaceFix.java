/*

 */

package com.mclegoman.perspective.mixin.client.shaders;

import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(priority = 10000, value = PostEffectProcessor.class)
public class PerspectiveShaderTextureNamespaceFix {
    @Redirect(method = "parsePass", at = @At(value = "NEW", target = "(Ljava/lang/String;)Lnet/minecraft/util/Identifier;"))
    private static Identifier perspective$loadTexture(String id) {
        return perspective$get(id);
    }

    private static Identifier perspective$get(String id) {
        if (id.contains(":")) {
            String[] shader = id.substring(16).split(":");
            return new Identifier(shader[0], "textures/effect/" + shader[1]);
        } else {
            return new Identifier(id);
        }
    }
}
