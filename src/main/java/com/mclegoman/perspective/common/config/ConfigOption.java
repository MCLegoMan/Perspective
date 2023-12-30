/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.common.config;

import com.mclegoman.perspective.common.util.Pair;

public class ConfigOption<String, Object> extends Pair<String, Object> {
	public ConfigOption(String option, Object value) {
		super(option, value);
	}
	public String getOption() {
		return getFirst();
	}
	public Object getValue() {
		return getSecond();
	}
}
