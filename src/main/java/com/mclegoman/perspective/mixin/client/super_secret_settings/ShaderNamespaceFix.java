/*
    Perspective
    Contributor(s): Nettakrim
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.super_secret_settings;

import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(priority = 100, value = JsonEffectShaderProgram.class)
public class ShaderNamespaceFix {
	@Shadow
	@Final
	private static String PROGRAM_DIRECTORY;
	@Redirect(method = "loadEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;"))
	private static Identifier perspective$loadEffect(String path) {
		return perspective$get(path);
	}
	@Unique
	private static Identifier perspective$get(String id) {
		if (id.contains(":")) {
			String[] shader = id.substring(16).split(":");
			return Identifier.of(shader[0], PROGRAM_DIRECTORY + shader[1]);
		} else {
			return Identifier.of(id);
		}
	}
	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;"))
	private Identifier perspective$init(String path) {
		return perspective$get(path);
	}
}