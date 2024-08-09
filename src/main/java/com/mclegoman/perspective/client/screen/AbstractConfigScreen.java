package com.mclegoman.perspective.client.screen;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.keybindings.Keybindings;
import com.mclegoman.perspective.client.logo.PerspectiveLogo;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.Update;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.config.ConfigHelper;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public abstract class AbstractConfigScreen extends Screen {
	protected final Screen parentScreen;
	protected final GridWidget grid;
	protected GridWidget.Adder gridAdder;
	protected boolean refresh;
	protected boolean shouldClose;
	protected boolean saveOnClose;
	public AbstractConfigScreen(Screen parentScreen, boolean refresh, boolean saveOnClose) {
		super(Text.literal(""));
		this.grid = new GridWidget();
		this.parentScreen = parentScreen;
		this.refresh = refresh;
		this.saveOnClose = saveOnClose;
	}
	public void init() {
		grid.getMainPositioner().alignHorizontalCenter().margin(0);
		gridAdder = grid.createAdder(1);
		gridAdder.add(new EmptyWidget(64, 64));
	}
	public void postInit() {
		gridAdder.add(new EmptyWidget(4, 4));
		gridAdder.add(createFooter());
		grid.refreshPositions();
		grid.forEachChild(this::addDrawableChild);
		initTabNavigation();
	}
	public void tick() {
		try {
			if (this.refresh) {
				ClientData.minecraft.setScreen(getRefreshScreen());
			}
			if (this.shouldClose) {
				if (this.saveOnClose) ConfigHelper.saveConfig();
				ClientData.minecraft.setScreen(this.parentScreen);
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to tick perspective$config screen: {}", error));
		}
	}
	protected GridWidget createFooter() {
		GridWidget footerGrid = new GridWidget();
		footerGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder footerGridAdder = footerGrid.createAdder(2);
		footerGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "reset"), (button) -> {
			if (ConfigHelper.resetConfig()) this.refresh = true;
		}).build());
		footerGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "back"), (button) -> this.shouldClose = true).build());
		return footerGrid;
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
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == KeyBindingHelper.getBoundKeyOf(Keybindings.openConfig).getCode())
			this.shouldClose = true;
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_F5) {
			if (hasControlDown()) ConfigHelper.reloadConfig(true);
			else Update.checkForUpdates(Data.version, true);
			this.refresh = true;
		}
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		if (ConfigHelper.showReloadOverlay) context.drawTextWithShadow(textRenderer, Translation.getConfigTranslation(Data.version.getID(), "reload"), 2, 2, 0xFFFFFF);
		context.drawTextWithShadow(textRenderer, Translation.getTranslation(Data.version.getID(), "version", new Object[]{Translation.getTranslation(Data.version.getID(), "name", new Formatting[]{Formatting.WHITE}), Translation.getText(Data.version.getFriendlyString(), false, new Formatting[]{Formatting.WHITE})}), 2, this.height - 10, 0xFFFFFF);
		Text licenceText = Translation.getTranslation(Data.version.getID(), "license", new Object[]{Translation.getTranslation(Data.version.getID(), "name", new Formatting[]{Formatting.WHITE}), Translation.getText(Data.version.getFriendlyString(false), false, new Formatting[]{Formatting.WHITE})});
		context.drawTextWithShadow(textRenderer, licenceText, this.width - this.textRenderer.getWidth(licenceText) - 2, this.height - 10, 0xFFFFFF);
		new PerspectiveLogo.Widget(this.width / 2 - 128, 30, false).renderWidget(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(textRenderer, getPageTitle(), this.width / 2, 78, 0xFFFFFF);
	}
	public Screen getRefreshScreen() {
		return this;
	}
	public Text getPageTitle() {
		return Text.empty();
	}
}
