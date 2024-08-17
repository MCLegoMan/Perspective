/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.overlays;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.AbstractConfigScreen;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.config.ConfigHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;

public class OverlaysConfigScreen extends AbstractConfigScreen {
	public OverlaysConfigScreen(Screen parentScreen, boolean refresh, boolean saveOnClose, int page) {
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
		GridWidget overlaysGrid = new GridWidget();
		overlaysGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder overlaysGridAdder = overlaysGrid.createAdder(1);
		overlaysGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "overlays.version_overlay", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "version_overlay"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "version_overlay", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "version_overlay"));
			this.refresh = true;
		}).width(304).build());
		overlaysGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "overlays.position_overlay", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "position_overlay"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "position_overlay", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "position_overlay"));
			this.refresh = true;
		}).width(304).build());
		overlaysGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "overlays.day_overlay", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "day_overlay"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "day_overlay", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "day_overlay"));
			this.refresh = true;
		}).width(304).build());
		overlaysGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "overlays.biome_overlay", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "biome_overlay"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "biome_overlay", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "biome_overlay"));
			this.refresh = true;
		}).width(304).build());
		return overlaysGrid;
	}
	public Screen getRefreshScreen() {
		return new OverlaysConfigScreen(this.parentScreen, false, false, this.page);
	}
	public String getPageId() {
		return "overlays";
	}
}