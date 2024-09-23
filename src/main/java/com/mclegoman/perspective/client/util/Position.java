/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;

public class Position {
	private static final Map<String, Boolean> hidePos = new HashMap<>();
	public static void register(String modId) {
		if (shouldShowPos()) Data.version.sendToLog(LogType.INFO, "Positional data will now be obfuscated.");
		hidePos.putIfAbsent(modId, true);
	}
	public static boolean shouldShowPos() {
		return hidePos.isEmpty();
	}
	public static Text getX(Vec3d pos, boolean integer) {
		return shouldShowPos() ? Text.of(String.valueOf((integer ? (int) pos.getX() : pos.getX()))) : Text.of("?");
	}
	public static Text getX(Vec3d pos) {
		return getX(pos, false);
	}
	public static Text getX(LivingEntity entity) {
		return getX(entity.getPos());
	}
	public static Text getX(LivingEntity entity, boolean integer) {
		return getX(entity.getPos(), integer);
	}
	public static Text getY(Vec3d pos, boolean integer) {
		return shouldShowPos() ? Text.of(String.valueOf((integer ? (int) pos.getY() : pos.getY()))) : Text.of("?");
	}
	public static Text getY(Vec3d pos) {
		return getY(pos, false);
	}
	public static Text getY(LivingEntity entity) {
		return getY(entity.getPos());
	}
	public static Text getY(LivingEntity entity, boolean integer) {
		return getY(entity.getPos(), integer);
	}
	public static Text getZ(Vec3d pos, boolean integer) {
		return shouldShowPos() ? Text.of(String.valueOf((integer ? (int) pos.getZ() : pos.getZ()))) : Text.of("?");
	}
	public static Text getZ(Vec3d pos) {
		return getZ(pos, false);
	}
	public static Text getZ(LivingEntity entity) {
		return getZ(entity.getPos());
	}
	public static Text getZ(LivingEntity entity, boolean integer) {
		return getZ(entity.getPos(), integer);
	}
}
