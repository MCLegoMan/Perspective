/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.april_fools_prank.AprilFoolsPrankConfigScreen;
import com.mclegoman.perspective.client.screen.config.experimental.ExperimentalConfigScreen;
import com.mclegoman.perspective.client.screen.config.hide.HideConfigScreen;
import com.mclegoman.perspective.client.screen.config.hold_perspective.HoldPerspectiveConfigScreen;
import com.mclegoman.perspective.client.screen.config.information.InformationScreen;
import com.mclegoman.perspective.client.screen.config.shaders.ShadersConfigScreen;
import com.mclegoman.perspective.client.screen.config.textured_entity.TexturedEntityConfigScreen;
import com.mclegoman.perspective.client.screen.UpdateCheckerScreen;
import com.mclegoman.perspective.client.screen.config.zoom.ZoomConfigScreen;
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

public class ConfigScreen extends Screen {
	private final Screen PARENT_SCREEN;
	private final GridWidget GRID;
	private boolean REFRESH;
	private boolean SHOULD_CLOSE;
	private int PAGE;
	private boolean CHECK_FOR_UPDATES;

	public ConfigScreen(Screen PARENT, boolean REFRESH, int PAGE, boolean CHECK_FOR_UPDATES) {
		super(Text.literal(""));
		this.GRID = new GridWidget();
		this.PARENT_SCREEN = PARENT;
		this.REFRESH = REFRESH;
		this.PAGE = PAGE;
		this.CHECK_FOR_UPDATES = CHECK_FOR_UPDATES;
	}

	public void init() {
		try {
			GRID.getMainPositioner().alignHorizontalCenter().margin(0);
			GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
			GRID_ADDER.add(ConfigScreenHelper.createTitle(client, new ConfigScreen(PARENT_SCREEN, true, PAGE, false), false, "", false));
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
				ClientData.CLIENT.setScreen(new ConfigScreen(PARENT_SCREEN, false, PAGE, CHECK_FOR_UPDATES));
			}
			if (this.SHOULD_CLOSE) {
				ConfigHelper.saveConfig(false);
				if (this.CHECK_FOR_UPDATES) ClientData.CLIENT.setScreen(new UpdateCheckerScreen(PARENT_SCREEN));
				else ClientData.CLIENT.setScreen(PARENT_SCREEN);
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to tick perspective$config screen: {}", Data.VERSION.getID(), error);
		}
	}

	private GridWidget createPageOne() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("zoom"), (button) -> ClientData.CLIENT.setScreen(new ZoomConfigScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE, false), false))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("shaders"), (button) -> ClientData.CLIENT.setScreen(new ShadersConfigScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE, false), false, false))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("textured_entity"), (button) -> ClientData.CLIENT.setScreen(new TexturedEntityConfigScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE, false), false))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("april_fools_prank"), (button) -> ClientData.CLIENT.setScreen(new AprilFoolsPrankConfigScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE, false), false))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("hide"), (button) -> ClientData.CLIENT.setScreen(new HideConfigScreen(new ConfigScreen(PARENT_SCREEN, false, PAGE, false), false))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("hold_perspective"), (button) -> ClientData.CLIENT.setScreen(new HoldPerspectiveConfigScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE, false), false))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("information"), (button) -> ClientData.CLIENT.setScreen(new InformationScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE, false), false))).build());
		ButtonWidget EXPERIMENTAL = ButtonWidget.builder(Translation.getConfigTranslation("experimental"), (button) -> ClientData.CLIENT.setScreen(new ExperimentalConfigScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE, false), false))).build();
		EXPERIMENTAL.active = ConfigHelper.EXPERIMENTS_AVAILABLE;
		GRID_ADDER.add(EXPERIMENTAL);
		return GRID;
	}

	private GridWidget createPageTwo() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("version_overlay", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "version_overlay"), TranslationType.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "version_overlay", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "version_overlay"));
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("position_overlay", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "position_overlay"), TranslationType.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "position_overlay", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "position_overlay"));
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("dirt_title_screen", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "dirt_title_screen"), TranslationType.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "dirt_title_screen", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "dirt_title_screen"));
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("show_death_coordinates", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "show_death_coordinates"), TranslationType.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "show_death_coordinates", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "show_death_coordinates"));
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("tutorials", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "tutorials"), TranslationType.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "tutorials", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "tutorials"));
			this.REFRESH = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation("tutorials", true))).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("force_pride", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "force_pride"), TranslationType.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "force_pride", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "force_pride"));
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("detect_update_channel", new Object[]{Translation.getDetectUpdateChannelTranslation((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "detect_update_channel"))}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "detect_update_channel", UpdateChecker.nextUpdateChannel());
			this.REFRESH = true;
			this.CHECK_FOR_UPDATES = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation("detect_update_channel", true))).width(304).build(), 2);
		return GRID;
	}

	private GridWidget createFooter() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(3);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("reset"), (button) -> {
			ConfigHelper.resetConfig();
			this.REFRESH = true;
		}).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("back"), (button) -> {
			if (PAGE <= 1) this.SHOULD_CLOSE = true;
			else {
				PAGE -= 1;
				if (this.CHECK_FOR_UPDATES) ClientData.CLIENT.setScreen(new UpdateCheckerScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE, false)));
				else this.REFRESH = true;
			}
		}).width(74).build());
		ButtonWidget NEXT = ButtonWidget.builder(Translation.getConfigTranslation("next"), (button) -> {
			if (!(PAGE >= 2)) {
				PAGE += 1;
				if (this.CHECK_FOR_UPDATES) ClientData.CLIENT.setScreen(new UpdateCheckerScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE, false)));
				else this.REFRESH = true;
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
		if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == KeyBindingHelper.getBoundKeyOf(Keybindings.OPEN_CONFIG).getCode()) {
			if (PAGE <= 1) {
				this.SHOULD_CLOSE = true;
			}
			else {
				PAGE -= 1;
				if (this.CHECK_FOR_UPDATES) ClientData.CLIENT.setScreen(new UpdateCheckerScreen(new ConfigScreen(PARENT_SCREEN, true, PAGE, false)));
				else this.REFRESH = true;
			}
		}
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