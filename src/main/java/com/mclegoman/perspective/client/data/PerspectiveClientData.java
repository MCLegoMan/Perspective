/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.data;

import com.mclegoman.perspective.common.data.PerspectiveData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class PerspectiveClientData {
    public static final Identifier LOGO = PerspectiveData.IS_DEVELOPMENT ? new Identifier(PerspectiveData.ID, "textures/logo/development.png") : new Identifier(PerspectiveData.ID, "textures/logo/release.png");
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();
}