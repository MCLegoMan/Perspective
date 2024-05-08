/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.common.data;

import com.mclegoman.releasetypeutils.common.version.Helper;
import net.fabricmc.loader.api.ModContainer;

public class Version extends com.mclegoman.releasetypeutils.common.version.Version {
	private final boolean hasModrinthId;
	private final String modrinthId;
	private Version(String name, String id, int major, int minor, int patch, Helper.ReleaseType type, int build, boolean hasModrinthId, String modrinthId) {
		super(name, id, major, minor, patch, type, build);
		this.hasModrinthId = hasModrinthId;
		this.modrinthId = modrinthId;
	}
	public static Version create(String name, String id, int major, int minor, int patch, Helper.ReleaseType type, int build, String modrinthId) {
		return new Version(name, id, major, minor, patch, type, build, true, modrinthId);
	}
	public static Version create(String name, String id, int major, int minor, int patch, Helper.ReleaseType type, int build) {
		return new Version(name, id, major, minor, patch, type, build, false, "");
	}
	public String getFriendlyString(boolean full) {
		return full ? getFriendlyString() : (getType().equals(Helper.ReleaseType.RELEASE) ? String.format("%s.%s.%s", getMajor(), getMinor(), getPatch()) : getFriendlyString());
	}
	public boolean hasModrinthID() {
		return this.hasModrinthId;
	}
	public String getModrinthID() {
		return this.modrinthId;
	}
	public ModContainer getModContainer() {
		return Data.getModContainer(getID());
	}
}