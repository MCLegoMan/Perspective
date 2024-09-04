package com.mclegoman.luminance.common.util;

public class Couple<a, b> {
	private a first;
	private b second;

	public Couple(a first, b second) {
		this.first = first;
		this.second = second;
	}

	public a getFirst() {
		return this.first;
	}

	public b getSecond() {
		return this.second;
	}

	public void setFirst(a value) {
		this.first = value;
	}

	public void setSecond(b value) {
		this.second = value;
	}
}

