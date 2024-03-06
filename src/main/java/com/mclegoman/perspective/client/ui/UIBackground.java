/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.ui;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UIBackground {
	private static final List<String> uiBackgroundTypes = new ArrayList<>();
	private static final List<String> titleScreenBackgroundTypes = new ArrayList<>();
	public static void init() {
		uiBackgroundTypes.add("default");
		uiBackgroundTypes.add("gaussian");
		uiBackgroundTypes.add("legacy");
		titleScreenBackgroundTypes.add("default");
		titleScreenBackgroundTypes.add("dirt");
	}
	public static String getUIBackgroundType() {
		if (!isValidUIBackgroundType((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "ui_background"))) cycleUIBackgroundType();
		return (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "ui_background");
	}
	public static void cycleUIBackgroundType() {
		cycleUIBackgroundType(true);
	}
	public static void cycleUIBackgroundType(boolean direction) {
		int currentIndex = uiBackgroundTypes.indexOf(getUIBackgroundType());
		ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "ui_background", uiBackgroundTypes.get(direction ? (currentIndex + 1) % uiBackgroundTypes.size() : (currentIndex - 1 + uiBackgroundTypes.size()) % uiBackgroundTypes.size()));
	}
	public static boolean isValidUIBackgroundType(String UIBackgroundType) {
		return uiBackgroundTypes.contains(UIBackgroundType.toLowerCase());
	}
	public static String getTitleScreenBackgroundType() {
		if (!isValidTitleScreenBackgroundType((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "title_screen"))) cycleTitleScreenBackgroundType();
		return (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "title_screen");
	}
	public static void cycleTitleScreenBackgroundType() {
		cycleTitleScreenBackgroundType(true);
	}
	public static void cycleTitleScreenBackgroundType(boolean direction) {
		String titleScreenBackgroundType = getTitleScreenBackgroundType();
		if (isValidTitleScreenBackgroundType(titleScreenBackgroundType)) {
			int currentIndex = titleScreenBackgroundTypes.indexOf(titleScreenBackgroundType);
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "title_screen", titleScreenBackgroundTypes.get(direction ? (currentIndex + 1) % titleScreenBackgroundTypes.size() : (currentIndex - 1 + titleScreenBackgroundTypes.size()) % titleScreenBackgroundTypes.size()));
		} else {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "title_screen", titleScreenBackgroundTypes.get(0));
		}
	}
	public static boolean isValidTitleScreenBackgroundType(String TitleScreenBackgroundType) {
		return titleScreenBackgroundTypes.contains(TitleScreenBackgroundType);
	}
	public static class Gaussian {
		@Nullable
		public static PostEffectProcessor postProcessor;
		private static void load() {
			try {
				if (postProcessor != null) postProcessor.close();
				postProcessor = new PostEffectProcessor(ClientData.CLIENT.getTextureManager(), ClientData.CLIENT.getResourceManager(), ClientData.CLIENT.getFramebuffer(), new Identifier("perspective", "shaders/post/gaussian.json"));
				postProcessor.setupDimensions(ClientData.CLIENT.getWindow().getFramebufferWidth(), ClientData.CLIENT.getWindow().getFramebufferHeight());
			} catch (Exception error) {
				Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("Error loading blur shader: {}", error));
			}
		}
		public static void render(float tickDelta) {
			try {
				if (postProcessor == null) load();
				RenderSystem.enableBlend();
				postProcessor.render(tickDelta);
				RenderSystem.disableBlend();
			} catch (Exception error) {
				Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("Error rendering blur ui background: {}", error));
			}
		}
	}
	public static class Legacy {
		public static void renderWorld(DrawContext context) {
			try {
				RenderSystem.enableBlend();
				context.fillGradient(0, 0, ClientData.CLIENT.getWindow().getScaledWidth(), ClientData.CLIENT.getWindow().getScaledHeight(), -1072689136, -804253680);
				RenderSystem.disableBlend();
			} catch (Exception error) {
				Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("Error rendering legacy ui background: {}", error));
			}
		}
		public static void renderMenu(DrawContext context) {
			try {
				RenderSystem.enableBlend();
				context.setShaderColor(0.25F, 0.25F, 0.25F, 1.0F);
				context.drawTexture(new Identifier("textures/block/dirt.png"), 0, 0, 0, 0.0F, 0.0F, ClientData.CLIENT.getWindow().getScaledWidth(), ClientData.CLIENT.getWindow().getScaledHeight(), 32, 32);
				context.drawTexture(new Identifier("textures/gui/options_background.png"), 0, 0, 0, 0.0F, 0.0F, ClientData.CLIENT.getWindow().getScaledWidth(), ClientData.CLIENT.getWindow().getScaledHeight(), 32, 32);
				context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.disableBlend();
			} catch (Exception error) {
				Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("Error rendering legacy ui background: {}", error));
			}
		}
	}
}
