/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.zoom;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.ConfigScreenHelper;
import com.mclegoman.perspective.client.screen.config.toasts.UpdateCheckerScreen;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.translation.TranslationType;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ZoomConfigScreen extends Screen {
	private final Screen PARENT_SCREEN;
	private final GridWidget GRID;
	private boolean REFRESH;
	private boolean SHOULD_CLOSE;

	public ZoomConfigScreen(Screen PARENT, boolean REFRESH) {
		super(Text.literal(""));
		this.GRID = new GridWidget();
		this.PARENT_SCREEN = PARENT;
		this.REFRESH = REFRESH;
	}

	public void init() {
		try {
			GRID.getMainPositioner().alignHorizontalCenter().margin(0);
			GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
			GRID_ADDER.add(ConfigScreenHelper.createTitle(ClientData.CLIENT, new ZoomConfigScreen(PARENT_SCREEN, true), true, "zoom", false));
			GRID_ADDER.add(createZoom());
			GRID_ADDER.add(new EmptyWidget(4, 4));
			GRID_ADDER.add(createFooter());
			GRID.refreshPositions();
			GRID.forEachChild(this::addDrawableChild);
			initTabNavigation();
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to initialize config$zoom screen: {}", Data.VERSION.getID(), error);
		}
	}

	public void tick() {
		try {
			if (this.REFRESH) {
				ClientData.CLIENT.setScreen(new ZoomConfigScreen(PARENT_SCREEN, false));
			}
			if (this.SHOULD_CLOSE) {
				ClientData.CLIENT.setScreen(PARENT_SCREEN);
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to tick config$zoom screen: {}", Data.VERSION.getID(), error);
		}
	}

	private GridWidget createZoom() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		double ZOOM_LEVEL = (double) ((int) ConfigHelper.getConfig("zoom_level")) / 100;
		GRID_ADDER.add(new SliderWidget(GRID_ADDER.getGridWidget().getX(), GRID_ADDER.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation("zoom.level", new Object[]{Text.literal((int) ConfigHelper.getConfig("zoom_level") + "%")}, false), ZOOM_LEVEL) {
			@Override
			protected void updateMessage() {
				setMessage(Translation.getConfigTranslation("zoom.level", new Object[]{Text.literal((int) ConfigHelper.getConfig("zoom_level") + "%")}, false));
			}

			@Override
			protected void applyValue() {
				ConfigHelper.setConfig("zoom_level", (int) ((value) * 100));
			}
		}, 1);
		double ZOOM_INCREMENT_SIZE = (double) ((int) ConfigHelper.getConfig("zoom_increment_size") - 1) / 9;
		GRID_ADDER.add(new SliderWidget(GRID_ADDER.getGridWidget().getX(), GRID_ADDER.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation("zoom.increment_size", new Object[]{Text.literal(String.valueOf((int) ConfigHelper.getConfig("zoom_increment_size")))}, false), ZOOM_INCREMENT_SIZE) {
			@Override
			protected void updateMessage() {
				setMessage(Translation.getConfigTranslation("zoom.increment_size", new Object[]{Text.literal(String.valueOf((int) ConfigHelper.getConfig("zoom_increment_size")))}, false));
			}

			@Override
			protected void applyValue() {
				ConfigHelper.setConfig("zoom_increment_size", (int) ((value) * 9) + 1);
			}
		}, 1);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("zoom.transition", new Object[]{Translation.getZoomModeTranslation((String) ConfigHelper.getConfig("zoom_transition"))}), (button) -> {
			ConfigHelper.setConfig("zoom_transition", Zoom.nextTransition());
			this.REFRESH = true;
		}).build(), 1);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("zoom.camera_mode", new Object[]{Translation.getZoomCameraModeTranslation((String) ConfigHelper.getConfig("zoom_camera_mode"))}), (button) -> {
			ConfigHelper.setConfig("zoom_camera_mode", Zoom.nextCameraMode());
			this.REFRESH = true;
		}).build(), 1);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("zoom.hide_hud", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig("zoom_hide_hud"), TranslationType.ONFF)}), (button) -> {
			ConfigHelper.setConfig("zoom_hide_hud", !(boolean) ConfigHelper.getConfig("zoom_hide_hud"));
			this.REFRESH = true;
		}).build(), 1);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("zoom.show_percentage", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig("zoom_show_percentage"), TranslationType.ONFF)}), (button) -> {
			ConfigHelper.setConfig("zoom_show_percentage", !(boolean) ConfigHelper.getConfig("zoom_show_percentage"));
			this.REFRESH = true;
		}).build(), 1);
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
			this.REFRESH = true;
		}
		return super.keyReleased(keyCode, scanCode, modifiers);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
	}
}