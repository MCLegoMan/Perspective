/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.logo.PerspectiveLogo;
import com.mclegoman.perspective.client.screen.config.overlays.OverlaysConfigScreen;
import com.mclegoman.perspective.client.screen.config.ui.UiBackgroundConfigScreen;
import com.mclegoman.perspective.client.logo.SplashesDataloader;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.ScreenHelper;
import com.mclegoman.perspective.client.screen.config.april_fools_prank.AprilFoolsPrankConfigScreen;
import com.mclegoman.perspective.client.screen.config.experimental.ExperimentalConfigScreen;
import com.mclegoman.perspective.client.screen.config.hide.HideConfigScreen;
import com.mclegoman.perspective.client.screen.config.hold_perspective.HoldPerspectiveConfigScreen;
import com.mclegoman.perspective.client.screen.config.information.InformationScreen;
import com.mclegoman.perspective.client.screen.config.shaders.ShadersConfigScreen;
import com.mclegoman.perspective.client.screen.config.textured_entity.TexturedEntityConfigScreen;
import com.mclegoman.perspective.client.screen.config.zoom.ZoomConfigScreen;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.Update;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ConfigScreen extends AbstractConfigScreen {
	public ConfigScreen(Screen parentScreen, boolean refresh, boolean saveOnClose, int page) {
		super(parentScreen, refresh, saveOnClose, page);
	}
	public void init() {
		try {
			super.init();
			if (this.page == 1) this.gridAdder.add(createPageOne());
			else if (this.page == 2) this.gridAdder.add(createPageTwo());
			else shouldClose = true;
			postInit();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize config screen: {}", error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
	private GridWidget createPageOne() {
		GridWidget grid = new GridWidget();
		grid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder gridAdder = grid.createAdder(2);
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom"), (button) -> ClientData.minecraft.setScreen(new ZoomConfigScreen(getRefreshScreen(), false, false, 1))).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "shaders"), (button) -> ClientData.minecraft.setScreen(new ShadersConfigScreen(getRefreshScreen(), false, false, new Formatting[]{Shader.getRandomColor()}))).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "textured_entity"), (button) -> ClientData.minecraft.setScreen(new TexturedEntityConfigScreen(getRefreshScreen(), false))).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "april_fools_prank"), (button) -> ClientData.minecraft.setScreen(new AprilFoolsPrankConfigScreen(getRefreshScreen(), false, false, 1))).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hide"), (button) -> ClientData.minecraft.setScreen(new HideConfigScreen(getRefreshScreen(), false, false, 1))).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hold_perspective"), (button) -> ClientData.minecraft.setScreen(new HoldPerspectiveConfigScreen(getRefreshScreen(), false))).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "overlays"), (button) -> ClientData.minecraft.setScreen(new OverlaysConfigScreen(getRefreshScreen(), false))).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "ui_background"), (button) -> ClientData.minecraft.setScreen(new UiBackgroundConfigScreen(getRefreshScreen(), false, false, 1))).build());
		return grid;
	}
	private GridWidget createPageTwo() {
		GridWidget grid = new GridWidget();
		grid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder gridAdder = grid.createAdder(2);
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "luminance"), (button) -> ClientData.minecraft.setScreen(new com.mclegoman.luminance.client.screen.config.ConfigScreen(getRefreshScreen(), false, SplashesDataloader.getSplashText(), PerspectiveLogo.isPride()))).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "show_death_coordinates", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "show_death_coordinates"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "show_death_coordinates", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "show_death_coordinates"));
			this.refresh = true;
		}).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "tutorials", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "tutorials"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "tutorials", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "tutorials"));
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "tutorials", true))).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "force_pride", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "force_pride"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "force_pride", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "force_pride"));
			this.refresh = true;
		}).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "detect_update_channel", new Object[]{Translation.getDetectUpdateChannelTranslation(Data.version.getID(), (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "detect_update_channel"))}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "detect_update_channel", Update.nextUpdateChannel());
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "detect_update_channel", true))).width(304).build(), 2);
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "information"), (button) -> ClientData.minecraft.setScreen(new InformationScreen(getRefreshScreen(), false, false))).build());
		ButtonWidget experimental = ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "experimental"), (button) -> ClientData.minecraft.setScreen(new ExperimentalConfigScreen(getRefreshScreen(), false, false, 1))).build();
		experimental.active = ConfigHelper.experimentsAvailable;
		gridAdder.add(experimental);
		return grid;
	}
	public Screen getRefreshScreen() {
		return new ConfigScreen(this.parentScreen, false, this.saveOnClose, this.page);
	}
	public Text getPageTitle() {
		return Translation.getConfigTranslation(Data.version.getID(), "config");
	}
	public int getMaxPage() {
		return 2;
	}
}