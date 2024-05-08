/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.experimental;

import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.ConfigScreenHelper;
import com.mclegoman.perspective.client.screen.config.toasts.UpdateCheckerScreen;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class ExperimentalConfigScreen extends Screen {
	private final Screen PARENT_SCREEN;
	private final GridWidget GRID;
	private boolean REFRESH;
	private boolean SHOULD_CLOSE;
	public ExperimentalConfigScreen(Screen PARENT, boolean REFRESH) {
		super(Text.literal(""));
		this.GRID = new GridWidget();
		this.PARENT_SCREEN = PARENT;
		this.REFRESH = REFRESH;
	}
	public void init() {
		try {
			GRID.getMainPositioner().alignHorizontalCenter().margin(0);
			GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
			GRID_ADDER.add(ConfigScreenHelper.createTitle(client, new ExperimentalConfigScreen(PARENT_SCREEN, true), false, "", true));
			GRID_ADDER.add(createExperiments());
			GRID_ADDER.add(new EmptyWidget(4, 4));
			GRID_ADDER.add(createFooter());
			GRID.refreshPositions();
			GRID.forEachChild(this::addDrawableChild);
			initTabNavigation();
		} catch (Exception error) {
			Data.version.getLogger().warn("{} Failed to initialize config>experimental screen: {}", Data.version.getID(), error);
		}
	}
	public void tick() {
		try {
			if (this.REFRESH) {
				ClientData.CLIENT.setScreen(new ExperimentalConfigScreen(PARENT_SCREEN, false));
			}
			if (this.SHOULD_CLOSE) {
				ClientData.CLIENT.setScreen(PARENT_SCREEN);
			}
		} catch (Exception error) {
			Data.version.getLogger().warn("{} Failed to tick perspective$config$experimental screen: {}", Data.version.getID(), error);
		}
	}
	private GridWidget createEmpty() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(new MultilineTextWidget(Translation.getConfigTranslation(Data.version.getID(), "experimental.none", new Formatting[]{Formatting.RED, Formatting.BOLD}), ClientData.CLIENT.textRenderer).setCentered(true));
		return GRID;
	}
	private GridWidget createExperiments() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(new MultilineTextWidget(Translation.getConfigTranslation(Data.version.getID(), "experimental.warning", new Formatting[]{Formatting.RED, Formatting.BOLD}), ClientData.CLIENT.textRenderer).setCentered(true), 2);
		GRID_ADDER.add(new EmptyWidget(4, 4), 2);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "experimental.option", new Object[]{Translation.getTranslation(Data.version.getID(), "experimental.super_secret_settings_list"), Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getExperimentalConfig("super_secret_settings_list"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setExperimentalConfig("super_secret_settings_list", !(boolean) ConfigHelper.getExperimentalConfig("super_secret_settings_list"));
			REFRESH = true;
		}).width(304).build(), 2);
		return GRID;
	}

	private GridWidget createFooter() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "reset"), (button) -> {
			ConfigHelper.resetConfig();
			REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "back"), (button) -> this.SHOULD_CLOSE = true).build());
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
		if (keyCode == GLFW.GLFW_KEY_F5) {
			ClientData.CLIENT.setScreen(new UpdateCheckerScreen(this));
			this.REFRESH = true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
	}
}