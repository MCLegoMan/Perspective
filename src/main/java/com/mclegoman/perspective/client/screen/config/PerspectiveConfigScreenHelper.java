/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.screen.config;

import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.client.widget.PerspectiveLogoWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.MultilineTextWidget;

public class PerspectiveConfigScreenHelper {
    public static GridWidget createTitle(MinecraftClient client, Screen SCREEN, boolean showPageName, String pageName) {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(new PerspectiveLogoWidget());
        if (showPageName) GRID_ADDER.add(new MultilineTextWidget(PerspectiveTranslation.getConfigTranslation(pageName), PerspectiveClientData.CLIENT.textRenderer).setCentered(true));
        GRID_ADDER.add(new EmptyWidget(4, 4));
        return GRID;
    }
}