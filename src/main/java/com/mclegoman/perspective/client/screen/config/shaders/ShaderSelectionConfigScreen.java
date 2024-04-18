/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.shaders;

import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class ShaderSelectionConfigScreen extends Screen {
	public final Screen parent;
	private final Formatting[] formattings;
	private final double scrollAmount;
	ShadersListWidget<ShaderListEntry> widget;
	private boolean shouldClose;
	private final boolean blurEnabled;
	private boolean refresh;
	public ShaderSelectionConfigScreen(Screen PARENT, Formatting[] formattings, double scrollAmount, boolean blurEnabled) {
		super(Text.literal(""));
		this.parent = PARENT;
		this.formattings = formattings;
		this.scrollAmount = scrollAmount;
		this.blurEnabled = ClientData.minecraft.world == null || blurEnabled;
		this.refresh = false;
	}
	protected void init() {
		this.widget = new ShadersListWidget<>(ClientData.minecraft.getWindow().getScaledWidth(), ClientData.minecraft.getWindow().getScaledHeight(), 32, 32, 27, scrollAmount);
		addDrawableChild(widget);
		addDrawableChild(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "back"), (button) -> this.shouldClose = true).dimensions(ClientData.minecraft.getWindow().getScaledWidth() / 2 - 75, ClientData.minecraft.getWindow().getScaledHeight() - 26, 150, 20).build());
		if (ClientData.minecraft.world != null) addDrawableChild(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "shaders.toggle_blur", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_selection_blur"), Translation.Type.BLUR)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_selection_blur", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_selection_blur"));
			this.refresh = true;
		}).dimensions(ClientData.minecraft.getWindow().getScaledWidth() - 42, ClientData.minecraft.getWindow().getScaledHeight() - 26, 20, 20).build());
	}
	public void tick() {
		try {
			if (this.shouldClose) {
				ClientData.minecraft.setScreen(parent);
			}
			if (this.refresh) {
				ClientData.minecraft.setScreen(new ShaderSelectionConfigScreen(parent, formattings, widget.getScrollAmount(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_selection_blur")));
			}
		} catch (Exception error) {
			Data.version.getLogger().warn("{} Failed to tick perspective$config$shaders$select screen: {}", Data.version.getID(), error);
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(ClientData.minecraft.textRenderer, Translation.getConfigTranslation(Data.version.getID(), "shaders.list.select", formattings), ClientData.minecraft.getWindow().getScaledWidth() / 2, 12, 0xFFFFFF);
	}
	@Override
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
		if (blurEnabled) super.renderBackground(context, mouseX, mouseY, delta);
	}
	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == KeyBindingHelper.getBoundKeyOf(Keybindings.openConfig).getCode())
			this.shouldClose = true;
		if (keyCode == GLFW.GLFW_KEY_F1) {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_selection_blur", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_selection_blur"));
			this.refresh = true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
}