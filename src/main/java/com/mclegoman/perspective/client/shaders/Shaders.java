package com.mclegoman.perspective.client.shaders;

import com.mclegoman.luminance.client.events.Events;
import com.mclegoman.luminance.client.shaders.Shader;
import com.mclegoman.luminance.common.util.Couple;
import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.config.ConfigHelper;

public class Shaders {
	public static Couple<String, String> superSecretSettingsId;
	public static void init() {
		Data.version.sendToLog(LogType.INFO, Translation.getString("Initializing Shader Renderers"));
		Events.AfterShaderDataRegistered.register(new Couple<>(Data.version.getID(), "main"), () -> {
			set(new Shader(getRenderType(), com.mclegoman.luminance.client.shaders.Shaders.get(1)), true);
		});
		Uniforms.init();
	}
	public static void tick() {
		Uniforms.tick();
	}
	public static void set(Shader shader, boolean enabled) {
		if (enabled) {
			Events.ShaderRender.Shaders.set(superSecretSettingsId, "main", shader);
		} else {
			Events.ShaderRender.Shaders.remove(superSecretSettingsId, "main");
		}
	}
	public static Shader.RenderType getRenderType() {
		Object renderMode = ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_mode");
		if (renderMode.equals("screen")) return Shader.RenderType.GAME;
		return Shader.RenderType.WORLD;
	}
	static {
		superSecretSettingsId = new Couple<>(Data.version.getID(), "super_secret_settings");
	}
}
