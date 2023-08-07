/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective

    This class was forked from Souper Secret Settings by Nettakrim.
    Licensed under GNU Lesser General Public License v3.0
    https://github.com/Nettakrim/Souper-Secret-Settings
*/

package com.mclegoman.perspective.mixin.client.shaders;

import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(priority = 10000, value = JsonEffectShaderProgram.class)
public class PerspectiveShaderNamespaceFix {
    @Redirect(method = "<init>", at = @At(value = "NEW", target = "(Ljava/lang/String;)Lnet/minecraft/util/Identifier;"))
    private Identifier perspective$init(String id) {
        return perspective$get(id);
    }
    @Redirect(method = "loadEffect", at = @At(value = "NEW", target = "(Ljava/lang/String;)Lnet/minecraft/util/Identifier;"))
    private static Identifier perspective$loadEffect(String id) {
        return perspective$get(id);
    }
    private static Identifier perspective$get(String id) {
        if (id.contains(":")) {
            String[] shader = id.substring(16).split(":");
            return new Identifier(shader[0], "shaders/program/" + shader[1]);
        } else {
            return new Identifier(id);
        }
    }
}