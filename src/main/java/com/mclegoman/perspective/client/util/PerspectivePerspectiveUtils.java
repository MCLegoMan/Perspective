/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.client.registry.PerspectiveKeybindings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;

@Environment(EnvType.CLIENT)
public class PerspectivePerspectiveUtils {
    private static boolean HOLD_THIRD_PERSON_BACK_LOCK;
    private static Perspective HOLD_THIRD_PERSON_BACK_PREV;
    private static boolean HOLD_THIRD_PERSON_FRONT_LOCK;
    private static Perspective HOLD_THIRD_PERSON_FRONT_PREV;
    public static void tick(MinecraftClient client) {
        if (PerspectiveKeybindings.KEY_SET_PERSPECTIVE_FP.wasPressed()) client.options.setPerspective(Perspective.FIRST_PERSON);
        if (PerspectiveKeybindings.KEY_SET_PERSPECTIVE_TPB.wasPressed()) client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
        if (PerspectiveKeybindings.KEY_SET_PERSPECTIVE_TPF.wasPressed()) client.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
        if (!PerspectiveKeybindings.KEY_HOLD_PERSPECTIVE_TPB.isPressed() && !HOLD_THIRD_PERSON_BACK_LOCK) {
            if (PerspectiveKeybindings.KEY_HOLD_PERSPECTIVE_TPF.isPressed()) {
                if (!HOLD_THIRD_PERSON_FRONT_LOCK) {
                    HOLD_THIRD_PERSON_FRONT_PREV = setPrev(client);
                    if (client.options.getPerspective().equals(Perspective.THIRD_PERSON_FRONT)) client.options.setPerspective(Perspective.FIRST_PERSON);
                    else client.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
                }
                HOLD_THIRD_PERSON_FRONT_LOCK = true;
            }
        }
        if (!PerspectiveKeybindings.KEY_HOLD_PERSPECTIVE_TPF.isPressed() && HOLD_THIRD_PERSON_FRONT_LOCK) {
            HOLD_THIRD_PERSON_FRONT_LOCK = false;
            client.options.setPerspective(HOLD_THIRD_PERSON_FRONT_PREV);
        }
        if (!PerspectiveKeybindings.KEY_HOLD_PERSPECTIVE_TPF.isPressed() && !HOLD_THIRD_PERSON_FRONT_LOCK) {
            if (PerspectiveKeybindings.KEY_HOLD_PERSPECTIVE_TPB.isPressed()) {
                if (!HOLD_THIRD_PERSON_BACK_LOCK) {
                    HOLD_THIRD_PERSON_BACK_PREV = setPrev(client);
                    if (client.options.getPerspective().equals(Perspective.THIRD_PERSON_BACK)) client.options.setPerspective(Perspective.FIRST_PERSON);
                    else client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
                }
                HOLD_THIRD_PERSON_BACK_LOCK = true;
            }
        }
        if (!PerspectiveKeybindings.KEY_HOLD_PERSPECTIVE_TPB.isPressed() && HOLD_THIRD_PERSON_BACK_LOCK) {
            HOLD_THIRD_PERSON_BACK_LOCK = false;
            client.options.setPerspective(HOLD_THIRD_PERSON_BACK_PREV);
        }
    }
    private static Perspective setPrev(MinecraftClient client) {
        if (PerspectiveConfig.PERSPECTIVE_HOLD) return Perspective.FIRST_PERSON;
        else return client.options.getPerspective();
    }
}