/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.april_fools_prank;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.screen.config.AbstractConfigScreen;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;

public class AprilFoolsPrankConfigScreen extends AbstractConfigScreen {
	public AprilFoolsPrankConfigScreen(Screen parentScreen, boolean refresh, boolean saveOnClose, int page) {
		super(parentScreen, refresh, saveOnClose, page);
	}
	public void init() {
		try {
			super.init();
			if (this.page == 1) this.gridAdder.add(createPageOne());
			else shouldClose = true;
			postInit();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize {} config screen: {}", getPageTitle(), error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
	private GridWidget createPageOne() {
		GridWidget aprilFoolsGrid = new GridWidget();
		aprilFoolsGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder aprilFoolsGridAdder = aprilFoolsGrid.createAdder(1);
		aprilFoolsGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "april_fools_prank.allow", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "allow_april_fools"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "allow_april_fools", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "allow_april_fools"));
			refresh = true;
		}).width(304).build());
		aprilFoolsGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "april_fools_prank.force", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "force_april_fools"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "force_april_fools", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "force_april_fools"));
			refresh = true;
		}).width(304).build());
		aprilFoolsGridAdder.add(new EmptyWidget(20, 20));
		aprilFoolsGridAdder.add(new EmptyWidget(20, 20));
		return aprilFoolsGrid;
	}
	public Screen getRefreshScreen() {
		return new AprilFoolsPrankConfigScreen(this.parentScreen, false, false, this.page);
	}
	public String getPageId() {
		return "april_fools_prank";
	}
}