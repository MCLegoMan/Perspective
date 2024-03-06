/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.shaders;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.ScreenHelper;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.shaders.ShaderDataLoader;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.translation.TranslationType;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.client.util.UpdateChecker;
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
	private final Screen PARENT_SCREEN;
	private final GridWidget GRID;
	private final boolean SAVE_ON_CLOSE;
	private boolean SHOULD_CLOSE;
	private boolean REFRESH;
	private boolean REVERSE;
	private Formatting[] formattings;
	public ShadersConfigScreen(Screen PARENT, boolean SAVE_ON_CLOSE, Formatting[] formattings, boolean REFRESH) {
		super(Text.literal(""));
		this.GRID = new GridWidget();
		this.PARENT_SCREEN = PARENT;
		this.SAVE_ON_CLOSE = SAVE_ON_CLOSE;
		this.REFRESH = REFRESH;
		this.formattings = formattings;
	}
	public void init() {
		try {
			GRID.getMainPositioner().alignHorizontalCenter().margin(0);
			GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
			GRID_ADDER.add(ScreenHelper.createTitle(ClientData.CLIENT, new ShadersConfigScreen(PARENT_SCREEN, SAVE_ON_CLOSE, formattings, true), true, "shaders", false, true));
			GRID_ADDER.add(createShaders());
			GRID_ADDER.add(createShaderOptions());
			GRID_ADDER.add(new EmptyWidget(4, 4));
			GRID_ADDER.add(createFooter());
			GRID.refreshPositions();
			GRID.forEachChild(this::addDrawableChild);
			initTabNavigation();
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to initialize config>shaders screen: {}", Data.VERSION.getID(), error);
		}
	}
	public void tick() {
		try {
			if (this.REFRESH) {
				ClientData.CLIENT.setScreen(new ShadersConfigScreen(PARENT_SCREEN, SAVE_ON_CLOSE, formattings, false));
			}
			if (this.SHOULD_CLOSE) {
				if (this.SAVE_ON_CLOSE) ConfigHelper.saveConfig();
				ClientData.CLIENT.setScreen(PARENT_SCREEN);
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to tick perspective$config$shaders screen: {}", Data.VERSION.getID(), error);
		}
	}
	private GridWidget createShaders() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(3);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "shaders.cycle", new Object[]{Shader.getTranslatedShaderName(Shader.superSecretSettingsIndex)}, formattings), (button) -> {
			Shader.cycle(true, !this.REVERSE, true, false, false);
			this.formattings = new Formatting[]{Shader.getRandomColor()};
			this.REFRESH = true;
		}).width(256).build());

		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "shaders.list"), (button) -> ClientData.CLIENT.setScreen(new ShaderSelectionConfigScreen(new ShadersConfigScreen(PARENT_SCREEN, SAVE_ON_CLOSE, formattings, false), new Formatting[]{Shader.getRandomColor()}, -1, (boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_selection_blur")))).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.VERSION.getID(), "shaders.list", true))).width(20).build());

		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "shaders.random"), (button) -> {
			Shader.random(true, false, false);
			this.REFRESH = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.VERSION.getID(), "shaders.random", true))).width(20).build());

		return GRID;
	}
	private GridWidget createShaderOptions() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "shaders.mode", new Object[]{Translation.getShaderModeTranslation(Data.VERSION.getID(), (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_mode")), ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_mode").equals("screen") ? Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) Shader.get(ShaderDataLoader.RegistryValue.DISABLE_SCREEN_MODE), TranslationType.DISABLE_SCREEN_MODE) : ""}), (button) -> {
			Shader.cycleShaderModes();
			this.REFRESH = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.VERSION.getID(), "shaders.mode", new Object[]{Translation.getConfigTranslation(Data.VERSION.getID(), "shaders.mode." + ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_mode"), true)}, true))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "shaders.play_sound", new Object[]{Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_sound"), TranslationType.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_sound", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_sound"));
			this.REFRESH = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.VERSION.getID(), "shaders.play_sound", new Object[]{Translation.getConfigTranslation(Data.VERSION.getID(), "shaders.play_sound." + ((boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_sound") ? "on" : "off"), true)}, true))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "shaders.show_name", new Object[]{Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_show_name"), TranslationType.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_show_name", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_show_name"));
			this.REFRESH = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.VERSION.getID(), "shaders.show_name", new Object[]{Translation.getConfigTranslation(Data.VERSION.getID(), "shaders.show_name." + ((boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_show_name") ? "on" : "off"), true)}, true))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "shaders.toggle", new Object[]{Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_enabled"), TranslationType.ENDISABLE)}), (button) -> {
			Shader.toggle(true, false, false, false);
			this.REFRESH = true;
		}).build());
		return GRID;
	}
	private GridWidget createFooter() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "reset"), (button) -> {
			if (ConfigHelper.resetConfig()) this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "back"), (button) -> this.SHOULD_CLOSE = true).build());
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
		if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == KeyBindingHelper.getBoundKeyOf(Keybindings.OPEN_CONFIG).getCode())
			this.SHOULD_CLOSE = true;
		if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT)
			this.REVERSE = true;
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_F5) {
			UpdateChecker.checkForUpdates(Data.VERSION, true);
			this.REFRESH = true;
		}
		if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT)
			this.REVERSE = false;
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
	}
}