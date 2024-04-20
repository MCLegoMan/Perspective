/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.perspective;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.keybindings.Keybindings;

public class Perspective {
	private static boolean holdThirdPersonBackLock;
	private static net.minecraft.client.option.Perspective holdThirdPersonBackPrev;
	private static boolean holdThirdPersonFrontLock;
	private static net.minecraft.client.option.Perspective holdThirdPersonFrontPrev;
	public static boolean isHoldingPerspective() {
		try {
			return holdThirdPersonBackLock || holdThirdPersonFrontLock;
		} catch (Exception ignored) {
		}
		return false;
	}
	public static void tick() {
		if (Keybindings.setPerspectiveFirstPerson.wasPressed())
			ClientData.minecraft.options.setPerspective(net.minecraft.client.option.Perspective.FIRST_PERSON);
		if (Keybindings.setPerspectiveThirdPersonBack.wasPressed())
			ClientData.minecraft.options.setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_BACK);
		if (Keybindings.setPerspectiveThirdPersonFront.wasPressed())
			ClientData.minecraft.options.setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_FRONT);
		getHoldAll();
	}
	private static void setThirdPersonFront(net.minecraft.client.option.Perspective perspective) {
		if (!Keybindings.holdPerspectiveThirdPersonBack.isPressed() && !holdThirdPersonBackLock) {
			if (!holdThirdPersonFrontLock) {
				holdThirdPersonFrontPrev = perspective;
				if (ClientData.minecraft.options.getPerspective().equals(net.minecraft.client.option.Perspective.THIRD_PERSON_FRONT))
					ClientData.minecraft.options.setPerspective(net.minecraft.client.option.Perspective.FIRST_PERSON);
				else
					ClientData.minecraft.options.setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_FRONT);
			}
			holdThirdPersonFrontLock = true;
		}
	}
	private static void setThirdPersonBack(net.minecraft.client.option.Perspective perspective) {
		if (!Keybindings.holdPerspectiveThirdPersonFront.isPressed() && !holdThirdPersonFrontLock) {
			if (!holdThirdPersonBackLock) {
				holdThirdPersonBackPrev = perspective;
				if (ClientData.minecraft.options.getPerspective().equals(net.minecraft.client.option.Perspective.THIRD_PERSON_BACK))
					ClientData.minecraft.options.setPerspective(net.minecraft.client.option.Perspective.FIRST_PERSON);
				else
					ClientData.minecraft.options.setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_BACK);
			}
			holdThirdPersonBackLock = true;
		}
	}
	private static void getHoldAll() {
		for (int i = 0; i < 2; i++) {
			if (Keybindings.holdPerspectiveThirdPersonFront.isPressed())
				setThirdPersonFront(ClientData.minecraft.options.getPerspective());
			clearHoldFront();
			if (Keybindings.holdPerspectiveThirdPersonBack.isPressed())
				setThirdPersonBack(ClientData.minecraft.options.getPerspective());
			clearHoldBack();
		}
	}
	private static void clearHoldFront() {
		if (!Keybindings.holdPerspectiveThirdPersonFront.isPressed() && holdThirdPersonFrontLock) {
			holdThirdPersonFrontLock = false;
			ClientData.minecraft.options.setPerspective(holdThirdPersonFrontPrev);
		}
	}
	private static void clearHoldBack() {
		if (!Keybindings.holdPerspectiveThirdPersonBack.isPressed() && holdThirdPersonBackLock) {
			holdThirdPersonBackLock = false;
			ClientData.minecraft.options.setPerspective(holdThirdPersonBackPrev);
		}
	}
}