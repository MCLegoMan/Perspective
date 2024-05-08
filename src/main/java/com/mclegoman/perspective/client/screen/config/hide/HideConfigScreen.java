/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.hide;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hide.Hide;
import com.mclegoman.perspective.client.screen.ScreenHelper;
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
import org.lwjgl.glfw.GLFW;

public class HideConfigScreen extends Screen {
	private final Screen parentScreen;
	private final GridWidget grid;
	private boolean refresh;
	private boolean shouldClose;
	public HideConfigScreen(Screen PARENT, boolean REFRESH) {
		super(Text.literal(""));
		this.grid = new GridWidget();
		this.parentScreen = PARENT;
		this.refresh = REFRESH;
	}
	public void init() {
		try {
			grid.getMainPositioner().alignHorizontalCenter().margin(0);
			GridWidget.Adder GRID_ADDER = grid.createAdder(1);
			GRID_ADDER.add(ScreenHelper.createTitle(ClientData.minecraft, new HideConfigScreen(parentScreen, true), "hide", false, true));
			GRID_ADDER.add(createHide());
			GRID_ADDER.add(new EmptyWidget(4, 4));
			GRID_ADDER.add(createFooter());
			grid.refreshPositions();
			grid.forEachChild(this::addDrawableChild);
			initTabNavigation();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize config$hide screen: {}", error));
		}
	}
	public void tick() {
		try {
			if (this.refresh) {
				ClientData.minecraft.setScreen(new HideConfigScreen(parentScreen, false));
			}
			if (this.shouldClose) {
				ClientData.minecraft.setScreen(parentScreen);
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to tick config$hide screen: {}", error));
		}
	}
	private GridWidget createHide() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hide.hide_block_outline", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_block_outline"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "hide_block_outline", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_block_outline"));
			this.refresh = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hide.hide_crosshair", new Object[]{Translation.getHideCrosshairModeTranslation(Data.version.getID(), (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_crosshair"))}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "hide_crosshair", Hide.nextCrosshairMode());
			this.refresh = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hide.hide_armor", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_armor"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "hide_armor", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_armor"));
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "hide.hide_armor", true))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hide.hide_nametags", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_nametags"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "hide_nametags", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_nametags"));
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "hide.hide_nametags", true))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hide.hide_players", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_players"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "hide_players", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_players"));
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "hide.hide_players", true))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hide.show_message", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_show_message"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "hide_show_message", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_show_message"));
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