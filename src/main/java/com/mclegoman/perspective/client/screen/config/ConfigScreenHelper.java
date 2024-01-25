/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.UpdateChecker;
import com.mclegoman.perspective.client.widget.LogoWidget;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ConfigScreenHelper {
	public static GridWidget createTitle(MinecraftClient client, Screen PARENT_SCREEN, boolean showPageName, String pageName, boolean experimental) {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
		GRID_ADDER.add(new LogoWidget(experimental));
		if (UpdateChecker.NEWER_VERSION_FOUND) {
			GRID_ADDER.add(new TextWidget(Translation.getConfigTranslation(Data.VERSION.getID(), "update.title", new Formatting[]{Formatting.BOLD, Formatting.RED}), client.textRenderer));
			Text NEW_VERSION_TEXT = Translation.getConfigTranslation(Data.VERSION.getID(), "update.description", new Object[]{UpdateChecker.LATEST_VERSION_FOUND}, new Formatting[]{Formatting.YELLOW});
			GRID_ADDER.add(new PressableTextWidget(GRID.getX() - (client.textRenderer.getWidth(NEW_VERSION_TEXT) / 2), GRID.getY(), client.textRenderer.getWidth(NEW_VERSION_TEXT), 9, NEW_VERSION_TEXT, (button -> ConfirmLinkScreen.open(PARENT_SCREEN, UpdateChecker.DOWNLOAD_LINK)), client.textRenderer));
		}
		if (showPageName) {
			GRID_ADDER.add(new EmptyWidget(2, 2));
			GRID_ADDER.add(new MultilineTextWidget(Translation.getConfigTranslation(Data.VERSION.getID(), pageName), ClientData.CLIENT.textRenderer).setCentered(true));
			GRID_ADDER.add(new EmptyWidget(2, 2));
		} else GRID_ADDER.add(new EmptyWidget(4, 4));
		return GRID;
	}

	public static GridWidget createTitle(boolean experimental) {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
		GRID_ADDER.add(new LogoWidget(experimental));
		GRID_ADDER.add(new EmptyWidget(4, 4));
		return GRID;
	}
}