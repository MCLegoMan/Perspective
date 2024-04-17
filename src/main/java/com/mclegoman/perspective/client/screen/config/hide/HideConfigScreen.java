/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.hide;

import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hide.Hide;
import com.mclegoman.perspective.client.screen.ScreenHelper;
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
import org.lwjgl.glfw.GLFW;

public class HideConfigScreen extends Screen {
	private final Screen PARENT_SCREEN;
	private final GridWidget GRID;
	private boolean REFRESH;
	private boolean SHOULD_CLOSE;

	public HideConfigScreen(Screen PARENT, boolean REFRESH) {
		super(Text.literal(""));
		this.GRID = new GridWidget();
		this.PARENT_SCREEN = PARENT;
		this.REFRESH = REFRESH;
	}

	public void init() {
		try {
			GRID.getMainPositioner().alignHorizontalCenter().margin(0);
			GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
			GRID_ADDER.add(ScreenHelper.createTitle(ClientData.minecraft, new HideConfigScreen(PARENT_SCREEN, true), "hide", false, true));
			GRID_ADDER.add(createHide());
			GRID_ADDER.add(new EmptyWidget(4, 4));
			GRID_ADDER.add(createFooter());
			GRID.refreshPositions();
			GRID.forEachChild(this::addDrawableChild);
			initTabNavigation();
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to initialize config$hide screen: {}", Data.VERSION.getID(), error);
		}
	}

	public void tick() {
		try {
			if (this.REFRESH) {
				ClientData.minecraft.setScreen(new HideConfigScreen(PARENT_SCREEN, false));
			}
			if (this.SHOULD_CLOSE) {
				ClientData.minecraft.setScreen(PARENT_SCREEN);
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to tick config$hide screen: {}", Data.VERSION.getID(), error);
		}
	}

	private GridWidget createHide() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "hide.hide_block_outline", new Object[]{Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_block_outline"), TranslationType.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "hide_block_outline", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_block_outline"));
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "hide.hide_crosshair", new Object[]{Translation.getHideCrosshairModeTranslation(Data.VERSION.getID(), (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_crosshair"))}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "hide_crosshair", Hide.nextCrosshairMode());
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "hide.hide_armor", new Object[]{Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_armor"), TranslationType.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "hide_armor", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_armor"));
			this.REFRESH = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.VERSION.getID(), "hide.hide_armor", true))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "hide.hide_nametags", new Object[]{Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_nametags"), TranslationType.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "hide_nametags", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_nametags"));
			this.REFRESH = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.VERSION.getID(), "hide.hide_nametags", true))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "hide.hide_players", new Object[]{Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_players"), TranslationType.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "hide_players", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_players"));
			this.REFRESH = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.VERSION.getID(), "hide.hide_players", true))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "hide.show_message", new Object[]{Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_show_message"), TranslationType.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "hide_show_message", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_show_message"));
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
		if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == KeyBindingHelper.getBoundKeyOf(Keybindings.openConfig).getCode())
			this.SHOULD_CLOSE = true;
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