/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.common.version;

import com.mclegoman.releasetypeutils.common.releasetype.RTUReleaseTypes;

public class VersionHelper {
	public static RTUReleaseTypes stringToType(String type) {
		if (type.equalsIgnoreCase("alpha")) {
			return RTUReleaseTypes.ALPHA;
		}
		if (type.equalsIgnoreCase("beta")) {
			return RTUReleaseTypes.BETA;
		}
		if (type.equalsIgnoreCase("rc")) {
			return RTUReleaseTypes.RELEASE_CANDIDATE;
		} else {
			return RTUReleaseTypes.RELEASE;
		}
	}
}
