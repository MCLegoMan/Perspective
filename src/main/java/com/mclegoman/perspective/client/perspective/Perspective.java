/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.perspective;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.util.Keybindings;

public class Perspective {
	private static boolean HOLD_THIRD_PERSON_BACK_LOCK;
	private static net.minecraft.client.option.Perspective HOLD_THIRD_PERSON_BACK_PREV;
	private static boolean HOLD_THIRD_PERSON_FRONT_LOCK;
	private static net.minecraft.client.option.Perspective HOLD_THIRD_PERSON_FRONT_PREV;

	public static boolean isHoldingPerspective() {
		try {
			return HOLD_THIRD_PERSON_BACK_LOCK || HOLD_THIRD_PERSON_FRONT_LOCK;
		} catch (Exception ignored) {
		}
		return false;
	}

	public static void tick() {
		if (Keybindings.SET_PERSPECTIVE_FIRST_PERSON.wasPressed())
			ClientData.CLIENT.options.setPerspective(net.minecraft.client.option.Perspective.FIRST_PERSON);
		if (Keybindings.SET_PERSPECTIVE_THIRD_PERSON_BACK.wasPressed())
			ClientData.CLIENT.options.setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_BACK);
		if (Keybindings.SET_PERSPECTIVE_THIRD_PERSON_FRONT.wasPressed())
			ClientData.CLIENT.options.setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_FRONT);
		getHoldAll();
	}

	private static void setThirdPersonFront(net.minecraft.client.option.Perspective perspective) {
		if (!Keybindings.HOLD_PERSPECTIVE_THIRD_PERSON_BACK.isPressed() && !HOLD_THIRD_PERSON_BACK_LOCK) {
			if (!HOLD_THIRD_PERSON_FRONT_LOCK) {
				HOLD_THIRD_PERSON_FRONT_PREV = perspective;
				if (ClientData.CLIENT.options.getPerspective().equals(net.minecraft.client.option.Perspective.THIRD_PERSON_FRONT))
					ClientData.CLIENT.options.setPerspective(net.minecraft.client.option.Perspective.FIRST_PERSON);
				else
					ClientData.CLIENT.options.setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_FRONT);
			}
			HOLD_THIRD_PERSON_FRONT_LOCK = true;
		}
	}

	private static void setThirdPersonBack(net.minecraft.client.option.Perspective perspective) {
		if (!Keybindings.HOLD_PERSPECTIVE_THIRD_PERSON_FRONT.isPressed() && !HOLD_THIRD_PERSON_FRONT_LOCK) {
			if (!HOLD_THIRD_PERSON_BACK_LOCK) {
				HOLD_THIRD_PERSON_BACK_PREV = perspective;
				if (ClientData.CLIENT.options.getPerspective().equals(net.minecraft.client.option.Perspective.THIRD_PERSON_BACK))
					ClientData.CLIENT.options.setPerspective(net.minecraft.client.option.Perspective.FIRST_PERSON);
				else
					ClientData.CLIENT.options.setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_BACK);
			}
			HOLD_THIRD_PERSON_BACK_LOCK = true;
		}
	}

	private static void getHoldAll() {
		for (int i = 0; i < 2; i++) {
			if (Keybindings.HOLD_PERSPECTIVE_THIRD_PERSON_FRONT.isPressed())
				setThirdPersonFront(ClientData.CLIENT.options.getPerspective());
			clearHoldFront();
			if (Keybindings.HOLD_PERSPECTIVE_THIRD_PERSON_BACK.isPressed())
				setThirdPersonBack(ClientData.CLIENT.options.getPerspective());
			clearHoldBack();
		}
	}

	private static void clearHoldFront() {
		if (!Keybindings.HOLD_PERSPECTIVE_THIRD_PERSON_FRONT.isPressed() && HOLD_THIRD_PERSON_FRONT_LOCK) {
			HOLD_THIRD_PERSON_FRONT_LOCK = false;
			ClientData.CLIENT.options.setPerspective(HOLD_THIRD_PERSON_FRONT_PREV);
		}
	}

	private static void clearHoldBack() {
		if (!Keybindings.HOLD_PERSPECTIVE_THIRD_PERSON_BACK.isPressed() && HOLD_THIRD_PERSON_BACK_LOCK) {
			HOLD_THIRD_PERSON_BACK_LOCK = false;
			ClientData.CLIENT.options.setPerspective(HOLD_THIRD_PERSON_BACK_PREV);
		}
	}
}