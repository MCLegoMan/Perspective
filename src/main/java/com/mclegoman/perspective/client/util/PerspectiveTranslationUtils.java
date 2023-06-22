/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class PerspectiveTranslationUtils {
    public static Text booleanTranslate(boolean BOOLEAN) {
        if (BOOLEAN) return Text.translatable("gui.perspective.true");
        else return Text.translatable("gui.perspective.false");
    }
}
