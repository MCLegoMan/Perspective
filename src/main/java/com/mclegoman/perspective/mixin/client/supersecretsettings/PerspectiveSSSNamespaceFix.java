package com.mclegoman.perspective.mixin.client.supersecretsettings;

import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Shader Namespace Fix - Forked from Souper Secret Settings
// https://github.com/Nettakrim/Souper-Secret-Settings/blob/main/src/main/java/com/nettakrim/souper_secret_settings/mixin/JsonEffectShaderProgramMixin.java
@Mixin(JsonEffectShaderProgram.class)
public class PerspectiveSSSNamespaceFix {
    @Redirect(method = "<init>", at = @At(value = "NEW", target = "(Ljava/lang/String;)Lnet/minecraft/util/Identifier;"))
    private Identifier init(String id) {
        return get(id);
    }
    @Redirect(method = "loadEffect", at = @At(value = "NEW", target = "(Ljava/lang/String;)Lnet/minecraft/util/Identifier;"))
    private static Identifier loadEffect(String id) {
        return get(id);
    }
    private static Identifier get(String id) {
        if (id.contains(":")) {
            String[] shader = id.substring(16).split(":");
            return new Identifier(shader[0], "shaders/program/" + shader[1]);
        } else {
            return new Identifier(id);
        }
    }
}