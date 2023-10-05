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
	public static boolean isTypeNewer(RTUReleaseTypes type1, RTUReleaseTypes type2) {
		if (type2.equals(RTUReleaseTypes.ALPHA)) {
			return type1.equals(RTUReleaseTypes.BETA) || type1.equals(RTUReleaseTypes.RELEASE_CANDIDATE) || type1.equals(RTUReleaseTypes.RELEASE);
		} else if (type2.equals(RTUReleaseTypes.BETA)) {
			return type1.equals(RTUReleaseTypes.RELEASE_CANDIDATE) || type1.equals(RTUReleaseTypes.RELEASE);
		} else if (type2.equals(RTUReleaseTypes.RELEASE_CANDIDATE)) {
			return type1.equals(RTUReleaseTypes.RELEASE);
		} else return false;
	}
}
