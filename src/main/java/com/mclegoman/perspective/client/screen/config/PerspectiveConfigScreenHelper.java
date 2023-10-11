/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config;

import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.client.util.PerspectiveUpdateChecker;
import com.mclegoman.perspective.client.widget.PerspectiveLogoWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PerspectiveConfigScreenHelper {
    public static GridWidget createTitle(MinecraftClient client, Screen PARENT_SCREEN, boolean showPageName, String pageName) {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(new PerspectiveLogoWidget());
        if (PerspectiveUpdateChecker.NEWER_VERSION_FOUND) {
            GRID_ADDER.add(new TextWidget(PerspectiveTranslation.getConfigTranslation("update.title", new Formatting[]{Formatting.BOLD, Formatting.RED}), client.textRenderer));
            Text NEW_VERSION_TEXT = PerspectiveTranslation.getConfigTranslation("update.description", new Object[]{PerspectiveUpdateChecker.LATEST_VERSION_FOUND}, new Formatting[]{Formatting.YELLOW});
            GRID_ADDER.add(new PressableTextWidget(GRID.getX() - (client.textRenderer.getWidth(NEW_VERSION_TEXT) / 2), GRID.getY(), client.textRenderer.getWidth(NEW_VERSION_TEXT), 9, NEW_VERSION_TEXT, (button -> ConfirmLinkScreen.open(PerspectiveUpdateChecker.DOWNLOAD_LINK, PARENT_SCREEN, true)), client.textRenderer));
        }
        if (showPageName) {
            GRID_ADDER.add(new EmptyWidget(2, 2));
            GRID_ADDER.add(new MultilineTextWidget(PerspectiveTranslation.getConfigTranslation(pageName), PerspectiveClientData.CLIENT.textRenderer).setCentered(true));
            GRID_ADDER.add(new EmptyWidget(2, 2));
        } else GRID_ADDER.add(new EmptyWidget(4, 4));

        return GRID;
    }
    public static GridWidget createTitle() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(new PerspectiveLogoWidget());
        GRID_ADDER.add(new EmptyWidget(4, 4));
        return GRID;
    }
}