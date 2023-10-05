/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.common.version;

import com.mclegoman.releasetypeutils.common.releasetype.RTUReleaseTypes;

public class PerspectiveVersionHelper {
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
