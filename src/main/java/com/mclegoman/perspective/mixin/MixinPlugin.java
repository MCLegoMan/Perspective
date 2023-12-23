/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import com.mclegoman.perspective.common.data.Data;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {
	@Override
	public void onLoad(String mixinPackage) {
		MixinExtrasBootstrap.init();
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		// Super Secret Settings: Shader Namespace Fix
		if (mixinClassName.equals("com.mclegoman.perspective.mixin.client.super_secret_settings.ShaderNamespaceFix")) {
			return !(Data.isModInstalled("souper_secret_settings") || Data.isModInstalled("architectury") || Data.isModInstalled("satin"));
		}
		// Super Secret Settings: Shader Texture Namespace Fix
		if (mixinClassName.equals("com.mclegoman.perspective.mixin.client.super_secret_settings.ShaderTextureFix")) {
			return !Data.isModInstalledVersionOrHigher("souper_secret_settings", "1.0.6", true);
		}
		// Everything Else
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
}