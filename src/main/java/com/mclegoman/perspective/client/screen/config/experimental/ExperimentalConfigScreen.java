/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.experimental;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.screen.config.AbstractConfigScreen;
import com.mclegoman.perspective.client.screen.config.ambience.AmbienceConfigScreen;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.util.Formatting;

public class ExperimentalConfigScreen extends AbstractConfigScreen {
	public ExperimentalConfigScreen(Screen parentScreen, boolean refresh, boolean saveOnClose, int page) {
		super(parentScreen, refresh, saveOnClose, page);
	}
	public void init() {
		try {
			super.init();
			if (this.page == 1) {
				if (ConfigHelper.experimentsAvailable) this.gridAdder.add(createPageOne());
				else this.gridAdder.add(createEmpty());
			} else shouldClose = true;
			postInit();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize zoom config screen: {}", error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
	private GridWidget createEmpty() {
		GridWidget experimentalGrid = new GridWidget();
		experimentalGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder experimentalGridAdder = experimentalGrid.createAdder(2);
		experimentalGridAdder.add(new MultilineTextWidget(Translation.getConfigTranslation(Data.version.getID(), "experimental.none", new Formatting[]{Formatting.RED, Formatting.BOLD}), ClientData.minecraft.textRenderer).setCentered(true));
		return experimentalGrid;
	}
	private GridWidget createPageOne() {
		GridWidget experimentalGrid = new GridWidget();
		experimentalGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder experimentalGridAdder = experimentalGrid.createAdder(2);
		experimentalGridAdder.add(new MultilineTextWidget(Translation.getConfigTranslation(Data.version.getID(), "experimental.warning", new Formatting[]{Formatting.RED, Formatting.BOLD}), ClientData.minecraft.textRenderer).setCentered(true), 2);

		experimentalGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "experimental.ambience", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.experimental, "ambience"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.experimental, "ambience", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.experimental, "ambience"));
			refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "experimental.ambience", true))).build(), 1);

		experimentalGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "ambience"), (button) -> ClientData.minecraft.setScreen(new AmbienceConfigScreen(getRefreshScreen(), false, false, 1))).build());

		return experimentalGrid;
	}
	protected GridWidget createFooter() {
		GridWidget footerGrid = new GridWidget();
		footerGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder footerGridAdder = footerGrid.createAdder(3);
		footerGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "reset.experiments"), (button) -> {
			if (ConfigHelper.resetExperiments()) this.refresh = true;
		}).build());
		footerGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "back"), (button) -> {
			if (this.page <= 1) {
				this.shouldClose = true;
			}
			else {
				this.page -= 1;
				this.refresh = true;
			}
		}).width(73).build());
		ButtonWidget nextButtonWidget = ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "next"), (button) -> {
			if (!(this.page >= getMaxPage())) {
				this.page += 1;
				this.refresh = true;
			}
		}).width(73).build();
		if (this.page >= getMaxPage()) nextButtonWidget.active = false;
		footerGridAdder.add(nextButtonWidget);
		return footerGrid;
	}
	public boolean getExperimental() {
		return true;
	}
	public Screen getRefreshScreen() {
		return new ExperimentalConfigScreen(this.parentScreen, false, false, this.page);
	}
	public String getPageId() {
		return "experimental";
	}
}