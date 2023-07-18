package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.client.screen.PerspectiveDevelopmentWarningScreen;
import com.mclegoman.perspective.client.lang.PerspectiveTranslationUtils;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PerspectiveConfigScreenUtils {
    public static GridWidget createTitle(MinecraftClient client, Screen SCREEN) {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        if (PerspectiveData.IS_DEVELOPMENT) {
            GRID_ADDER.add(new IconWidget(224, 42, new Identifier(PerspectiveData.ID, "textures/logo/development.png")));
            GridWidget DEVELOPMENT_BUTTONS = new GridWidget();
            DEVELOPMENT_BUTTONS.getMainPositioner().alignHorizontalCenter().margin(2);
            GridWidget.Adder DEVELOPMENT_BUTTONS_ADDER = DEVELOPMENT_BUTTONS.createAdder(2);
            DEVELOPMENT_BUTTONS_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.show_development_warning", PerspectiveTranslationUtils.onOffTranslate((boolean) PerspectiveConfigHelper.getConfig("show_development_warning"))), (button) -> {
                PerspectiveConfigHelper.setConfig("show_development_warning", !(boolean)PerspectiveConfigHelper.getConfig("show_development_warning"));
                client.setScreen(SCREEN);
            }).build()).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.show_development_warning.hover"), Text.translatable("gui.perspective.config.show_development_warning.hover")));
            DEVELOPMENT_BUTTONS_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.test_development_warning"), (button) -> {
                client.setScreen(new PerspectiveDevelopmentWarningScreen(SCREEN, 200, false));
            }).width(20).build()).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.test_development_warning.hover"), Text.translatable("gui.perspective.config.test_development_warning.hover")));;
            GRID_ADDER.add(DEVELOPMENT_BUTTONS);
        } else {
            GRID_ADDER.add(new IconWidget(224, 42, new Identifier(PerspectiveData.ID, "textures/logo/release.png")), 2);
        }
        return GRID;
    }
}