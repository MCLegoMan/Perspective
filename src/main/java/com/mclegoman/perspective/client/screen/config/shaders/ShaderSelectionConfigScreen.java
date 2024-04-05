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
import com.mclegoman.perspective.client.translation.TranslationType;
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
	private boolean SHOULD_CLOSE;
	private final boolean blurEnabled;
	private boolean refresh;
	public ShaderSelectionConfigScreen(Screen PARENT, Formatting[] formattings, double scrollAmount, boolean blurEnabled) {
		super(Text.literal(""));
		this.parent = PARENT;
		this.formattings = formattings;
		this.scrollAmount = scrollAmount;
		this.blurEnabled = ClientData.CLIENT.world == null || blurEnabled;
		this.refresh = false;
	}
	protected void init() {
		this.widget = new ShadersListWidget<>(ClientData.CLIENT.getWindow().getScaledWidth(), ClientData.CLIENT.getWindow().getScaledHeight(), 32, 32, 27, scrollAmount);
		addDrawableChild(widget);
		addDrawableChild(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "back"), (button) -> this.SHOULD_CLOSE = true).dimensions(ClientData.CLIENT.getWindow().getScaledWidth() / 2 - 75, ClientData.CLIENT.getWindow().getScaledHeight() - 26, 150, 20).build());
		if (ClientData.CLIENT.world != null) addDrawableChild(ButtonWidget.builder(Translation.getConfigTranslation(Data.VERSION.getID(), "shaders.toggle_blur", new Object[]{Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_selection_blur"), TranslationType.BLUR)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_selection_blur", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_selection_blur"));
			this.refresh = true;
		}).dimensions(ClientData.CLIENT.getWindow().getScaledWidth() - 42, ClientData.CLIENT.getWindow().getScaledHeight() - 26, 20, 20).build());
	}
	public void tick() {
		try {
			if (this.SHOULD_CLOSE) {
				ClientData.CLIENT.setScreen(parent);
			}
			if (this.refresh) {
				ClientData.CLIENT.setScreen(new ShaderSelectionConfigScreen(parent, formattings, widget.getScrollAmount(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_selection_blur")));
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to tick perspective$config$shaders$select screen: {}", Data.VERSION.getID(), error);
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(ClientData.CLIENT.textRenderer, Translation.getConfigTranslation(Data.VERSION.getID(), "shaders.list.select", formattings), ClientData.CLIENT.getWindow().getScaledWidth() / 2, 12, 0xFFFFFF);
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
			this.SHOULD_CLOSE = true;
		if (keyCode == GLFW.GLFW_KEY_F1) {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_selection_blur", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_selection_blur"));
			this.refresh = true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
}