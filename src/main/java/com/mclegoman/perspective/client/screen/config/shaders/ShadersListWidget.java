/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.shaders;

import com.mclegoman.luminance.client.shaders.ShaderDataloader;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.shaders.Shader;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import org.jetbrains.annotations.Nullable;

public class ShadersListWidget<E extends AlwaysSelectedEntryListWidget.Entry<E>> extends AlwaysSelectedEntryListWidget<ShaderListEntry> {
	protected ShadersListWidget(int width, int height, int top, int bottom, int itemHeight, double scrollAmount) {
		super(ClientData.minecraft, width, height - top - bottom, top, itemHeight);
		for (int i = 0; i <= (ShaderDataloader.getShaderAmount() - 1); i++) this.addEntry(new ShaderListEntry(i));
		if (this.getEntryCount() > 0) {
			this.setSelected(ShaderDataloader.isValidIndex(Shader.superSecretSettingsIndex) ? getEntry(Shader.superSecretSettingsIndex) : getEntry(0));
			this.setScrollAmount(scrollAmount >= 0 ? scrollAmount : ((ShaderDataloader.isValidIndex(Shader.superSecretSettingsIndex)) ? Shader.superSecretSettingsIndex * 27 : 0));
		}
		this.setFocused(true);
	}
	@Override
	public void setSelected(@Nullable ShaderListEntry entry) {
		super.setSelected(entry);
		if (entry != null && Shader.superSecretSettingsIndex != entry.shader) {
			Shader.superSecretSettingsIndex = entry.shader;
			Shader.set(true, true, false, false);
		}
	}
	@Override
	protected int addEntry(ShaderListEntry entry) {
		return super.addEntry(entry);
	}

	@Override
	protected int getScrollbarX()  {
		return (ClientData.minecraft.getWindow().getScaledWidth()) - 6;
	}
}