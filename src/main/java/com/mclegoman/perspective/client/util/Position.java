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
		return Text.of(!shouldShowPos() ? "?" : (integer ? String.valueOf((int) pos.x) : String.valueOf(pos.x)));
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
		return Text.of(!shouldShowPos() ? "?" : (integer ? String.valueOf((int) pos.y) : String.valueOf(pos.y)));
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
		return Text.of(!shouldShowPos() ? "?" : (integer ? String.valueOf((int) pos.z) : String.valueOf(pos.z)));
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
