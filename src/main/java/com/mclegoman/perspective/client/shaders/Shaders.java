package com.mclegoman.perspective.client.shaders;

import com.mclegoman.luminance.client.events.Events;
import com.mclegoman.luminance.client.shaders.Shader;
import com.mclegoman.luminance.client.shaders.ShaderRegistry;
import com.mclegoman.luminance.common.util.Couple;
import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.config.ConfigDataLoader;
import com.mclegoman.perspective.config.ConfigHelper;

public class Shaders {
	public static Couple<String, String> superSecretSettingsId;
	public static void init() {
		Data.version.sendToLog(LogType.INFO, Translation.getString("Initializing Shader Renderers"));
		Events.AfterShaderDataRegistered.register(new Couple<>(Data.version.getID(), "main"), Shaders::setSuperSecretSettings);
		Uniforms.init();
	}
	public static void tick() {
		Uniforms.tick();
	}
	public static void setSuperSecretSettings() {
		try {
			ShaderRegistry shaderData = com.mclegoman.luminance.client.shaders.Shaders.get((String)ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_shader"));
			if (shaderData != null) set(shaderData);
			else {
				Data.version.sendToLog(LogType.WARN, Translation.getString("Cannot find specified shader: {}", ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_shader")));
				shaderData = com.mclegoman.luminance.client.shaders.Shaders.get(ConfigDataLoader.superSecretSettingsShader);
				if (shaderData != null) {
					ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_shader", shaderData.getId());
					ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_enabled", ConfigDataLoader.superSecretSettingsEnabled);
					set(shaderData);
				} else {
					Data.version.sendToLog(LogType.WARN, Translation.getString("Cannot find default shader: {}", ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_shader")));
					Events.ShaderRender.Shaders.remove(superSecretSettingsId, "main");
					ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_enabled", false);
					com.mclegoman.perspective.client.shaders.Shader.superSecretSettingsIndex = 0;
				}
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("An error occurred whilst trying to set super secret settings: {}", error));
		}
	}
	private static void set(ShaderRegistry shaderData) {
		try {
			String logMessage = Translation.getString("Setting super secret settings shader: {}", ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_shader"));
			if (Events.ShaderRender.Shaders.exists(superSecretSettingsId, "main")) {
				Couple<String, Shader> shader = Events.ShaderRender.Shaders.get(superSecretSettingsId, "main");
				if (shader.getSecond() != null) {
					if (!com.mclegoman.luminance.client.shaders.Shaders.getPostShader(shaderData.getId()).toString().equals(shader.getSecond().getShaderId().toString())) {
						Data.version.sendToLog(LogType.INFO, logMessage);
						shader.getSecond().setShaderData(shaderData);
					}
				}
			} else {
				Data.version.sendToLog(LogType.INFO, logMessage);
				Events.ShaderRender.Shaders.set(superSecretSettingsId, "main", new Shader(shaderData, Shaders::getRenderType, Shaders::getShouldRender));
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("An error occurred whilst trying to set super secret settings: {}", error));
		}
	}
	public static Shader.RenderType getRenderType() {
		Object renderMode = ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_mode");
		if (renderMode.equals("screen")) return Shader.RenderType.GAME;
		return Shader.RenderType.WORLD;
	}
	public static Boolean getShouldRender() {
		return (boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_enabled");
	}
	static {
		superSecretSettingsId = new Couple<>(Data.version.getID(), "super_secret_settings");
	}
}
