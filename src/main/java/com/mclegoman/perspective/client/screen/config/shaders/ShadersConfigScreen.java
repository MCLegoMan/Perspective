/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.shaders;

import com.mclegoman.luminance.client.shaders.ShaderRegistry;
import com.mclegoman.luminance.client.shaders.Shaders;
import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.ScreenHelper;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.keybindings.Keybindings;
import com.mclegoman.perspective.client.util.Update;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class ShadersConfigScreen extends Screen {
	private final Screen parentScreen;
	private final GridWidget grid;
	private final boolean saveOnClose;
	private boolean shouldClose;
	private boolean refresh;
	private boolean reverse;
	private Formatting[] formattings;
	public ShadersConfigScreen(Screen PARENT, boolean SAVE_ON_CLOSE, Formatting[] formattings, boolean REFRESH) {
		super(Text.literal(""));
		this.grid = new GridWidget();
		this.parentScreen = PARENT;
		this.saveOnClose = SAVE_ON_CLOSE;
		this.refresh = REFRESH;
		this.formattings = formattings;
	}
	public void init() {
		try {
			grid.getMainPositioner().alignHorizontalCenter().margin(0);
			GridWidget.Adder GRID_ADDER = grid.createAdder(1);
			GRID_ADDER.add(ScreenHelper.createTitle(ClientData.minecraft, new ShadersConfigScreen(parentScreen, saveOnClose, formattings, true), "shaders", false, true));
			GRID_ADDER.add(createShaders());
			GRID_ADDER.add(createShaderOptions());
			GRID_ADDER.add(new EmptyWidget(4, 4));
			GRID_ADDER.add(createFooter());
			grid.refreshPositions();
			grid.forEachChild(this::addDrawableChild);
			initTabNavigation();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize config>shaders screen: {}", error));
		}
	}
	public void tick() {
		try {
			if (this.refresh) {
				ClientData.minecraft.setScreen(new ShadersConfigScreen(parentScreen, saveOnClose, formattings, false));
			}
			if (this.shouldClose) {
				if (this.saveOnClose) ConfigHelper.saveConfig();
				ClientData.minecraft.setScreen(parentScreen);
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to tick perspective$config$shaders screen: {}", error));
		}
	}
	private GridWidget createShaders() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(3);
		ButtonWidget cycleShaders = ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "shaders.cycle", new Object[]{Shaders.getShaderName(Shader.superSecretSettingsIndex)}, formattings), (button) -> {
			Shader.cycle(true, !this.reverse, true, false, false);
			this.formattings = new Formatting[]{Shader.getRandomColor()};
			this.refresh = true;
		}).width(256).build();
		cycleShaders.active = Shader.isShaderButtonsEnabled();
		GRID_ADDER.add(cycleShaders);
		ButtonWidget listShaders = ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "shaders.list"), (button) -> ClientData.minecraft.setScreen(new ShaderSelectionConfigScreen(new ShadersConfigScreen(parentScreen, saveOnClose, formattings, false), new Formatting[]{Shader.getRandomColor()}, -1, (boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_selection_blur")))).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "shaders.list", true))).width(20).build();
		listShaders.active = Shader.isShaderButtonsEnabled();
		GRID_ADDER.add(listShaders);
		ButtonWidget randomShader = ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "shaders.random"), (button) -> {
			Shader.random(true, false, false);
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "shaders.random", true))).width(20).build();
		randomShader.active = Shader.isShaderButtonsEnabled();
		GRID_ADDER.add(randomShader);
		return GRID;
	}
	private GridWidget createShaderOptions() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "shaders.mode", new Object[]{Translation.getShaderModeTranslation(Data.version.getID(), (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_mode")), ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_mode").equals("screen") ? Translation.getVariableTranslation(Data.version.getID(), (boolean) Shaders.get(Shader.superSecretSettingsIndex, ShaderRegistry.DISABLE_GAME_RENDERTYPE), Translation.Type.DISABLE_SCREEN_MODE) : ""}), (button) -> {
			Shader.cycleShaderModes();
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "shaders.mode", new Object[]{Translation.getConfigTranslation(Data.version.getID(), "shaders.mode." + ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_mode"), true)}, true))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "shaders.play_sound", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_sound"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_sound", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_sound"));
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "shaders.play_sound", new Object[]{Translation.getConfigTranslation(Data.version.getID(), "shaders.play_sound." + ((boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_sound") ? "on" : "off"), true)}, true))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "shaders.show_name", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_show_name"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_show_name", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_show_name"));
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "shaders.show_name", new Object[]{Translation.getConfigTranslation(Data.version.getID(), "shaders.show_name." + ((boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_show_name") ? "on" : "off"), true)}, true))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "shaders.toggle", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_enabled"), Translation.Type.ENDISABLE)}), (button) -> {
			Shader.toggle(true, false, false, false);
			this.refresh = true;
		}).build());
		return GRID;
	}
	private GridWidget createFooter() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "reset"), (button) -> {
			if (ConfigHelper.resetConfig()) this.refresh = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "back"), (button) -> this.shouldClose = true).build());
		return GRID;
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
		if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == KeyBindingHelper.getBoundKeyOf(Keybindings.openConfig).getCode())
			this.shouldClose = true;
		if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT)
			this.reverse = true;
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_F5) {
			if (hasControlDown()) ConfigHelper.reloadConfig(true);
			else Update.checkForUpdates(Data.version, true);
			this.refresh = true;
		}
		if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT)
			this.reverse = false;
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		if (ConfigHelper.showReloadOverlay) context.drawTextWithShadow(textRenderer, Translation.getConfigTranslation(Data.version.getID(), "reload"), 2, 2, 0xFFFFFF);
	}
}