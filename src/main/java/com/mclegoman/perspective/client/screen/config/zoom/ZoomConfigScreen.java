/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.zoom;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.ScreenHelper;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.keybindings.Keybindings;
import com.mclegoman.perspective.client.util.Update;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ZoomConfigScreen extends Screen {
	private final Screen parentScreen;
	private final GridWidget grid;
	private boolean refresh;
	private boolean shouldClose;

	public ZoomConfigScreen(Screen PARENT, boolean REFRESH) {
		super(Text.literal(""));
		this.grid = new GridWidget();
		this.parentScreen = PARENT;
		this.refresh = REFRESH;
	}

	public void init() {
		try {
			grid.getMainPositioner().alignHorizontalCenter().margin(0);
			GridWidget.Adder GRID_ADDER = grid.createAdder(1);
			GRID_ADDER.add(ScreenHelper.createTitle(ClientData.minecraft, new ZoomConfigScreen(parentScreen, true), "zoom", false, true));
			GRID_ADDER.add(createZoom());
			GRID_ADDER.add(new EmptyWidget(4, 4));
			GRID_ADDER.add(createFooter());
			grid.refreshPositions();
			grid.forEachChild(this::addDrawableChild);
			initTabNavigation();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize config$zoom screen: {}", error));
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
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		double ZOOM_LEVEL = (double) ((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_level")) / 100;
		GRID_ADDER.add(new SliderWidget(GRID_ADDER.getGridWidget().getX(), GRID_ADDER.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.version.getID(), "zoom.level", new Object[]{Text.literal((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_level") + "%")}, false), ZOOM_LEVEL) {
			@Override
			protected void updateMessage() {
				setMessage(Translation.getConfigTranslation(Data.version.getID(),  "zoom.level", new Object[]{Text.literal((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_level") + "%")}, false));
			}

			@Override
			protected void applyValue() {
				ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "zoom_level", (int) ((value) * 100));
			}
		}, 1);
		double ZOOM_INCREMENT_SIZE = (double) ((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_increment_size") - 1) / 9;
		GRID_ADDER.add(new SliderWidget(GRID_ADDER.getGridWidget().getX(), GRID_ADDER.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.version.getID(), "zoom.increment_size", new Object[]{Text.literal(String.valueOf((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_increment_size")))}, false), ZOOM_INCREMENT_SIZE) {
			@Override
			protected void updateMessage() {
				setMessage(Translation.getConfigTranslation(Data.version.getID(),  "zoom.increment_size", new Object[]{Text.literal(String.valueOf((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_increment_size")))}, false));
			}

			@Override
			protected void applyValue() {
				ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "zoom_increment_size", (int) ((value) * 9) + 1);
			}
		}, 1);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom.type", new Object[]{Translation.getZoomTypeTranslation(Zoom.getZoomType().getNamespace(), Zoom.getZoomType().getPath())}), (button) -> {
			Zoom.cycleZoomType(!hasShiftDown());
			this.refresh = true;
		}).build(), 1);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom.transition", new Object[]{Translation.getZoomTransitionTranslation(Data.version.getID(), (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_transition"))}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "zoom_transition", Zoom.nextTransition());
			this.refresh = true;
		}).build(), 1);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom.scale_mode", new Object[]{Translation.getZoomScaleModeTranslation(Data.version.getID(), (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_scale_mode"))}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "zoom_scale_mode", Zoom.nextScaleMode());
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "zoom.scale_mode", new Object[]{Translation.getConfigTranslation(Data.version.getID(), "zoom.scale_mode." + ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_scale_mode"), true)}, true))).build(), 1);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom.hide_hud", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_hide_hud"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "zoom_hide_hud", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_hide_hud"));
			this.refresh = true;
		}).build(), 1);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom.show_percentage", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_show_percentage"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "zoom_show_percentage", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_show_percentage"));
			this.refresh = true;
		}).build(), 2);
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