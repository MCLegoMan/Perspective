/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.perspective;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.keybindings.Keybindings;
import com.mclegoman.perspective.config.ConfigHelper;

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
	public static net.minecraft.client.option.Perspective getPerspective() {
		return ClientData.minecraft.options.getPerspective();
	}
	public static double getHoldPerspectiveBackMultiplier() {
		return (double) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hold_perspective_back_multiplier");
	}
	public static double getHoldPerspectiveFrontMultiplier() {
		return (double) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hold_perspective_front_multiplier");
	}
	public static void tick() {
		if (Keybindings.setPerspectiveFirstPerson.wasPressed())
			setPerspective(net.minecraft.client.option.Perspective.FIRST_PERSON);
		if (Keybindings.setPerspectiveThirdPersonBack.wasPressed())
			setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_BACK);
		if (Keybindings.setPerspectiveThirdPersonFront.wasPressed())
			setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_FRONT);
		getHoldAll();
	}
	private static void setThirdPersonFront(net.minecraft.client.option.Perspective perspective) {
		if (!Keybindings.holdPerspectiveThirdPersonBack.isPressed() && !holdThirdPersonBackLock) {
			if (!holdThirdPersonFrontLock) {
				holdThirdPersonFrontPrev = perspective;
				if (ClientData.minecraft.options.getPerspective().equals(net.minecraft.client.option.Perspective.THIRD_PERSON_FRONT))
					setPerspective(net.minecraft.client.option.Perspective.FIRST_PERSON);
				else
					setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_FRONT);
			}
			holdThirdPersonFrontLock = true;
		}
	}
	private static void setThirdPersonBack(net.minecraft.client.option.Perspective perspective) {
		if (!Keybindings.holdPerspectiveThirdPersonFront.isPressed() && !holdThirdPersonFrontLock) {
			if (!holdThirdPersonBackLock) {
				ClientData.minecraft.worldRenderer.scheduleTerrainUpdate();
				holdThirdPersonBackPrev = perspective;
				if (ClientData.minecraft.options.getPerspective().equals(net.minecraft.client.option.Perspective.THIRD_PERSON_BACK))
					setPerspective(net.minecraft.client.option.Perspective.FIRST_PERSON);
				else
					setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_BACK);
			}
			holdThirdPersonBackLock = true;
		}
	}
	private static void getHoldAll() {
		getHoldFront(ClientData.minecraft.options.getPerspective());
		getHoldBack(ClientData.minecraft.options.getPerspective());
	}
	private static boolean isHoldFront() {
		return Keybindings.holdPerspectiveThirdPersonFront.isPressed();
	}
	private static void getHoldFront(net.minecraft.client.option.Perspective perspective) {
		if (isHoldFront())
			setThirdPersonFront(perspective);
		clearHoldFront();
	}
	private static boolean isHoldBack() {
		return Keybindings.holdPerspectiveThirdPersonBack.isPressed();
	}
	private static void getHoldBack(net.minecraft.client.option.Perspective perspective) {
		if (isHoldBack())
			setThirdPersonBack(perspective);
		clearHoldBack();
	}
	private static void clearHoldFront() {
		if (!Keybindings.holdPerspectiveThirdPersonFront.isPressed() && holdThirdPersonFrontLock) {
			holdThirdPersonFrontLock = false;
			if (isHoldBack()) getHoldBack(holdThirdPersonFrontPrev);
			else setPerspective(holdThirdPersonFrontPrev);
		}
	}
	private static void clearHoldBack() {
		if (!Keybindings.holdPerspectiveThirdPersonBack.isPressed() && holdThirdPersonBackLock) {
			holdThirdPersonBackLock = false;
			if (isHoldFront()) getHoldFront(holdThirdPersonBackPrev);
			else setPerspective(holdThirdPersonBackPrev);
		}
	}
	public static void setPerspective(net.minecraft.client.option.Perspective perspective) {
		ClientData.minecraft.worldRenderer.scheduleTerrainUpdate();
		ClientData.minecraft.options.setPerspective(perspective);
	}
}