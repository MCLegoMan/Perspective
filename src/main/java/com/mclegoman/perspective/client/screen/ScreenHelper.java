/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.ui.PerspectiveLogo;
import com.mclegoman.perspective.client.util.UpdateChecker;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

public class ScreenHelper {
	public static GridWidget createTitle(MinecraftClient client, Screen parentScreen, @Nullable String pageName, boolean experimental, boolean updateMsg) {
		return createTitle(client, parentScreen, true, pageName, experimental, updateMsg);
	}
	public static GridWidget createTitle(MinecraftClient client, Screen parentScreen, boolean experimental, boolean updateMsg) {
		return createTitle(client, parentScreen, false, null, experimental, updateMsg);
	}
	public static GridWidget createTitle(MinecraftClient client, Screen parentScreen, boolean showPageName, @Nullable String pageName, boolean experimental, boolean updateMsg) {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter();
		GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
		GRID_ADDER.add(new PerspectiveLogo.Widget(experimental));
		if (UpdateChecker.NEWER_VERSION_FOUND && updateMsg) {
			GRID_ADDER.add(new TextWidget(Translation.getConfigTranslation(Data.version.getID(), "update.title", new Formatting[]{Formatting.BOLD, Formatting.RED}), client.textRenderer));
			Text NEW_VERSION_TEXT = Translation.getConfigTranslation(Data.version.getID(), "update.description", new Object[]{UpdateChecker.LATEST_VERSION_FOUND}, new Formatting[]{Formatting.YELLOW});
			GRID_ADDER.add(new PressableTextWidget(GRID.getX() - (client.textRenderer.getWidth(NEW_VERSION_TEXT) / 2), GRID.getY(), client.textRenderer.getWidth(NEW_VERSION_TEXT), 9, NEW_VERSION_TEXT, (button -> ConfirmLinkScreen.open(parentScreen, UpdateChecker.DOWNLOAD_LINK)), client.textRenderer));
		}
		if (showPageName && pageName != null) GRID_ADDER.add(new MultilineTextWidget(Translation.getConfigTranslation(Data.version.getID(), pageName), ClientData.minecraft.textRenderer).setCentered(true));
		GRID_ADDER.add(new EmptyWidget(4, 4));
		return GRID;
	}
}
