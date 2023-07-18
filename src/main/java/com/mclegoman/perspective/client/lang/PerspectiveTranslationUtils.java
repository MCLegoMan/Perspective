/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.lang;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class PerspectiveTranslationUtils {
    public static Text onOffTranslate(boolean BOOLEAN) {
        if (BOOLEAN) return Text.translatable("gui.perspective.true");
        else return Text.translatable("gui.perspective.false");
    }
    public static Text enabledDisabledTranslate(boolean BOOLEAN) {
        if (BOOLEAN) return Text.translatable("gui.perspective.enabled");
        else return Text.translatable("gui.perspective.disabled");
    }
    public static Text superSecretSettingsModeTranslate(boolean BOOLEAN) {
        if (BOOLEAN) return Text.translatable("gui.perspective.super_secret_settings_mode.screen");
        else return Text.translatable("gui.perspective.super_secret_settings_mode.game");
    }
}