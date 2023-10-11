/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.more_options.toasts;

import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.screen.config.PerspectiveConfigScreenHelper;
import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.client.util.PerspectiveKeybindings;
import com.mclegoman.perspective.client.util.PerspectiveUpdateChecker;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class PerspectiveUpdateCheckerScreen extends Screen {
	private final Screen PARENT_SCREEN;
	private final GridWidget GRID;
	private boolean STARTED;
	public PerspectiveUpdateCheckerScreen(Screen PARENT) {
		super(Text.literal(""));
		this.GRID = new GridWidget();
		this.PARENT_SCREEN = PARENT;
	}
	public void init() {
		try {
			GRID.getMainPositioner().alignHorizontalCenter().margin(0);
			GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
			GRID_ADDER.add(PerspectiveConfigScreenHelper.createTitle());
			GRID_ADDER.add(createUpdateChecker());
			GRID.refreshPositions();
			GRID.forEachChild(this::addDrawableChild);
			initTabNavigation();
		} catch (Exception error) {
			PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize config$update checker screen: {}", PerspectiveData.PERSPECTIVE_VERSION.getID(), error);
		}
	}
	public void tick() {
		try {
			if (!STARTED) {
				PerspectiveUpdateChecker.checkForUpdates();
				STARTED = true;
			}
			if (PerspectiveUpdateChecker.UPDATE_CHECKER_COMPLETE) {
				PerspectiveClientData.CLIENT.setScreen(PARENT_SCREEN);
			}
		} catch (Exception error) {
			PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to tick config$update checker screen: {}", PerspectiveData.PERSPECTIVE_VERSION.getID(), error);
		}
	}
	private static GridWidget createUpdateChecker() {
		GridWidget GRID = new GridWidget();
		GRID.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
		GRID_ADDER.add(new TextWidget(PerspectiveTranslation.getConfigTranslation("update.checking"), PerspectiveClientData.CLIENT.textRenderer));
		return GRID;
	}
	public void initTabNavigation() {
		try {
			SimplePositioningWidget.setPos(GRID, getNavigationFocus());
		} catch (Exception error) {
			PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize config>update checker screen TabNavigation: {}", PerspectiveData.PERSPECTIVE_VERSION.getID(), error);
		}
	}
	public Text getNarratedTitle() {
		return ScreenTexts.joinSentences();
	}
	public boolean shouldCloseOnEsc() {
		return false;
	}
}
