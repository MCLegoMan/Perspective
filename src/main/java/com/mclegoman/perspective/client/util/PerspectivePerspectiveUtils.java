/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.client.registry.PerspectiveKeybindings;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;

public class PerspectivePerspectiveUtils {
    private static final ArrayList<Integer> PERSPECTIVES = new ArrayList<>();
    private static int getPerspectiveAmount() {
        return PERSPECTIVES.size() - 1;
    }
    private static boolean HOLD_THIRD_PERSON_BACK_LOCK;
    private static boolean HOLD_THIRD_PERSON_FRONT_LOCK;
    public static void init() {
        PERSPECTIVES.add(0);
        PERSPECTIVES.add(1);
        PERSPECTIVES.add(2);
    }
    public static void tick(MinecraftClient client) {
        if (PerspectiveKeybindings.KEY_CYCLE_PERSPECTIVE.wasPressed()) cycle(client);
        if (PerspectiveKeybindings.KEY_SET_PERSPECTIVE_FP.wasPressed()) {
            if (PERSPECTIVES.contains(0)) PerspectiveConfig.PERSPECTIVE = PERSPECTIVES.indexOf(0);
            set(client, PERSPECTIVES.get(PerspectiveConfig.PERSPECTIVE));
        }
        if (PerspectiveKeybindings.KEY_SET_PERSPECTIVE_TPB.wasPressed()) {
            if (PERSPECTIVES.contains(1)) PerspectiveConfig.PERSPECTIVE = PERSPECTIVES.indexOf(1);
            set(client, PERSPECTIVES.get(PerspectiveConfig.PERSPECTIVE));
        }
        if (PerspectiveKeybindings.KEY_SET_PERSPECTIVE_TPF.wasPressed()) {
            if (PERSPECTIVES.contains(2)) PerspectiveConfig.PERSPECTIVE = PERSPECTIVES.indexOf(2);
            set(client, PERSPECTIVES.get(PerspectiveConfig.PERSPECTIVE));
        }
        if (PerspectiveKeybindings.KEY_HOLD_PERSPECTIVE_TPB.isPressed()) {
            HOLD_THIRD_PERSON_BACK_LOCK = true;
            if (PERSPECTIVES.contains(1)) PerspectiveConfig.PERSPECTIVE = PERSPECTIVES.indexOf(1);
            set(client, PERSPECTIVES.get(PerspectiveConfig.PERSPECTIVE));
        }
        if (!PerspectiveKeybindings.KEY_HOLD_PERSPECTIVE_TPB.isPressed() && HOLD_THIRD_PERSON_BACK_LOCK) {
            HOLD_THIRD_PERSON_BACK_LOCK = false;
            if (PERSPECTIVES.contains(0)) PerspectiveConfig.PERSPECTIVE = PERSPECTIVES.indexOf(0);
            set(client, PERSPECTIVES.get(PerspectiveConfig.PERSPECTIVE));
        }
        if (PerspectiveKeybindings.KEY_HOLD_PERSPECTIVE_TPF.isPressed()) {
            HOLD_THIRD_PERSON_FRONT_LOCK = true;
            if (PERSPECTIVES.contains(2)) PerspectiveConfig.PERSPECTIVE = PERSPECTIVES.indexOf(2);
            set(client, PERSPECTIVES.get(PerspectiveConfig.PERSPECTIVE));
        }
        if (!PerspectiveKeybindings.KEY_HOLD_PERSPECTIVE_TPF.isPressed() && HOLD_THIRD_PERSON_FRONT_LOCK) {
            HOLD_THIRD_PERSON_FRONT_LOCK = false;
            if (PERSPECTIVES.contains(0)) PerspectiveConfig.PERSPECTIVE = PERSPECTIVES.indexOf(0);
            set(client, PERSPECTIVES.get(PerspectiveConfig.PERSPECTIVE));
        }
    }
    private static void cycle(MinecraftClient client) {
        try {
            if (PerspectiveConfig.PERSPECTIVE < getPerspectiveAmount()) PerspectiveConfig.PERSPECTIVE = PerspectiveConfig.PERSPECTIVE + 1;
            else PerspectiveConfig.PERSPECTIVE = 0;
            set(client, PERSPECTIVES.get(PerspectiveConfig.PERSPECTIVE));
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to cycle Perspectives.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
    }
    private static void set(MinecraftClient client, int perspective) {
        try {
            client.options.perspective = perspective;
            PerspectiveConfig.write_to_file();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to set Perspectives.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
            PerspectiveConfig.SUPER_SECRET_SETTINGS = 0;
            PerspectiveConfig.write_to_file();
        }
    }
}
