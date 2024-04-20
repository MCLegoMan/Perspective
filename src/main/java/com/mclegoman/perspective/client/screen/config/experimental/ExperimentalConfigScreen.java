/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.experimental;

import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.ScreenHelper;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.keybindings.Keybindings;
import com.mclegoman.perspective.client.util.UpdateChecker;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class ExperimentalConfigScreen extends Screen {
	private final Screen parentScreen;
	private final GridWidget grid;
	private boolean refresh;
	private boolean shouldClose;

	public ExperimentalConfigScreen(Screen PARENT, boolean REFRESH) {
		super(Text.literal(""));
		this.grid = new GridWidget();
		this.parentScreen = PARENT;
		this.refresh = REFRESH;
	}

	public void init() {
		try {
			grid.getMainPositioner().alignHorizontalCenter().margin(0);
			GridWidget.Adder GRID_ADDER = grid.createAdder(1);
			GRID_ADDER.add(ScreenHelper.createTitle(client, new ExperimentalConfigScreen(parentScreen, true), "experimental", true, true));
			if (ConfigHelper.experimentsAvailable) GRID_ADDER.add(createExperiments());
			else GRID_ADDER.add(createEmpty());
			GRID_ADDER.add(new EmptyWidget(4, 4));
			GRID_ADDER.add(createFooter());
			grid.refreshPositions();
			grid.forEachChild(this::addDrawableChild);
			initTabNavigation();
		} catch (Exception error) {
			Data.version.getLogger().warn("{} Failed to initialize config>experimental screen: {}", Data.version.getID(), error);
		}
	}

	public void tick() {
		try {
			if (this.refresh) {
				ClientData.minecraft.setScreen(new ExperimentalConfigScreen(parentScreen, false));
			}
			if (this.shouldClose) {
				ClientData.minecraft.setScreen(parentScreen);
			}
		} catch (Exception error) {
			Data.version.getLogger().warn("{} Failed to tick perspective$config$experimental screen: {}", Data.version.getID(), error);
		}
	}

	private GridWidget createEmpty() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(new MultilineTextWidget(Translation.getConfigTranslation(Data.version.getID(), "experimental.none", new Formatting[]{Formatting.RED, Formatting.BOLD}), ClientData.minecraft.textRenderer).setCentered(true));
		return GRID;
	}

	private GridWidget createExperiments() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
		GRID_ADDER.add(new MultilineTextWidget(Translation.getConfigTranslation(Data.version.getID(), "experimental.warning", new Formatting[]{Formatting.RED, Formatting.BOLD}), ClientData.minecraft.textRenderer).setCentered(true));
		GRID_ADDER.add(new EmptyWidget(4, 4));
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "experimental.improved_shader_renderer", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.EXPERIMENTAL, "improved_shader_renderer"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.EXPERIMENTAL, "improved_shader_renderer", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.EXPERIMENTAL, "improved_shader_renderer"));
			this.refresh = true;
		}).width(304).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "experimental.improved_shader_renderer", true))).build());
		return GRID;
	}

	private GridWidget createFooter() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "reset.experiments"), (button) -> {
			if (ConfigHelper.resetExperiments()) this.refresh = true;
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
			else UpdateChecker.checkForUpdates(Data.version, true);
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