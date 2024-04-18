/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.april_fools_prank;

import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.ScreenHelper;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.client.util.UpdateChecker;
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
import org.lwjgl.glfw.GLFW;

public class AprilFoolsPrankConfigScreen extends Screen {
	private final Screen PARENT_SCREEN;
	private final GridWidget GRID;
	private boolean REFRESH;
	private boolean SHOULD_CLOSE;
	public AprilFoolsPrankConfigScreen(Screen PARENT, boolean REFRESH) {
		super(Text.literal(""));
		this.GRID = new GridWidget();
		this.PARENT_SCREEN = PARENT;
		this.REFRESH = REFRESH;
	}
	public void init() {
		try {
			GRID.getMainPositioner().alignHorizontalCenter().margin(0);
			GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
			GRID_ADDER.add(ScreenHelper.createTitle(ClientData.minecraft, new AprilFoolsPrankConfigScreen(PARENT_SCREEN, true), "april_fools_prank", false, true));
			GRID_ADDER.add(createAprilFools());
			GRID_ADDER.add(new EmptyWidget(4, 4));
			GRID_ADDER.add(createFooter());
			GRID.refreshPositions();
			GRID.forEachChild(this::addDrawableChild);
			initTabNavigation();
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to initialize config>april fools prank screen: {}", Data.VERSION.getLoggerPrefix(), error);
		}
	}
	public void tick() {
		try {
			if (this.REFRESH) {
				ClientData.minecraft.setScreen(new AprilFoolsPrankConfigScreen(PARENT_SCREEN, false));
			}
			if (this.SHOULD_CLOSE) {
				ClientData.minecraft.setScreen(PARENT_SCREEN);
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to tick perspective$config$april_fools screen: {}", Data.VERSION.getID(), error);
		}
	}
	private GridWidget createAprilFools() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "april_fools_prank.allow", new Object[]{Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "allow_april_fools"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "allow_april_fools", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "allow_april_fools"));
			REFRESH = true;
		}).width(304).build());
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "april_fools_prank.force", new Object[]{Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "force_april_fools"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "force_april_fools", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "force_april_fools"));
			REFRESH = true;
		}).width(304).build());
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
			if (hasControlDown()) ConfigHelper.reloadConfig();
			else UpdateChecker.checkForUpdates(Data.VERSION, true);
			this.REFRESH = true;
		}
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
	}
}