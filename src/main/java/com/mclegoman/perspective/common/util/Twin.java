/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.common.util;

public record Twin<a, b>(a first, b second) {
	public a getFirst() {
		return first;
	}
	public b getSecond() {
		return second;
	}
}
