/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config;

import com.mclegoman.perspective.client.ui.SplashesDataloader;
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
import com.mclegoman.perspective.client.ui.UIBackground;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.client.util.UpdateChecker;
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

import java.util.Random;

public class ConfigScreen extends Screen {
	private final Screen PARENT_SCREEN;
	private final GridWidget GRID;
	private boolean REFRESH;
	private boolean SHOULD_CLOSE;
	private int PAGE;
	public ConfigScreen(Screen PARENT, boolean REFRESH, int PAGE) {
		super(Text.literal(""));
		this.GRID = new GridWidget();
		this.PARENT_SCREEN = PARENT;
		this.REFRESH = REFRESH;
		this.PAGE = PAGE;
	}

	public void init() {
		try {
			GRID.getMainPositioner().alignHorizontalCenter().margin(0);
			GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
			GRID_ADDER.add(ScreenHelper.createTitle(client, new ConfigScreen(PARENT_SCREEN, true, PAGE), false, true));
			if (PAGE == 1) GRID_ADDER.add(createPageOne());
			else if (PAGE == 2) GRID_ADDER.add(createPageTwo());
			else SHOULD_CLOSE = true;
			GRID_ADDER.add(new EmptyWidget(4, 4));
			GRID_ADDER.add(createFooter());
			GRID.refreshPositions();
			GRID.forEachChild(this::addDrawableChild);
			initTabNavigation();
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to initialize config screen: {}", Data.VERSION.getID(), error);
		}
	}
	public void tick() {
		try {
			if (this.REFRESH) {
				ClientData.minecraft.setScreen(new ConfigScreen(PARENT_SCREEN, false, PAGE));
			}
			if (this.SHOULD_CLOSE) {
				ConfigHelper.saveConfig();
				ClientData.minecraft.setScreen(PARENT_SCREEN);
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to tick perspective$config screen: {}", Data.VERSION.getID(), error);
		}
	}
	private GridWidget createPageOne() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "zoom"), (button) -> ClientData.minecraft.setScreen(new ZoomConfigScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE), false))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "shaders"), (button) -> ClientData.minecraft.setScreen(new ShadersConfigScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE), false, new Formatting[]{Shader.getRandomColor()}, false))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "textured_entity"), (button) -> ClientData.minecraft.setScreen(new TexturedEntityConfigScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE), false))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "april_fools_prank"), (button) -> ClientData.minecraft.setScreen(new AprilFoolsPrankConfigScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE), false))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "hide"), (button) -> ClientData.minecraft.setScreen(new HideConfigScreen(new ConfigScreen(PARENT_SCREEN, false, PAGE), false))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "hold_perspective"), (button) -> ClientData.minecraft.setScreen(new HoldPerspectiveConfigScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE), false))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "information"), (button) -> ClientData.minecraft.setScreen(new InformationScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE), false))).build());
		ButtonWidget EXPERIMENTAL = ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "experimental"), (button) -> ClientData.minecraft.setScreen(new ExperimentalConfigScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE), false))).build();
		EXPERIMENTAL.active = ConfigHelper.EXPERIMENTS_AVAILABLE;
		GRID_ADDER.add(EXPERIMENTAL);
		return GRID;
	}

	private GridWidget createPageTwo() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "version_overlay", new Object[]{Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "version_overlay"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "version_overlay", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "version_overlay"));
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "position_overlay", new Object[]{Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "position_overlay"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "position_overlay", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "position_overlay"));
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "title_screen", new Object[]{Translation.getTitleScreenBackgroundTranslation(Data.VERSION.getID(), (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "title_screen"))}), (button) -> {
			UIBackground.cycleTitleScreenBackgroundType(!hasShiftDown());
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "ui_background", new Object[]{Translation.getUIBackgroundTranslation(Data.VERSION.getID(), (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "ui_background"))}), (button) -> {
			UIBackground.cycleUIBackgroundType(!hasShiftDown());
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "show_death_coordinates", new Object[]{Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "show_death_coordinates"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "show_death_coordinates", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "show_death_coordinates"));
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "tutorials", new Object[]{Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "tutorials"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "tutorials", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "tutorials"));
			this.REFRESH = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.VERSION.getID(), "tutorials", true))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "force_pride", new Object[]{Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "force_pride"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "force_pride", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "force_pride"));
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "detect_update_channel", new Object[]{Translation.getDetectUpdateChannelTranslation(Data.VERSION.getID(), (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "detect_update_channel"))}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "detect_update_channel", UpdateChecker.nextUpdateChannel());
			this.REFRESH = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.VERSION.getID(), "detect_update_channel", true))).build());
		return GRID;
	}

	private GridWidget createFooter() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(3);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "reset"), (button) -> {
			if (ConfigHelper.resetConfig()) this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "back"), (button) -> {
			if (PAGE <= 1) {
				this.SHOULD_CLOSE = true;
			}
			else {
				PAGE -= 1;
				this.REFRESH = true;
			}
		}).width(74).build());
		ButtonWidget NEXT = ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "next"), (button) -> {
			if (!(PAGE >= 2)) {
				PAGE += 1;
				this.REFRESH = true;
			}
		}).width(74).build();
		if (PAGE >= 2) NEXT.active = false;
		GRID_ADDER.add(NEXT);
		return GRID;
	}

	public void initTabNavigation() {
		SimplePositioningWidget.setPos(GRID, getNavigationFocus());
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
			if (PAGE <= 1) {
				this.SHOULD_CLOSE = true;
			}
			else {
				PAGE -= 1;
				this.REFRESH = true;
			}
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_F5) {
			UpdateChecker.checkForUpdates(Data.VERSION, true);
			this.REFRESH = true;
		}
		return super.keyReleased(keyCode, scanCode, modifiers);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
	}
}