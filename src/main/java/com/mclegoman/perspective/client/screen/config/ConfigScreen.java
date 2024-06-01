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
import com.mclegoman.perspective.client.keybindings.Keybindings;
import com.mclegoman.perspective.client.util.Update;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class ConfigScreen extends Screen {
	private final Screen parentScreen;
	private final GridWidget grid;
	private boolean refresh;
	private boolean shouldClose;
	private int page;
	public ConfigScreen(Screen PARENT, boolean REFRESH, int PAGE) {
		super(Text.literal(""));
		this.grid = new GridWidget();
		this.parentScreen = PARENT;
		this.refresh = REFRESH;
		this.page = PAGE;
	}

	public void init() {
		try {
			grid.getMainPositioner().alignHorizontalCenter().margin(0);
			GridWidget.Adder gridAdder = grid.createAdder(1);
			gridAdder.add(ScreenHelper.createTitle(client, new ConfigScreen(parentScreen, true, page), false, true));
			if (page == 1) gridAdder.add(createPageOne());
			else if (page == 2) gridAdder.add(createPageTwo());
			else shouldClose = true;
			gridAdder.add(new EmptyWidget(4, 4));
			gridAdder.add(createFooter());
			grid.refreshPositions();
			grid.forEachChild(this::addDrawableChild);
			initTabNavigation();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize config screen: {}", error));
		}
	}
	public void tick() {
		try {
			if (this.refresh) {
				ClientData.minecraft.setScreen(new ConfigScreen(parentScreen, false, page));
			}
			if (this.shouldClose) {
				ConfigHelper.saveConfig();
				ClientData.minecraft.setScreen(parentScreen);
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to tick perspective$config screen: {}", error));
		}
	}
	private GridWidget createPageOne() {
		GridWidget grid = new GridWidget();
		grid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder gridAdder = grid.createAdder(2);
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom"), (button) -> ClientData.minecraft.setScreen(new ZoomConfigScreen(new ConfigScreen(parentScreen, true, page), false))).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "shaders"), (button) -> ClientData.minecraft.setScreen(new ShadersConfigScreen(new ConfigScreen(parentScreen, true, page), false, new Formatting[]{Shader.getRandomColor()}, false))).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "textured_entity"), (button) -> ClientData.minecraft.setScreen(new TexturedEntityConfigScreen(new ConfigScreen(parentScreen, true, page), false))).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "april_fools_prank"), (button) -> ClientData.minecraft.setScreen(new AprilFoolsPrankConfigScreen(new ConfigScreen(parentScreen, true, page), false))).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hide"), (button) -> ClientData.minecraft.setScreen(new HideConfigScreen(new ConfigScreen(parentScreen, false, page), false))).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hold_perspective"), (button) -> ClientData.minecraft.setScreen(new HoldPerspectiveConfigScreen(new ConfigScreen(parentScreen, true, page), false))).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "information"), (button) -> ClientData.minecraft.setScreen(new InformationScreen(new ConfigScreen(parentScreen, true, page), false))).build());
		ButtonWidget EXPERIMENTAL = ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "experimental"), (button) -> ClientData.minecraft.setScreen(new ExperimentalConfigScreen(new ConfigScreen(parentScreen, true, page), false))).build();
		EXPERIMENTAL.active = ConfigHelper.experimentsAvailable;
		gridAdder.add(EXPERIMENTAL);
		return grid;
	}

	private GridWidget createPageTwo() {
		GridWidget grid = new GridWidget();
		grid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder gridAdder = grid.createAdder(2);
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "overlays"), (button) -> ClientData.minecraft.setScreen(new OverlaysConfigScreen(new ConfigScreen(parentScreen, false, page), false))).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "ui_background"), (button) -> ClientData.minecraft.setScreen(new UiBackgroundConfigScreen(new ConfigScreen(parentScreen, false, page), false))).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "luminance"), (button) -> ClientData.minecraft.setScreen(new com.mclegoman.luminance.client.screen.config.ConfigScreen(new ConfigScreen(parentScreen, false, page), false, SplashesDataloader.getSplashText(), PerspectiveLogo.isPride()))).build());
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
		return grid;
	}

	private GridWidget createFooter() {
		GridWidget grid = new GridWidget();
		grid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder gridAdder = grid.createAdder(3);
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "reset"), (button) -> {
			if (ConfigHelper.resetConfig()) this.refresh = true;
		}).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "back"), (button) -> {
			if (page <= 1) {
				this.shouldClose = true;
			}
			else {
				page -= 1;
				this.refresh = true;
			}
		}).width(74).build());
		ButtonWidget next = ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "next"), (button) -> {
			if (!(page >= 2)) {
				page += 1;
				this.refresh = true;
			}
		}).width(74).build();
		if (page >= 2) next.active = false;
		gridAdder.add(next);
		return grid;
	}

	public void initTabNavigation() {
		SimplePositioningWidget.setPos(grid, getNavigationFocus());
	}

	public Text getNarratedTitle() {
		return ScreenTexts.joinSentences();
	}

	public boolean shouldCloseOnEsc() {
		return false;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == KeyBindingHelper.getBoundKeyOf(Keybindings.openConfig).getCode()) {
			if (page <= 1) {
				this.shouldClose = true;
			}
			else {
				page -= 1;
				this.refresh = true;
			}
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_F5) {
			if (hasControlDown()) ConfigHelper.reloadConfig(true);
			else Update.checkForUpdates(Data.version, true);
			this.refresh = true;
		}
		return super.keyReleased(keyCode, scanCode, modifiers);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		if (ConfigHelper.showReloadOverlay) context.drawTextWithShadow(textRenderer, Translation.getConfigTranslation(Data.version.getID(), "reload"), 2, 2, 0xFFFFFF);
	}
}