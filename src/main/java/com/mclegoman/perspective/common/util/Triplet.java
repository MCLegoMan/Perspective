/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.common.util;

public record Triplet<a, b, c>(a first, b second, c third) {
	public a getFirst() {
		return first;
	}
	public b getSecond() {
		return second;
	}
	public c getThird() {
		return third;
	}
}
