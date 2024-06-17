/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.panorama;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.shaders.ShaderDataLoader;
import com.mclegoman.perspective.client.shaders.ShaderRegistryValue;
import com.mclegoman.perspective.client.toasts.Toast;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Panorama {
	private static final List<String> incompatibleMods = new ArrayList<>();
	public static void addIncompatibleMod(String modID) {
		if (!incompatibleMods.contains(modID)) incompatibleMods.add(modID);
	}
	public static List<String> getIncompatibleMods() {
		List<String> incompatibleModsFound = new ArrayList<>();
		for (String modID : incompatibleMods) {
			if (Data.isModInstalled(modID)) {
				incompatibleModsFound.add(FabricLoader.getInstance().getModContainer(modID).get().getMetadata().getName());
			}
		}
		return incompatibleModsFound;
	}
	public static void init() {
		addIncompatibleMod("canvas");
		addIncompatibleMod("iris");
	}
	public static void tick() {
		if (Keybindings.TAKE_PANORAMA_SCREENSHOT.wasPressed()) takePanorama(1024);
	}
	private static String getFilename() {
		String currentTime = Util.getFormattedCurrentTime();
		String filename = currentTime;
		int i = 1;
		boolean shouldReturn = false;
		while (!shouldReturn) {
			String filename1 = currentTime + (i == 1 ? "" : "_" + i);
			File file = new File(ClientData.minecraft.runDirectory.getPath() + "/resourcepacks/", filename1);
			if (!file.exists()) {
				filename = filename1;
				shouldReturn = true;
			}
			i++;
		}
		return filename;
	}
	private static boolean shouldTakePanorama() {
		return (boolean) ConfigHelper.getConfig("debug") || getIncompatibleMods().size() == 0 && !ClientData.minecraft.options.getGraphicsMode().getValue().equals(GraphicsMode.FABULOUS);
	}
	private static void takePanorama(int resolution) {
		if (ClientData.minecraft.player != null) {
			try {
				if (shouldTakePanorama()) {
					String panoramaName = getFilename();
					String rpDirLoc = ClientData.minecraft.runDirectory.getPath() + "/resourcepacks/" + panoramaName;
					String assetsDirLoc = rpDirLoc + "/assets/minecraft/textures/gui/title/background";
					if (new File(assetsDirLoc).mkdirs()) {
						int framebufferWidth = ClientData.minecraft.getWindow().getFramebufferWidth();
						int framebufferHeight = ClientData.minecraft.getWindow().getFramebufferHeight();
						Framebuffer framebuffer = new SimpleFramebuffer(resolution, resolution, true, MinecraftClient.IS_SYSTEM_MAC);
						float pitch = ClientData.minecraft.player.getPitch();
						float yaw = ClientData.minecraft.player.getYaw();
						ClientData.minecraft.gameRenderer.setBlockOutlineEnabled(false);
						ClientData.minecraft.gameRenderer.setRenderingPanorama(true);
						ClientData.minecraft.getWindow().setFramebufferWidth(resolution);
						ClientData.minecraft.getWindow().setFramebufferHeight(resolution);
						ClientData.minecraft.getFramebuffer().beginWrite(true);
						Perspective CURRENT_PERSPECTIVE = ClientData.minecraft.options.getPerspective();
						if (!CURRENT_PERSPECTIVE.isFirstPerson())
							ClientData.minecraft.options.setPerspective(Perspective.FIRST_PERSON);
						for (int l = 0; l < 6; ++l) {
							switch (l) {
								case 0 -> {
									ClientData.minecraft.player.setYaw(0.0F);
									ClientData.minecraft.player.setPitch(0.0F);
								}
								case 1 -> {
									ClientData.minecraft.player.setYaw(90.0F);
									ClientData.minecraft.player.setPitch(0.0F);
								}
								case 2 -> {
									ClientData.minecraft.player.setYaw(180.0F);
									ClientData.minecraft.player.setPitch(0.0F);
								}
								case 3 -> {
									ClientData.minecraft.player.setYaw(270.0F);
									ClientData.minecraft.player.setPitch(0.0F);
								}
								case 4 -> {
									ClientData.minecraft.player.setYaw(0.0F);
									ClientData.minecraft.player.setPitch(-90.0F);
								}
								default -> {
									ClientData.minecraft.player.setYaw(0.0F);
									ClientData.minecraft.player.setPitch(90.0F);
								}
							}
							framebuffer.beginWrite(true);
							ClientData.minecraft.gameRenderer.renderWorld(RenderTickCounter.ONE);
							try {
								if ((boolean) ConfigHelper.getConfig("super_secret_settings_enabled")) {
									PostEffectProcessor panoramaPerspectivePostProcessor = new PostEffectProcessor(ClientData.minecraft.getTextureManager(), ClientData.minecraft.getResourceManager(), framebuffer, (Identifier) Objects.requireNonNull(ShaderDataLoader.get((int) ConfigHelper.getConfig("super_secret_settings"), ShaderRegistryValue.ID)));
									panoramaPerspectivePostProcessor.setupDimensions(resolution, resolution);
									panoramaPerspectivePostProcessor.render(ClientData.minecraft.getRenderTickCounter().getTickDelta(true));
								}
							} catch (Exception error) {
								Data.version.sendToLog(LogType.ERROR, "Failed to add Super Secret Settings to Panorama.");
							}
							ScreenshotRecorder.saveScreenshot(new File(assetsDirLoc), "panorama_" + l + ".png", framebuffer);
						}
						if (!CURRENT_PERSPECTIVE.isFirstPerson())
							ClientData.minecraft.options.setPerspective(CURRENT_PERSPECTIVE);
						File pack_file = new File(rpDirLoc + "/pack.mcmeta");
						if (pack_file.createNewFile()) {
							FileWriter pack_writer = new FileWriter(pack_file);
							pack_writer.write("{\"pack\": {\"pack_format\": 9, \"supported_formats\": {\"min_inclusive\": 9, \"max_inclusive\": 2147483647}, \"description\": \"" + panoramaName + "\"}}\"}}");
							pack_writer.close();
						}
						ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.version.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.take_panorama_screenshot.success.title")}), Translation.getTranslation(Data.version.getID(), "toasts.take_panorama_screenshot.success.description", new Object[]{Text.literal(panoramaName)}), 320, Toast.Type.INFO));
						ClientData.minecraft.player.sendMessage(Translation.getTranslation(Data.version.getID(), "message.take_panorama_screenshot.success", new Object[]{Text.literal(panoramaName).formatted(Formatting.UNDERLINE).styled((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, new File(rpDirLoc).getAbsolutePath())))}));
						ClientData.minecraft.player.setPitch(pitch);
						ClientData.minecraft.player.setYaw(yaw);
						ClientData.minecraft.gameRenderer.setBlockOutlineEnabled(true);
						ClientData.minecraft.getWindow().setFramebufferWidth(framebufferWidth);
						ClientData.minecraft.getWindow().setFramebufferHeight(framebufferHeight);
						ClientData.minecraft.getFramebuffer().beginWrite(true);
						framebuffer.delete();
						ClientData.minecraft.gameRenderer.setRenderingPanorama(false);
						ClientData.minecraft.getFramebuffer().beginWrite(true);
					}
					if (!getIncompatibleMods().isEmpty() || ClientData.minecraft.options.getGraphicsMode().getValue().equals(GraphicsMode.FABULOUS)) {
						if ((boolean) ConfigHelper.getConfig("debug")) {
							Data.version.sendToLog(LogType.WARN, "Debug is enabled. The panorama screenshot might not have rendered as expected.");
							Text description = Translation.getTranslation(Data.version.getID(), "toasts.take_panorama_screenshot.debug.description");
							ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.version.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.take_panorama_screenshot.debug.title")}), description, 320, Toast.Type.WARNING));
						}
					}
				} else {
					if (!getIncompatibleMods().isEmpty()) {
						Data.version.sendToLog(LogType.WARN, Translation.getString("An error occurred whilst trying to take a panorama: Incompatible Mod(s): {}", getIncompatibleMods().toString().replace("[", "").replace("]", "")));
						Text description = (getIncompatibleMods().size() == 1) ? Translation.getTranslation(Data.version.getID(), "toasts.take_panorama_screenshot.failure.description.incompatible_mod", new Object[]{getIncompatibleMods().toString().replace("[", "").replace("]", "")}) : Translation.getTranslation(Data.version.getID(), "toasts.take_panorama_screenshot.failure.description.incompatible_mods", new Object[]{getIncompatibleMods().toString().replace("[", "").replace("]", "")});
						ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.version.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.take_panorama_screenshot.failure.title")}), description, 320, Toast.Type.WARNING));
					}
					if (ClientData.minecraft.options.getGraphicsMode().getValue().equals(GraphicsMode.FABULOUS)) {
						Data.version.sendToLog(LogType.WARN, Translation.getString("An error occurred whilst trying to take a panorama: Take Panorama Screenshot is not compatible with Fabulous graphics.", Data.version.getLoggerPrefix()));
						ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.version.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.take_panorama_screenshot.failure.title")}), Translation.getTranslation(Data.version.getID(), "toasts.take_panorama_screenshot.failure.description.fabulous"), 320, Toast.Type.WARNING));
					}
				}
			} catch (Exception error) {
				Data.version.sendToLog(LogType.WARN, Translation.getString("An error occurred whilst trying to take a panorama: {}", error));
				ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.version.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.take_panorama_screenshot.failure.title")}), Text.of(String.valueOf(error)), 320, Toast.Type.WARNING));
			}
		}
	}
}