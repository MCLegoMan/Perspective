/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.config.screen;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.developmentwarning.screen.PerspectiveDevelopmentWarningScreen;
import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.client.translation.PerspectiveTranslationType;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.IconWidget;
import net.minecraft.util.Formatting;

public class PerspectiveConfigScreenHelper {
    public static GridWidget createTitle(MinecraftClient client, Screen SCREEN) {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(new IconWidget(224, 42, PerspectiveClientData.getLogo()));
        if (PerspectiveData.IS_DEVELOPMENT) {
            GridWidget DEVELOPMENT_BUTTONS = new GridWidget();
            DEVELOPMENT_BUTTONS.getMainPositioner().alignHorizontalCenter().margin(2);
            GridWidget.Adder DEVELOPMENT_BUTTONS_ADDER = DEVELOPMENT_BUTTONS.createAdder(2);
            DEVELOPMENT_BUTTONS_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("development_warning", new Object[]{PerspectiveTranslation.getVariableTranslation((boolean)PerspectiveConfigHelper.getConfig("show_development_warning"), PerspectiveTranslationType.ONFF)}), (button) -> {
                PerspectiveConfigHelper.setConfig("show_development_warning", !(boolean)PerspectiveConfigHelper.getConfig("show_development_warning"));
                client.setScreen(SCREEN);
            }).build()).setTooltip(Tooltip.of(PerspectiveTranslation.getConfigTranslation("development_warning", true)));
            DEVELOPMENT_BUTTONS_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("development_warning_preview"), (button) -> {
                client.setScreen(new PerspectiveDevelopmentWarningScreen(SCREEN, 200, false));
            }).width(20).build()).setTooltip(Tooltip.of(PerspectiveTranslation.getConfigTranslation("development_warning_preview", true)));
            GRID_ADDER.add(DEVELOPMENT_BUTTONS);
        }
        return GRID;
    }
}