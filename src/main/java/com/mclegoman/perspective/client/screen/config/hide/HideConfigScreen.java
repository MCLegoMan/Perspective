/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.hide;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.screen.config.AbstractConfigScreen;
import com.mclegoman.perspective.client.screen.widget.ConfigSliderWidget;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hide.Hide;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;

public class HideConfigScreen extends AbstractConfigScreen {
	public HideConfigScreen(Screen parentScreen, boolean refresh, boolean saveOnClose, int page) {
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
		GridWidget hideGrid = new GridWidget();
		hideGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder hideGridAdder = hideGrid.createAdder(2);
		hideGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hide.block_outline", new Object[]{Translation.getVariableTranslation(Data.version.getID(), !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_block_outline"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "hide_block_outline", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_block_outline"));
			this.refresh = true;
		}).build());
		double blockOutlineLevel = (double) ((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "block_outline")) / 100;
		hideGridAdder.add(new ConfigSliderWidget(hideGridAdder.getGridWidget().getX(), hideGridAdder.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.version.getID(), "hide.block_outline", new Object[]{Text.literal((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "block_outline") + "%")}, false), blockOutlineLevel) {
			@Override
			protected void updateMessage() {
				setMessage(Translation.getConfigTranslation(Data.version.getID(),  "hide.block_outline.level", new Object[]{Text.literal((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "block_outline") + "%")}, false));
			}
			@Override
			protected void applyValue() {
				ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "block_outline", (int) ((value) * 100));
			}
		});
		hideGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hide.rainbow_block_outline", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "rainbow_block_outline"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "rainbow_block_outline", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "rainbow_block_outline"));
			this.refresh = true;
		}).build());
		hideGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hide.crosshair", new Object[]{Translation.getCrosshairTranslation(Data.version.getID(), (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "crosshair_type"))}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "crosshair_type", Hide.nextCrosshairMode());
			this.refresh = true;
		}).build());
		hideGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hide.hide_armor", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_armor"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "hide_armor", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_armor"));
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "hide.hide_armor", true))).build());
		hideGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hide.hide_nametags", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_nametags"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "hide_nametags", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_nametags"));
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "hide.hide_nametags", true))).build());
		hideGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hide.hide_players", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_players"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "hide_players", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_players"));
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "hide.hide_players", true))).build());
		hideGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hide.show_message", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_show_message"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "hide_show_message", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_show_message"));
			this.refresh = true;
		}).build());
		return hideGrid;
	}
	public Screen getRefreshScreen() {
		return new HideConfigScreen(this.parentScreen, false, false, this.page);
	}
	public String getPageId() {
		return "hide";
	}
}