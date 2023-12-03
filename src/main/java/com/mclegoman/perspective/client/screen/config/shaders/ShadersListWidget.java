/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.shaders;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.shaders.ShaderDataLoader;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import org.jetbrains.annotations.Nullable;

public class ShadersListWidget<E extends AlwaysSelectedEntryListWidget.Entry<E>> extends AlwaysSelectedEntryListWidget<ShaderListEntry> {
	protected ShadersListWidget(int width, int height, int top, int bottom, int itemHeight) {
		super(ClientData.CLIENT, width, height, top, bottom, itemHeight);
		this.setRenderBackground(false);
		for (int i = 0; i <= ShaderDataLoader.getShaderAmount(); i++) {
			this.addEntry(new ShaderListEntry(i));
		}
		this.setSelected(getEntry((int) ConfigHelper.getConfig("super_secret_settings")));
		this.setFocused(true);
		this.setScrollAmount((int) ConfigHelper.getConfig("super_secret_settings") * 27);
	}

	@Override
	public void setSelected(@Nullable ShaderListEntry entry) {
		super.setSelected(entry);
		if (entry != null) {
			ConfigHelper.setConfig("super_secret_settings", entry.shader);
			Shader.set(ClientData.CLIENT, true, true, false, false);
		}
	}
	@Override
	protected int addEntry(ShaderListEntry entry) {
		return super.addEntry(entry);
	}
	@Override
	protected int getScrollbarPositionX() {
		return (ClientData.CLIENT.getWindow().getScaledWidth()) - 6;
	}
}
