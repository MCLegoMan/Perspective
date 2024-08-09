/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.zoom;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.screen.AbstractConfigScreen;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.ScreenHelper;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;

public class ZoomConfigScreen extends AbstractConfigScreen {
	public ZoomConfigScreen(Screen parentScreen, boolean refresh) {
		super(parentScreen, refresh, false);
	}
	public void init() {
		try {
			super.init();
			this.gridAdder.add(ScreenHelper.createTitle(ClientData.minecraft, getRefreshScreen(), "zoom", false, true));
			this.gridAdder.add(createZoom());
			postInit();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize config screen: {}", error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
	public void tick() {
		try {
			if (this.refresh) {
				ClientData.minecraft.setScreen(new ZoomConfigScreen(parentScreen, false));
			}
			if (this.shouldClose) {
				ClientData.minecraft.setScreen(parentScreen);
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to tick config$zoom screen: {}", error));
		}
	}
	private GridWidget createZoom() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder gridAdder = GRID.createAdder(2);
		double ZOOM_LEVEL = (double) ((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_level")) / 100;
		gridAdder.add(new SliderWidget(gridAdder.getGridWidget().getX(), gridAdder.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.version.getID(), "zoom.level", new Object[]{Text.literal((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_level") + "%")}, false), ZOOM_LEVEL) {
			@Override
			protected void updateMessage() {
				setMessage(Translation.getConfigTranslation(Data.version.getID(),  "zoom.level", new Object[]{Text.literal((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_level") + "%")}, false));
			}

			@Override
			protected void applyValue() {
				ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "zoom_level", (int) ((value) * 100));
			}
		}, 1);
		double zoomIncrementSize = (double) ((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_increment_size") - 1) / 9;
		SliderWidget zoomIncrementSizeWidget = new SliderWidget(gridAdder.getGridWidget().getX(), gridAdder.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.version.getID(), "zoom.increment_size", new Object[]{Text.literal(String.valueOf((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_increment_size")))}, false), zoomIncrementSize) {
			protected void updateMessage() {
				setMessage(Translation.getConfigTranslation(Data.version.getID(),  "zoom.increment_size", new Object[]{Text.literal(String.valueOf((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_increment_size")))}, false));
			}
			protected void applyValue() {
				ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "zoom_increment_size", (int) ((value) * 9) + 1);
			}
		};
		zoomIncrementSizeWidget.setTooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "zoom.increment_size", true)));
		gridAdder.add(zoomIncrementSizeWidget, 1);
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom.type", new Object[]{Translation.getZoomTypeTranslation(Zoom.getZoomType().getNamespace(), Zoom.getZoomType().getPath())}), (button) -> {
			Zoom.cycleZoomType(!hasShiftDown());
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "zoom.type", new Object[]{Translation.getZoomTypeTranslation(Zoom.getZoomType().getNamespace(), Zoom.getZoomType().getPath(), true)}, true))).build(), 1);
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom.transition", new Object[]{Translation.getZoomTransitionTranslation(Data.version.getID(), (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_transition"))}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "zoom_transition", Zoom.nextTransition());
			this.refresh = true;
		}).build(), 1);
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom.scale_mode", new Object[]{Translation.getZoomScaleModeTranslation(Data.version.getID(), (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_scale_mode"))}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "zoom_scale_mode", Zoom.nextScaleMode());
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "zoom.scale_mode", new Object[]{Translation.getConfigTranslation(Data.version.getID(), "zoom.scale_mode." + ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_scale_mode"), true)}, true))).build(), 1);
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom.hide_hud", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_hide_hud"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "zoom_hide_hud", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_hide_hud"));
			this.refresh = true;
		}).build(), 1);
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom.show_percentage", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_show_percentage"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "zoom_show_percentage", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_show_percentage"));
			this.refresh = true;
		}).build(), 1);
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom.enabled", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_enabled"), Translation.Type.ENDISABLE)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "zoom_enabled", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_enabled"));
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "zoom.enabled", new Object[]{Translation.getConfigTranslation(Data.version.getID(), "zoom.enabled." + ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_enabled"), true)}, true))).build(), 1);
		return GRID;
	}
	public Screen getRefreshScreen() {
		return new ZoomConfigScreen(this.parentScreen, this.refresh);
	}
	public Text getPageTitle() {
		return Translation.getConfigTranslation(Data.version.getID(), "zoom");
	}
}