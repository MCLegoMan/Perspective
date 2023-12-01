/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.more_options.toasts;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.ConfigScreenHelper;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.UpdateChecker;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class UpdateCheckerScreen extends Screen {
	private final Screen PARENT_SCREEN;
	private final GridWidget GRID;
	private boolean STARTED;
	public UpdateCheckerScreen(Screen PARENT) {
		super(Text.literal(""));
		this.GRID = new GridWidget();
		this.PARENT_SCREEN = PARENT;
	}
	public void init() {
		try {
			GRID.getMainPositioner().alignHorizontalCenter().margin(0);
			GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
			GRID_ADDER.add(ConfigScreenHelper.createTitle());
			GRID_ADDER.add(createUpdateChecker());
			GRID.refreshPositions();
			GRID.forEachChild(this::addDrawableChild);
			initTabNavigation();
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize config$update checker screen: {}", Data.PERSPECTIVE_VERSION.getID(), error);
		}
	}
	public void tick() {
		try {
			if (!STARTED) {
				UpdateChecker.checkForUpdates();
				STARTED = true;
			}
			if (UpdateChecker.UPDATE_CHECKER_COMPLETE) {
				ClientData.CLIENT.setScreen(PARENT_SCREEN);
			}
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to tick config$update checker screen: {}", Data.PERSPECTIVE_VERSION.getID(), error);
		}
	}
	private static GridWidget createUpdateChecker() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
		GRID_ADDER.add(new TextWidget(Translation.getConfigTranslation("update.checking"), ClientData.CLIENT.textRenderer));
		return GRID;
	}
	public void initTabNavigation() {
		try {
			SimplePositioningWidget.setPos(GRID, getNavigationFocus());
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize config>update checker screen TabNavigation: {}", Data.PERSPECTIVE_VERSION.getID(), error);
		}
	}
	public Text getNarratedTitle() {
		return ScreenTexts.joinSentences();
	}
	public boolean shouldCloseOnEsc() {
		return false;
	}
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
	}
}
