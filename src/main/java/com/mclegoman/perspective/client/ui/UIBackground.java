/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.ui;

import com.mclegoman.luminance.common.util.IdentifierHelper;
import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UIBackground {
	@Nullable
	public static PostEffectProcessor postProcessor;
	private static final List<UIBackgroundData> uiBackgroundTypes = new ArrayList<>();
	private static final List<String> titleScreenBackgroundTypes = new ArrayList<>();
	public static void init() {
		uiBackgroundTypes.add(new UIBackgroundData("default", null, true, null, true, null));
		uiBackgroundTypes.add(new UIBackgroundData("gaussian", null, true, null, true, Identifier.of("perspective", "shaders/post/gaussian.json")));
		uiBackgroundTypes.add(new UIBackgroundData("legacy",
				new Runnable() {
					public void run(DrawContext context) {
						RenderSystem.enableBlend();
						context.fillGradient(0, 0, ClientData.minecraft.getWindow().getScaledWidth(), ClientData.minecraft.getWindow().getScaledHeight(), -1072689136, -804253680);
						RenderSystem.disableBlend();
					}
				},false,
				new Runnable() {
					public void run(DrawContext context) {
						RenderSystem.enableBlend();
						context.drawTexture(getUiBackgroundTextureFromConfig(), 0, 0, 0, 0.0F, 0.0F, ClientData.minecraft.getWindow().getScaledWidth(), ClientData.minecraft.getWindow().getScaledHeight(), 32, 32);
						context.drawTexture(Identifier.of(Data.version.getID(), "textures/gui/uibackground_menu_background.png"), 0, 0, 0, 0.0F, 0.0F, ClientData.minecraft.getWindow().getScaledWidth(), ClientData.minecraft.getWindow().getScaledHeight(), 32, 32);
						RenderSystem.disableBlend();
					}
				},
				false, null));
		titleScreenBackgroundTypes.add("default");
		titleScreenBackgroundTypes.add("dirt");
	}
	public static void cycleUIBackgroundType() {
		cycleUIBackgroundType(true);
	}
	public static void cycleUIBackgroundType(boolean direction) {
		int currentIndex = uiBackgroundTypes.indexOf(getCurrentUIBackground());
		ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "ui_background", uiBackgroundTypes.get(direction ? (currentIndex + 1) % uiBackgroundTypes.size() : (currentIndex - 1 + uiBackgroundTypes.size()) % uiBackgroundTypes.size()).getId());
		loadShader(getCurrentUIBackground());
	}
	public static UIBackgroundData getCurrentUIBackground() {
		return getUIBackgroundType((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "ui_background"));
	}
	public static UIBackgroundData getUIBackgroundType(String type) {
		for (UIBackgroundData data : uiBackgroundTypes) {
			if (data.getId().equals(type)) return data;
		}
		return new UIBackgroundData("fallback", null, true, null, true, null);
	}
	public static boolean isRegisteredUIBackgroundType(String type) {
		for (UIBackgroundData data : uiBackgroundTypes) {
			if (data.getId().equals(type)) return true;
		}
		return false;
	}
	public static String getTitleScreenBackgroundType() {
		if (!isValidTitleScreenBackgroundType((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "title_screen"))) cycleTitleScreenBackgroundType();
		return (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "title_screen");
	}
	public static void cycleTitleScreenBackgroundType() {
		cycleTitleScreenBackgroundType(true);
	}
	public static void cycleTitleScreenBackgroundType(boolean direction) {
		String titleScreenBackgroundType = getTitleScreenBackgroundType();
		if (isValidTitleScreenBackgroundType(titleScreenBackgroundType)) {
			int currentIndex = titleScreenBackgroundTypes.indexOf(titleScreenBackgroundType);
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "title_screen", titleScreenBackgroundTypes.get(direction ? (currentIndex + 1) % titleScreenBackgroundTypes.size() : (currentIndex - 1 + titleScreenBackgroundTypes.size()) % titleScreenBackgroundTypes.size()));
		} else {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "title_screen", titleScreenBackgroundTypes.getFirst());
		}
	}
	public static boolean isValidTitleScreenBackgroundType(String TitleScreenBackgroundType) {
		return titleScreenBackgroundTypes.contains(TitleScreenBackgroundType);
	}
	public static Identifier getUiBackgroundTextureFromConfig() {
		String uiBackgroundTexture = (String)ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "ui_background_texture");
		String namespace = IdentifierHelper.getStringPart(IdentifierHelper.Type.NAMESPACE, uiBackgroundTexture);
		String key = IdentifierHelper.getStringPart(IdentifierHelper.Type.KEY, uiBackgroundTexture);
		return (namespace != null && key != null) ? Identifier.of(namespace, (!key.startsWith("textures/") ? "textures/" : "") + key + (!key.endsWith(".png") ? ".png" : "")) : Identifier.of("minecraft", "textures/block/dirt.png");
	}
	public static void loadShader(UIBackgroundData data) {
		try {
			if (postProcessor != null) postProcessor.close();
			if (data.getRenderShader() && data.getShaderId() != null) {
				postProcessor = new PostEffectProcessor(ClientData.minecraft.getTextureManager(), ClientData.minecraft.getResourceManager(), ClientData.minecraft.getFramebuffer(), data.getShaderId());
				postProcessor.setupDimensions(ClientData.minecraft.getWindow().getFramebufferWidth(), ClientData.minecraft.getWindow().getFramebufferHeight());
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Error loading blur shader: {}", error));
		}
	}
	public static void renderShader(float tickDelta, UIBackgroundData data) {
		try {
			if (data.getRenderShader()) {
				if (postProcessor == null) loadShader(data);
				RenderSystem.enableBlend();
				postProcessor.render(tickDelta);
				RenderSystem.disableBlend();
				ClientData.minecraft.getFramebuffer().beginWrite(false);
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Error rendering blur ui background: {}", error));
		}
	}
	public interface Runnable {
		default void run(DrawContext context) {
		}
	}
}