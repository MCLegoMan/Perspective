/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.textured_entity;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.screen.AbstractConfigScreen;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;

public class TexturedEntityConfigScreen extends AbstractConfigScreen {
	public TexturedEntityConfigScreen(Screen parentScreen, boolean refresh) {
		super(parentScreen, refresh, false);
	}
	public void init() {
		try {
			super.init();
			this.gridAdder.add(createTexturedEntity());
			this.gridAdder.add(new EmptyWidget(20, 20));
			this.gridAdder.add(new EmptyWidget(20, 20));
			postInit();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize textured entity config screen: {}", error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
	private GridWidget createTexturedEntity() {
		GridWidget texturedEntityGrid = new GridWidget();
		texturedEntityGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder texturedEntityGridAdder = texturedEntityGrid.createAdder(1);
		texturedEntityGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "textured_entity.named", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "textured_named_entity"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "textured_named_entity", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "textured_named_entity"));
			refresh = true;
		}).width(304).build(), 1);
		texturedEntityGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "textured_entity.random", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "textured_random_entity"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "textured_random_entity", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "textured_random_entity"));
			refresh = true;
		}).width(304).build(), 1);
		return texturedEntityGrid;
	}
	public Screen getRefreshScreen() {
		return new TexturedEntityConfigScreen(this.parentScreen, false);
	}
	public Text getPageTitle() {
		return Translation.getConfigTranslation(Data.version.getID(), "textured_entity");
	}
}