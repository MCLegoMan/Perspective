/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.common.util;

public record Couple<a, b>(a first, b second) {
	public a getFirst() {
		return first;
	}
	public b getSecond() {
		return second;
	}
}
