/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.logo.PerspectiveLogo;
import com.mclegoman.perspective.client.util.Update;
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
		GridWidget titleGrid = new GridWidget();
		titleGrid.getMainPositioner().alignHorizontalCenter();
		GridWidget.Adder titleGridAdder = titleGrid.createAdder(1);
		if (Update.isNewerVersionFound() && updateMsg) {
			titleGridAdder.add(new TextWidget(Translation.getConfigTranslation(Data.version.getID(), "update.title", new Formatting[]{Formatting.BOLD, Formatting.RED}), client.textRenderer));
			Text NEW_VERSION_TEXT = Translation.getConfigTranslation(Data.version.getID(), "update.description", new Object[]{Update.latestVersionFound}, new Formatting[]{Formatting.YELLOW});
			titleGridAdder.add(new PressableTextWidget(titleGrid.getX() - (client.textRenderer.getWidth(NEW_VERSION_TEXT) / 2), titleGrid.getY(), client.textRenderer.getWidth(NEW_VERSION_TEXT), 9, NEW_VERSION_TEXT, (button -> ConfirmLinkScreen.open(parentScreen, Update.downloadLink)), client.textRenderer));
		}
		return titleGrid;
	}
}
