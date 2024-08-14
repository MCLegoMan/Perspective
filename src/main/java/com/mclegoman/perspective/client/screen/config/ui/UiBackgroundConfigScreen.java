/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.ui;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.AbstractConfigScreen;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.ui.UIBackground;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.config.ConfigHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;

public class UiBackgroundConfigScreen extends AbstractConfigScreen {
	public UiBackgroundConfigScreen(Screen parentScreen, boolean refresh, boolean saveOnClose, int page) {
		super(parentScreen, refresh, saveOnClose, page);
	}
	public void init() {
		try {
			super.init();
			if (this.page == 1) this.gridAdder.add(createPageOne());
			else shouldClose = true;
			postInit();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize zoom config screen: {}", error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
	private GridWidget createPageOne() {
		GridWidget uiGrid = new GridWidget();
		uiGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder uiGridAdder = uiGrid.createAdder(1);
		uiGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "ui_background.title_screen", new Object[]{Translation.getTitleScreenBackgroundTranslation(Data.version.getID(), (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "title_screen"))}), (button) -> {
			UIBackground.cycleTitleScreenBackgroundType(!hasShiftDown());
			this.refresh = true;
		}).width(304).build());
		uiGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "ui_background.ui_background", new Object[]{Translation.getUIBackgroundTranslation(Data.version.getID(), (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "ui_background"))}), (button) -> {
			UIBackground.cycleUIBackgroundType(!hasShiftDown());
			this.refresh = true;
		}).width(304).build());
		uiGridAdder.add(new EmptyWidget(20, 20));
		uiGridAdder.add(new EmptyWidget(20, 20));
		return uiGrid;
	}
	public Screen getRefreshScreen() {
		return new UiBackgroundConfigScreen(this.parentScreen, false, false, this.page);
	}
	public Text getPageTitle() {
		return Translation.getConfigTranslation(Data.version.getID(), "ui_background");
	}
}