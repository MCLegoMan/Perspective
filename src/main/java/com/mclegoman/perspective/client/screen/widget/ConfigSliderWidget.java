/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.widget;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public abstract class ConfigSliderWidget extends SliderWidget {
	public ConfigSliderWidget(int x, int y, int width, int height, Text text, double value) {
		super(x, y, width, height, text, value);
	}
	@Override
	public void onRelease(double mouseX, double mouseY) {
		if (this.isFocused()) super.onRelease(mouseX, mouseY);
	}
}
