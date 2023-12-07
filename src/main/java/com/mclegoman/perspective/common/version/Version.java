/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.common.version;

import com.mclegoman.releasetypeutils.common.util.Translation;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

public class Version extends com.mclegoman.releasetypeutils.common.version.Version {
	public Version(String name, String id, int major, int minor, int patch, Helper.ReleaseType type, int build) {
		super(name, id, major, minor, patch, type, build);
	}
	public String getFriendlyString(boolean full) {
		return full ? getFriendlyString() : (getType().equals(Helper.ReleaseType.RELEASE) ? String.format("%s.%s.%s", getMajor(), getMinor(), getPatch()) : getFriendlyString());
	}
	public ModContainer getModContainer() {
		return FabricLoader.getInstance().getModContainer(getID()).get();
	}
}