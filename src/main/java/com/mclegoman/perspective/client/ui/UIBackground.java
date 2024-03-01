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
import com.mclegoman.perspective.client.util.JsonHelper;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UIBackground {
	private static final List<String> uiBackgroundTypes = new ArrayList<>();
	public static void init() {
		uiBackgroundTypes.add("default");
		uiBackgroundTypes.add("gaussian");
		uiBackgroundTypes.add("legacy");
	}
	public static String getUIBackgroundType() {
		if (!isValidUIBackgroundType(JsonHelper.asUIBackground((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "ui_background")))) cycleUIBackgroundType();
		return JsonHelper.asUIBackground((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "ui_background"));
	}
	public static void cycleUIBackgroundType() {
		cycleUIBackgroundType(true);
	}
	public static void cycleUIBackgroundType(boolean direction) {
		int currentIndex = uiBackgroundTypes.indexOf(getUIBackgroundType());
		ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "ui_background", JsonHelper.asUIBackground(uiBackgroundTypes.get(direction ? (currentIndex + 1) % uiBackgroundTypes.size() : (currentIndex - 1 + uiBackgroundTypes.size()) % uiBackgroundTypes.size())));
	}
	public static boolean isValidUIBackgroundType(String UIBackgroundType) {
		return uiBackgroundTypes.contains(UIBackgroundType.toLowerCase());
	}
	public static class Gaussian {
		@Nullable
		public static PostEffectProcessor blurPostProcessor;
		private static void load() {
			try {
				if (blurPostProcessor != null) blurPostProcessor.close();
				blurPostProcessor = new PostEffectProcessor(ClientData.CLIENT.getTextureManager(), ClientData.CLIENT.getResourceManager(), ClientData.CLIENT.getFramebuffer(), new Identifier("perspective", "shaders/post/gaussian.json"));
				blurPostProcessor.setupDimensions(ClientData.CLIENT.getWindow().getFramebufferWidth(), ClientData.CLIENT.getWindow().getFramebufferHeight());
			} catch (Exception error) {
				Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("Error loading blur shader: {}", error));
			}
		}
		public static void render(float tickDelta) {
			try {
				if (blurPostProcessor == null) load();
				blurPostProcessor.setUniforms("Alpha", (float) ClientData.CLIENT.options.getMenuBackgroundBlurrinessValue());
				blurPostProcessor.render(tickDelta);
			} catch (Exception error) {
				Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("Error rendering blur ui background: {}", error));
			}
		}
	}
	public static class Legacy {
		public static void render(DrawContext context, float tickDelta) {
			try {
				if (ClientData.CLIENT.world != null) {
					context.fillGradient(0, 0, ClientData.CLIENT.getWindow().getScaledWidth(), ClientData.CLIENT.getWindow().getScaledHeight(), -1072689136, -804253680);
				} else {
					context.setShaderColor(0.25F, 0.25F, 0.25F, 1.0F);
					context.drawTexture(new Identifier("minecraft", "textures/block/dirt.png"), 0, 0, 0, 0.0F, 0.0F, ClientData.CLIENT.getWindow().getScaledWidth(), ClientData.CLIENT.getWindow().getScaledHeight(), 16, 16);
					context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				}
			} catch (Exception error) {
				Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("Error rendering legacy ui background: {}", error));
			}
		}
	}
}
