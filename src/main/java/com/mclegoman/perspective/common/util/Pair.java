/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.common.util;

public class Pair<f, s> {
	private final f first;
	private final s second;

	public Pair(final f first, final s second) {
		this.first = first;
		this.second = second;
	}

	public f getFirst() {
		return first;
	}

	public s getSecond() {
		return second;
	}
}
