package com.mclegoman.perspective.client.shaders;

import com.mclegoman.luminance.client.events.Events;
import com.mclegoman.luminance.client.shaders.Shader;
import com.mclegoman.luminance.client.shaders.ShaderDataloader;
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
		Events.AfterShaderDataRegistered.register(new Couple<>(Data.version.getID(), "main"), Shaders::set);
		Uniforms.init();
	}
	public static void tick() {
		Uniforms.tick();
	}
	public static void set() {
		ShaderRegistry shader = com.mclegoman.luminance.client.shaders.Shaders.get((String)ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_shader"));
		if (shader != null) {
			if (Events.ShaderRender.Shaders.exists(superSecretSettingsId, "main")) set(shader);
			else set(new Shader(shader, Shaders::getRenderType, Shaders::getShouldRender));
		}
		else {
			Data.version.sendToLog(LogType.WARN, Translation.getString("Cannot find specified shader: {}", ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_shader")));
			if (com.mclegoman.luminance.client.shaders.Shaders.get(ConfigDataLoader.superSecretSettingsShader) != null) {
				ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_shader", ConfigDataLoader.superSecretSettingsShader);
				ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_enabled", ConfigDataLoader.superSecretSettingsEnabled);
			}
			else if (ShaderDataloader.isValidIndex(0)) {
				ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_shader", com.mclegoman.luminance.client.shaders.Shaders.get(0).getId());
				ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_enabled", false);
			}
		}
	}
	private static void set(Shader shader) {
		Data.version.sendToLog(LogType.INFO, Translation.getString("Setting super secret settings shader: {}", ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_shader")));
		Events.ShaderRender.Shaders.set(superSecretSettingsId, "main", shader);
	}
	private static void set(ShaderRegistry shaderData) {
		Data.version.sendToLog(LogType.INFO, Translation.getString("Setting super secret settings shader: {}", ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_shader")));
		Shader shader = Events.ShaderRender.Shaders.get(superSecretSettingsId, "main").getSecond();
		shader.setShaderData(shaderData);
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
