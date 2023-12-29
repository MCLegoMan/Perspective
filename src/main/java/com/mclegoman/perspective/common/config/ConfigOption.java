/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.common.config;

public class ConfigOption<o, v> {
	private final o option;
	private final v value;

	public ConfigOption(final o option, final v value) {
		this.option = option;
		this.value = value;
	}

	public o getOption() {
		return option;
	}

	public v getValue() {
		return value;
	}
}
