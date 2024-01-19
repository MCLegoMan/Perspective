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
	private final String modrinth_id;
	public Version(String name, String id, int major, int minor, int patch, Helper.ReleaseType type, int build, String modrinth_id) {
		super(name, id, major, minor, patch, type, build);
		this.modrinth_id = modrinth_id;
	}
	public String getFriendlyString(boolean full) {
		return full ? getFriendlyString() : (getType().equals(Helper.ReleaseType.RELEASE) ? String.format("%s.%s.%s", getMajor(), getMinor(), getPatch()) : getFriendlyString());
	}
	public String getModrinthID() {
		return this.modrinth_id;
	}
	public ModContainer getModContainer() {
		return Data.getModContainer(getID());
	}
}