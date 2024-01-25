/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.common.util;

public record Triplet<f, s, t>(f first, s second, t third) {
	public f getFirst() {
		return first;
	}
	public s getSecond() {
		return second;
	}
	public t getThird() {
		return third;
	}
}
