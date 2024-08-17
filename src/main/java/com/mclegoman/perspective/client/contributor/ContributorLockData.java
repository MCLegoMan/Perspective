/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.contributor;

import java.util.Arrays;
import java.util.List;

public class ContributorLockData {
	private final List<String> uuids;
	private final Contributor.Type type;
	public ContributorLockData(Contributor.Type type, String... uuids) {
		this.uuids = Arrays.stream(uuids).toList();
		this.type = type;
	}
	public List<String> getUuids() {
		return this.uuids;
	}
	public Contributor.Type getType() {
		return this.type;
	}
}
