/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.common.version;

import com.mclegoman.releasetypeutils.common.releasetype.RTUReleaseTranslationTypes;
import com.mclegoman.releasetypeutils.common.releasetype.RTUReleaseType;
import com.mclegoman.releasetypeutils.common.releasetype.RTUReleaseTypes;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Version implements Comparable<Version> {
	private final String name;
	private final String id;
	private final int major;
	private final int minor;
	private final int patch;
	private final RTUReleaseTypes type;
	private final int build;
	public Version(String name, String id, int major, int minor, int patch, RTUReleaseTypes type, int build) {
		this.name = name;
		this.id = id;
		this.major = major;
		this.minor = minor;
		this.patch = patch;
		this.type = type;
		this.build = build;
	}
	public String getName() {
		return name;
	}
	public String getID() {
		return id;
	}
	public int getMajor() {
		return major;
	}
	public int getMinor() {
		return minor;
	}
	public int getPatch() {
		return patch;
	}
	public RTUReleaseTypes getType() {
		return type;
	}
	public int getBuild() {
		return build;
	}
	public String getFriendlyString() {
		return String.format("%s.%s.%s-%s.%s", getMajor(), getMinor(), getPatch(), RTUReleaseType.releaseTypeString(getType(), RTUReleaseTranslationTypes.CODE), getBuild());
	}
	public boolean isDevelopmentBuild() {
		return RTUReleaseType.isDevelopmentBuild(getType());
	}
	public Logger getLogger() {
		return LoggerFactory.getLogger(getName());
	}
	public String getLoggerPrefix() {
		return String.format("[%s %s]", getName(), getFriendlyString());
	}
	public ModContainer getModContainer() {
		return FabricLoader.getInstance().getModContainer(getID()).get();
	}
	@Override
	public int compareTo(Version other) {
		if (major != other.major) {
			return Integer.compare(major, other.major);
		} else if (minor != other.minor) {
			return Integer.compare(minor, other.minor);
		} else if (patch != other.patch) {
			return Integer.compare(patch, other.patch);
		} else if (type != other.type) {
			return type.compareTo(other.type);
		} else {
			return Integer.compare(build, other.build);
		}
	}
}