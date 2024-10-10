/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.AbstractConfigScreen;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.update.Update;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.MultilineTextWidget;

public class UpdateCheckerScreen extends AbstractConfigScreen {
	public UpdateCheckerScreen(Screen parentScreen) {
		super(parentScreen, false, false, 1);
	}
	public void init() {
		try {
			super.init();
			if (this.page == 1) this.gridAdder.add(new MultilineTextWidget(Translation.getConfigTranslation(Data.version.getID(), "update.checking"), ClientData.minecraft.textRenderer).setCentered(true));
			postInit();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize zoom config screen: {}", error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
	public void tick() {
		super.tick();
		if (Update.updateCheckerComplete) this.shouldClose = true;
	}
	protected GridWidget createFooter() {
		return new GridWidget();
	}
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (Update.updateCheckerComplete) return super.keyPressed(keyCode, scanCode, modifiers);
		else return false;
	}
}