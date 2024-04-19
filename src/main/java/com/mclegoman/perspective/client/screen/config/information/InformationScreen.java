/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.information;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.ScreenHelper;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.client.util.UpdateChecker;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.config.ConfigHelper;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class InformationScreen extends Screen {
	private final Screen parentScreen;
	private final GridWidget grid;
	private boolean refresh;
	private boolean shouldClose;

	public InformationScreen(Screen PARENT, boolean REFRESH) {
		super(Text.literal(""));
		this.grid = new GridWidget();
		this.parentScreen = PARENT;
		this.refresh = REFRESH;
	}

	public void init() {
		try {
			grid.getMainPositioner().alignHorizontalCenter().margin(0);
			GridWidget.Adder GRID_ADDER = grid.createAdder(1);
			GRID_ADDER.add(ScreenHelper.createTitle(client, new InformationScreen(parentScreen, true), "information", false, true));
			GRID_ADDER.add(createInformation());
			GRID_ADDER.add(new EmptyWidget(4, 4));
			GRID_ADDER.add(createFooter());
			grid.refreshPositions();
			grid.forEachChild(this::addDrawableChild);
			initTabNavigation();
		} catch (Exception error) {
			Data.version.getLogger().warn("{} Failed to initialize information screen: {}", Data.version.getID(), error);
		}
	}

	public void tick() {
		try {
			if (this.refresh) {
				ClientData.minecraft.setScreen(new InformationScreen(parentScreen, false));
			}
			if (this.shouldClose) {
				ClientData.minecraft.setScreen(parentScreen);
			}
		} catch (Exception error) {
			Data.version.getLogger().warn("{} Failed to tick perspective$config$info screen: {}", Data.version.getID(), error);
		}
	}

	private GridWidget createInformation() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "information.documentation"), ConfirmLinkScreen.opening(this, "https://mclegoman.com/Perspective")).build(), 1);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "information.source_code"), ConfirmLinkScreen.opening(this, "https://github.com/MCLegoMan/Perspective")).build(), 1);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "information.report"), ConfirmLinkScreen.opening(this, "https://github.com/MCLegoMan/Perspective/issues")).build(), 1);
		GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "information.credits"), button -> ClientData.minecraft.setScreen(new CreditsAttributionScreen(ClientData.minecraft.currentScreen))).build(), 1);
		return GRID;
	}

	private GridWidget createFooter() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
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