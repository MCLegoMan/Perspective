/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.ambience;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.AbstractConfigScreen;
import com.mclegoman.perspective.client.screen.widget.ConfigSliderWidget;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.config.ConfigHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;

public class AmbienceConfigScreen extends AbstractConfigScreen {
	public AmbienceConfigScreen(Screen parentScreen, boolean refresh, boolean saveOnClose, int page) {
		super(parentScreen, refresh, saveOnClose, page);
	}
	public void init() {
		try {
			super.init();
			if (this.page == 1) this.gridAdder.add(createPageOne());
			else shouldClose = true;
			postInit();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize ambience config screen: {}", error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
	private GridWidget createPageOne() {
		GridWidget ambienceGrid = new GridWidget();
		ambienceGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder ambienceGridAdder = ambienceGrid.createAdder(2);
		double rippleDensity = (double) ((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "ripple_density")) / 16;
		ambienceGridAdder.add(new ConfigSliderWidget(ambienceGridAdder.getGridWidget().getX(), ambienceGridAdder.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.version.getID(), "ambience.ripple_density", new Object[]{Text.literal(String.valueOf((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "ripple_density")))}, false), rippleDensity) {
			protected void updateMessage() {
				setMessage(Translation.getConfigTranslation(Data.version.getID(),  "ambience.ripple_density", new Object[]{Text.literal(String.valueOf((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "ripple_density")))}, false));
			}
			protected void applyValue() {
				ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "ripple_density", (int) (value * 16));
			}
		}, 2);
		return ambienceGrid;
	}
	public Screen getRefreshScreen() {
		return new AmbienceConfigScreen(this.parentScreen, false, false, this.page);
	}
	public String getPageId() {
		return "ambience";
	}
}