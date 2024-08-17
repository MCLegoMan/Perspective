/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.MultilineTextWidget;
import net.minecraft.util.Util;

public class LinkScreen extends AbstractConfigScreen {
	private final String url;
	private final boolean trusted;
	public LinkScreen(Screen parentScreen, String url, boolean trusted) {
		super(parentScreen, false, false, 1);
		this.url = url;
		this.trusted = trusted;
	}
	public void init() {
		try {
			this.grid.getMainPositioner().alignHorizontalCenter().margin(2);
			this.gridAdder = grid.createAdder(3);
			this.gridAdder.add(new EmptyWidget(20, 20), 3);
			this.gridAdder.add(new MultilineTextWidget(Translation.getText(this.trusted ? "chat.link.confirmTrusted" : "chat.link.confirm", true), ClientData.minecraft.textRenderer).setCentered(true), 3);
			this.gridAdder.add(new MultilineTextWidget(Translation.getText(this.url, false), ClientData.minecraft.textRenderer).setCentered(true), 3);
			this.gridAdder.add(new EmptyWidget(20, 20), 3);
			this.gridAdder.add(ButtonWidget.builder(Translation.getText("chat.link.open", true), (button) -> {
				Util.getOperatingSystem().open(this.url);
				ClientData.minecraft.setScreen(this.parentScreen);
			}).width(100).build());
			this.gridAdder.add(ButtonWidget.builder(Translation.getText("chat.copy", true), (button) -> {
				ClientData.minecraft.keyboard.setClipboard(this.url);
				ClientData.minecraft.setScreen(this.parentScreen);
			}).width(100).build());
			this.gridAdder.add(ButtonWidget.builder(Translation.getText("gui.cancel", true), (button) -> ClientData.minecraft.setScreen(this.parentScreen)).width(100).build());
			if (this.page != 1) shouldClose = true;
			this.grid.refreshPositions();
			this.grid.forEachChild(this::addDrawableChild);
			initTabNavigation();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize link screen: {}", getPageTitle(), error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
}
