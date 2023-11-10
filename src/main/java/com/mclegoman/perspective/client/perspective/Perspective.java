/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.perspective;

import com.mclegoman.perspective.client.util.Keybindings;
import net.minecraft.client.MinecraftClient;

public class Perspective {
    public static boolean isHoldingPerspective() {
        try {
            return HOLD_THIRD_PERSON_BACK_LOCK || HOLD_THIRD_PERSON_FRONT_LOCK;
        } catch (Exception ignored) {
        }
        return false;
    }
    private static boolean HOLD_THIRD_PERSON_BACK_LOCK;
    private static net.minecraft.client.option.Perspective HOLD_THIRD_PERSON_BACK_PREV;
    private static boolean HOLD_THIRD_PERSON_FRONT_LOCK;
    private static net.minecraft.client.option.Perspective HOLD_THIRD_PERSON_FRONT_PREV;
    public static void tick(MinecraftClient client) {
        if (Keybindings.SET_PERSPECTIVE_FIRST_PERSON.wasPressed()) client.options.setPerspective(net.minecraft.client.option.Perspective.FIRST_PERSON);
        if (Keybindings.SET_PERSPECTIVE_THIRD_PERSON_BACK.wasPressed()) client.options.setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_BACK);
        if (Keybindings.SET_PERSPECTIVE_THIRD_PERSON_FRONT.wasPressed()) client.options.setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_FRONT);
        getHoldAll(client);
    }
    private static void setThirdPersonFront(MinecraftClient client, net.minecraft.client.option.Perspective perspective) {
        if (!Keybindings.HOLD_PERSPECTIVE_THIRD_PERSON_BACK.isPressed() && !HOLD_THIRD_PERSON_BACK_LOCK) {
            if (!HOLD_THIRD_PERSON_FRONT_LOCK) {
                HOLD_THIRD_PERSON_FRONT_PREV = perspective;
                if (client.options.getPerspective().equals(net.minecraft.client.option.Perspective.THIRD_PERSON_FRONT)) client.options.setPerspective(net.minecraft.client.option.Perspective.FIRST_PERSON);
                else client.options.setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_FRONT);
            }
            HOLD_THIRD_PERSON_FRONT_LOCK = true;
        }
    }
    private static void setThirdPersonBack(MinecraftClient client, net.minecraft.client.option.Perspective perspective) {
        if (!Keybindings.HOLD_PERSPECTIVE_THIRD_PERSON_FRONT.isPressed() && !HOLD_THIRD_PERSON_FRONT_LOCK) {
            if (!HOLD_THIRD_PERSON_BACK_LOCK) {
                HOLD_THIRD_PERSON_BACK_PREV = perspective;
                if (client.options.getPerspective().equals(net.minecraft.client.option.Perspective.THIRD_PERSON_BACK)) client.options.setPerspective(net.minecraft.client.option.Perspective.FIRST_PERSON);
                else client.options.setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_BACK);
            }
            HOLD_THIRD_PERSON_BACK_LOCK = true;
        }
    }
    private static void getHoldAll(MinecraftClient client) {
        for (int i = 0; i < 2; i++) {
            if (Keybindings.HOLD_PERSPECTIVE_THIRD_PERSON_FRONT.isPressed()) setThirdPersonFront(client, client.options.getPerspective());
            clearHoldFront(client);
            if (Keybindings.HOLD_PERSPECTIVE_THIRD_PERSON_BACK.isPressed()) setThirdPersonBack(client, client.options.getPerspective());
            clearHoldBack(client);
        }
    }
    private static void clearHoldFront(MinecraftClient client) {
        if (!Keybindings.HOLD_PERSPECTIVE_THIRD_PERSON_FRONT.isPressed() && HOLD_THIRD_PERSON_FRONT_LOCK) {
            HOLD_THIRD_PERSON_FRONT_LOCK = false;
            client.options.setPerspective(HOLD_THIRD_PERSON_FRONT_PREV);
        }
    }
    private static void clearHoldBack(MinecraftClient client) {
        if (!Keybindings.HOLD_PERSPECTIVE_THIRD_PERSON_BACK.isPressed() && HOLD_THIRD_PERSON_BACK_LOCK) {
            HOLD_THIRD_PERSON_BACK_LOCK = false;
            client.options.setPerspective(HOLD_THIRD_PERSON_BACK_PREV);
        }
    }
}