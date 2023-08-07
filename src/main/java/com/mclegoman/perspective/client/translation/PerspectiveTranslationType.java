/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.translation;

import net.minecraft.util.StringIdentifiable;

public enum PerspectiveTranslationType implements StringIdentifiable {
    ENDISABLE("endisable"),
    ONFF("onff"),
    SHADER_MODE("shader_mode"),
    DISABLE_SCREEN_MODE("disable_screen_mode");
    private final String name;
    PerspectiveTranslationType(String name) {
        this.name = name;
    }
    @Override
    public String asString() {
        return this.name;
    }
}