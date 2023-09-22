/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin;

import com.mclegoman.perspective.common.data.PerspectiveData;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class PerspectiveMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.equals("com.mclegoman.perspective.mixin.client.shaders.PerspectiveShaderNamespaceFix")) {
            return !(PerspectiveData.isModInstalled("souper_secret_settings") || PerspectiveData.isModInstalled("architectury") || PerspectiveData.isModInstalled("satin"));
        }
        if (mixinClassName.equals("com.mclegoman.perspective.mixin.client.shaders.PerspectiveShaderTextureNamespaceFix")) {
            return !PerspectiveData.isModInstalledVersionOrHigher("souper_secret_settings", "1.0.6", true);
        }
        if (mixinClassName.equals("com.mclegoman.perspective.mixin.client.compat.PerspectiveWTHIT")) {
            return PerspectiveData.isModInstalled("wthit");
        }
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
