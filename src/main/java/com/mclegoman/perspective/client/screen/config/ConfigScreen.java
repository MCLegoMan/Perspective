/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.experimental.ExperimentalConfigScreen;
import com.mclegoman.perspective.client.screen.config.hide.HideConfigScreen;
import com.mclegoman.perspective.client.screen.config.hold_perspective.HoldPerspectiveConfigScreen;
import com.mclegoman.perspective.client.screen.config.shaders.ShadersConfigScreen;
import com.mclegoman.perspective.client.screen.config.textured_entity.TexturedEntityConfigScreen;
import com.mclegoman.perspective.client.screen.config.toasts.ToastsConfigScreen;
import com.mclegoman.perspective.client.screen.config.toasts.UpdateCheckerScreen;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

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
			GRID_ADDER.add(ConfigScreenHelper.createTitle(client, new ConfigScreen(PARENT_SCREEN, true, PAGE), false, "", false));
			if (PAGE == 1) GRID_ADDER.add(createPageOne());
			else if (PAGE == 2) GRID_ADDER.add(createPageTwo());
			else SHOULD_CLOSE = true;
			GRID_ADDER.add(new EmptyWidget(4, 4));
			GRID_ADDER.add(createFooter());
			GRID.refreshPositions();
			GRID.forEachChild(this::addDrawableChild);
			initTabNavigation();
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to initialize config screen: {}", error));
		}
	}
	public void tick() {
		try {
			if (this.REFRESH) {
				ClientData.minecraft.setScreen(new ConfigScreen(PARENT_SCREEN, false, PAGE));
			}
			if (this.SHOULD_CLOSE) {
				ConfigHelper.saveConfig(false);
				ClientData.minecraft.setScreen(PARENT_SCREEN);
			}
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to tick config screen: {}", error));
		}
	}
	private GridWidget createPageOne() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		ButtonWidget zoomWidget = ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom"), (button) -> {}).build();
		zoomWidget.active = false;
		GRID_ADDER.add(zoomWidget);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "shaders"), (button) -> ClientData.minecraft.setScreen(new ShadersConfigScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE), false, false))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "textured_entity"), (button) -> ClientData.minecraft.setScreen(new TexturedEntityConfigScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE), false))).build());
		ButtonWidget aprilFoolsWidget = ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "april_fools_prank"), (button) -> {}).build();
		aprilFoolsWidget.active = false;
		GRID_ADDER.add(aprilFoolsWidget);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hide"), (button) -> ClientData.minecraft.setScreen(new HideConfigScreen(new ConfigScreen(PARENT_SCREEN, false, PAGE), false))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "toasts"), (button) -> ClientData.minecraft.setScreen(new ToastsConfigScreen(new ConfigScreen(PARENT_SCREEN, false, PAGE), false, false))).build());
		ButtonWidget infoWidget = ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "information"), (button) -> {}).build();
		infoWidget.active = false;
		GRID_ADDER.add(infoWidget);
		ButtonWidget EXPERIMENTAL = ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "experimental"), (button) -> ClientData.minecraft.setScreen(new ExperimentalConfigScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE), false))).build();
		EXPERIMENTAL.active = ConfigHelper.EXPERIMENTS_AVAILABLE;
		GRID_ADDER.add(EXPERIMENTAL);
		return GRID;
	}
	private GridWidget createPageTwo() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "more_options.version_overlay", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig("version_overlay"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig("version_overlay", !(boolean) ConfigHelper.getConfig("version_overlay"));
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "more_options.position_overlay", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig("position_overlay"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig("position_overlay", !(boolean) ConfigHelper.getConfig("position_overlay"));
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "more_options.force_pride", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig("force_pride"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig("force_pride", !(boolean) ConfigHelper.getConfig("force_pride"));
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "more_options.dirt_title_screen", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig("dirt_title_screen"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig("dirt_title_screen", !(boolean) ConfigHelper.getConfig("dirt_title_screen"));
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hold_perspective"), (button) -> ClientData.minecraft.setScreen(new HoldPerspectiveConfigScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE), false))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "more_options.show_death_coordinates", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig("show_death_coordinates"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig("show_death_coordinates", !(boolean) ConfigHelper.getConfig("show_death_coordinates"));
			this.REFRESH = true;
		}).build());
		return GRID;
	}
	private GridWidget createFooter() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(3);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "reset"), (button) -> {
			ConfigHelper.resetConfig();
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "back"), (button) -> {
			if (PAGE <= 1) this.SHOULD_CLOSE = true;
			else {
				PAGE -= 1;
				this.REFRESH = true;
			}
		}).width(74).build());
		ButtonWidget NEXT = ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "next"), (button) -> {
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
		if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == KeyBindingHelper.getBoundKeyOf(Keybindings.OPEN_CONFIG).getCode())
			this.SHOULD_CLOSE = true;
		if (keyCode == GLFW.GLFW_KEY_F5) {
			ClientData.minecraft.setScreen(new UpdateCheckerScreen(this));
			this.REFRESH = true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context);
		super.render(context, mouseX, mouseY, delta);
	}
}