/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.shaders;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.ConfigScreenHelper;
import com.mclegoman.perspective.client.screen.config.toasts.UpdateCheckerScreen;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.shaders.ShaderRegistryValue;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.translation.TranslationType;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
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
	public ShadersConfigScreen(Screen PARENT, boolean SAVE_ON_CLOSE, boolean REFRESH) {
		super(Text.literal(""));
		this.GRID = new GridWidget();
		this.PARENT_SCREEN = PARENT;
		this.SAVE_ON_CLOSE = SAVE_ON_CLOSE;
		this.REFRESH = REFRESH;
	}
	public void init() {
		try {
			GRID.getMainPositioner().alignHorizontalCenter().margin(0);
			GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
			GRID_ADDER.add(ConfigScreenHelper.createTitle(ClientData.CLIENT, new ShadersConfigScreen(PARENT_SCREEN, SAVE_ON_CLOSE, true), true, "shaders", false));
			GRID_ADDER.add(createShaders());
			GRID_ADDER.add(createShaderOptions());
			GRID_ADDER.add(new EmptyWidget(4, 4));
			GRID_ADDER.add(createFooter());
			GRID.refreshPositions();
			GRID.forEachChild(this::addDrawableChild);
			initTabNavigation();
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize config>shaders screen: {}", Data.PERSPECTIVE_VERSION.getID(), error);
		}
	}
	public void tick() {
		try {
			if (this.REFRESH) {
				ClientData.CLIENT.setScreen(new ShadersConfigScreen(PARENT_SCREEN, SAVE_ON_CLOSE, false));
			}
			if (this.SHOULD_CLOSE) {
				if (this.SAVE_ON_CLOSE) ConfigHelper.saveConfig(false);
				ClientData.CLIENT.setScreen(PARENT_SCREEN);
			}
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to tick perspective$config$shaders screen: {}", Data.PERSPECTIVE_VERSION.getID(), error);
		}
	}
	private GridWidget createShaders() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("shaders.shader", new Object[]{Shader.getShaderName(Shader.superSecretSettingsIndex)}, new Formatting[]{Shader.getRandomColor()}), (button) -> ClientData.CLIENT.setScreen(new ShaderSelectionConfigScreen(new ShadersConfigScreen(PARENT_SCREEN, SAVE_ON_CLOSE, false), -1))).width(280).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("shaders.random"), (button) -> {
			Shader.random(true, false, false);
			this.REFRESH = true;
		}).width(20).build());
		return GRID;
	}
	private GridWidget createShaderOptions() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("shaders.mode", new Object[]{Translation.getShaderModeTranslation((String) ConfigHelper.getConfig("super_secret_settings_mode")), ConfigHelper.getConfig("super_secret_settings_mode").equals("screen") ? Translation.getVariableTranslation((boolean) Shader.getShaderData(ShaderRegistryValue.DISABLE_SCREEN_MODE), TranslationType.DISABLE_SCREEN_MODE) : ""}), (button) -> {
			Shader.cycleShaderModes();
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("shaders.toggle", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig("super_secret_settings_enabled"), TranslationType.ENDISABLE)}), (button) -> {
			Shader.toggle(true, false, false, false);
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("shaders.show_name", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig("super_secret_settings_show_name"), TranslationType.ONFF)}), (button) -> {
			ConfigHelper.setConfig("super_secret_settings_show_name", !(boolean) ConfigHelper.getConfig("super_secret_settings_show_name"));
			this.REFRESH = true;
		}).width(304).build(), 2);
		return GRID;
	}
	private GridWidget createFooter() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("reset"), (button) -> {
			ConfigHelper.resetConfig();
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("back"), (button) -> this.SHOULD_CLOSE = true).build());
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
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_F5) {
			ClientData.CLIENT.setScreen(new UpdateCheckerScreen(this));
			this.REFRESH = false;
		}
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
	}
}