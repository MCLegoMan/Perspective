/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.panorama;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.shaders.ShaderDataLoader;
import com.mclegoman.perspective.client.shaders.ShaderRegistryValue;
import com.mclegoman.perspective.client.toasts.Toast;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.option.Perspective;
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
	public static void tick(MinecraftClient client) {
		if (Keybindings.TAKE_PANORAMA_SCREENSHOT.wasPressed()) takePanorama(1024);
	}
	private static String getFilename() {
		String currentTime = Util.getFormattedCurrentTime();
		String filename = currentTime;
		int i = 1;
		boolean shouldReturn = false;
		while (!shouldReturn) {
			String filename1 = currentTime + (i == 1 ? "" : "_" + i);
			File file = new File(ClientData.CLIENT.runDirectory.getPath() + "/resourcepacks/", filename1);
			if (!file.exists()) {
				filename = filename1;
				shouldReturn = true;
			}
			i++;
		}
		return filename;
	}
	private static boolean shouldTakePanorama() {
		return (boolean) ConfigHelper.getConfig("debug") || getIncompatibleMods().size() == 0 && !ClientData.CLIENT.options.getGraphicsMode().getValue().equals(GraphicsMode.FABULOUS);
	}
	private static void takePanorama(int resolution) {
		if (ClientData.CLIENT.player != null) {
			try {
				if (shouldTakePanorama()) {
					String panoramaName = getFilename();
					String rpDirLoc = ClientData.CLIENT.runDirectory.getPath() + "/resourcepacks/" + panoramaName;
					String assetsDirLoc = rpDirLoc + "/assets/minecraft/textures/gui/title/background";
					if (new File(assetsDirLoc).mkdirs()) {
						int framebufferWidth = ClientData.CLIENT.getWindow().getFramebufferWidth();
						int framebufferHeight = ClientData.CLIENT.getWindow().getFramebufferHeight();
						Framebuffer framebuffer = new SimpleFramebuffer(resolution, resolution, true, MinecraftClient.IS_SYSTEM_MAC);
						float pitch = ClientData.CLIENT.player.getPitch();
						float yaw = ClientData.CLIENT.player.getYaw();
						ClientData.CLIENT.gameRenderer.setBlockOutlineEnabled(false);
						ClientData.CLIENT.gameRenderer.setRenderingPanorama(true);
						ClientData.CLIENT.getWindow().setFramebufferWidth(resolution);
						ClientData.CLIENT.getWindow().setFramebufferHeight(resolution);
						ClientData.CLIENT.getFramebuffer().beginWrite(true);
						Perspective CURRENT_PERSPECTIVE = ClientData.CLIENT.options.getPerspective();
						if (!CURRENT_PERSPECTIVE.isFirstPerson())
							ClientData.CLIENT.options.setPerspective(Perspective.FIRST_PERSON);
						for (int l = 0; l < 6; ++l) {
							switch (l) {
								case 0 -> {
									ClientData.CLIENT.player.setYaw(0.0F);
									ClientData.CLIENT.player.setPitch(0.0F);
								}
								case 1 -> {
									ClientData.CLIENT.player.setYaw(90.0F);
									ClientData.CLIENT.player.setPitch(0.0F);
								}
								case 2 -> {
									ClientData.CLIENT.player.setYaw(180.0F);
									ClientData.CLIENT.player.setPitch(0.0F);
								}
								case 3 -> {
									ClientData.CLIENT.player.setYaw(270.0F);
									ClientData.CLIENT.player.setPitch(0.0F);
								}
								case 4 -> {
									ClientData.CLIENT.player.setYaw(0.0F);
									ClientData.CLIENT.player.setPitch(-90.0F);
								}
								default -> {
									ClientData.CLIENT.player.setYaw(0.0F);
									ClientData.CLIENT.player.setPitch(90.0F);
								}
							}
							framebuffer.beginWrite(true);
							ClientData.CLIENT.gameRenderer.render(1.0F, 0L, true);
							try {
								if ((boolean) ConfigHelper.getConfig("super_secret_settings_enabled")) {
									PostEffectProcessor panoramaPerspectivePostProcessor = new PostEffectProcessor(ClientData.CLIENT.getTextureManager(), ClientData.CLIENT.getResourceManager(), framebuffer, (Identifier) Objects.requireNonNull(ShaderDataLoader.get((int) ConfigHelper.getConfig("super_secret_settings"), ShaderRegistryValue.ID)));
									panoramaPerspectivePostProcessor.setupDimensions(resolution, resolution);
									panoramaPerspectivePostProcessor.render(ClientData.CLIENT.getTickDelta());
								}
							} catch (Exception error) {
								Data.PERSPECTIVE_VERSION.sendToLog(Helper.LogType.ERROR, "Failed to add Super Secret Settings to Panorama.");
							}
							ScreenshotRecorder.saveScreenshot(new File(assetsDirLoc), "panorama_" + l + ".png", framebuffer);
						}
						if (!CURRENT_PERSPECTIVE.isFirstPerson())
							ClientData.CLIENT.options.setPerspective(CURRENT_PERSPECTIVE);
						File pack_file = new File(rpDirLoc + "/pack.mcmeta");
						if (pack_file.createNewFile()) {
							FileWriter pack_writer = new FileWriter(pack_file);
							pack_writer.write("{\"pack\": {\"pack_format\": 9, \"supported_formats\": {\"min_inclusive\": 9, \"max_inclusive\": 2147483647}, \"description\": \"" + panoramaName + "\"}}\"}}");
							pack_writer.close();
						}
						ClientData.CLIENT.getToastManager().add(new Toast(Translation.getTranslation("toasts.title", new Object[]{Translation.getTranslation("name"), Translation.getTranslation("toasts.take_panorama_screenshot.success.title")}), Translation.getTranslation("toasts.take_panorama_screenshot.success.description", new Object[]{Text.literal(panoramaName)}), 320, Toast.Type.INFO));
						ClientData.CLIENT.player.sendMessage(Translation.getTranslation("message.take_panorama_screenshot.success", new Object[]{Text.literal(panoramaName).formatted(Formatting.UNDERLINE).styled((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, new File(rpDirLoc).getAbsolutePath())))}));
						ClientData.CLIENT.player.setPitch(pitch);
						ClientData.CLIENT.player.setYaw(yaw);
						ClientData.CLIENT.gameRenderer.setBlockOutlineEnabled(true);
						ClientData.CLIENT.getWindow().setFramebufferWidth(framebufferWidth);
						ClientData.CLIENT.getWindow().setFramebufferHeight(framebufferHeight);
						ClientData.CLIENT.getFramebuffer().beginWrite(true);
						framebuffer.delete();
						ClientData.CLIENT.gameRenderer.setRenderingPanorama(false);
						ClientData.CLIENT.getFramebuffer().beginWrite(true);
					}
					if (getIncompatibleMods().size() != 0 || ClientData.CLIENT.options.getGraphicsMode().getValue().equals(GraphicsMode.FABULOUS)) {
						if ((boolean) ConfigHelper.getConfig("debug")) {
							Data.PERSPECTIVE_VERSION.getLogger().warn("{} Debug is enabled. The panorama screenshot might not have rendered as expected.", Data.PERSPECTIVE_VERSION.getLoggerPrefix());
							Text description = Translation.getTranslation("toasts.take_panorama_screenshot.debug.description");
							ClientData.CLIENT.getToastManager().add(new Toast(Translation.getTranslation("toasts.title", new Object[]{Translation.getTranslation("name"), Translation.getTranslation("toasts.take_panorama_screenshot.debug.title")}), description, 320, Toast.Type.WARNING));
						}
					}
				} else {
					if (getIncompatibleMods().size() != 0) {
						Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to take a panorama: Incompatible Mod(s): {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), getIncompatibleMods().toString().replace("[", "").replace("]", ""));
						Text description = (getIncompatibleMods().size() == 1) ? Translation.getTranslation("toasts.take_panorama_screenshot.failure.description.incompatible_mod", new Object[]{getIncompatibleMods().toString().replace("[", "").replace("]", "")}) : Translation.getTranslation("toasts.take_panorama_screenshot.failure.description.incompatible_mods", new Object[]{getIncompatibleMods().toString().replace("[", "").replace("]", "")});
						ClientData.CLIENT.getToastManager().add(new Toast(Translation.getTranslation("toasts.title", new Object[]{Translation.getTranslation("name"), Translation.getTranslation("toasts.take_panorama_screenshot.failure.title")}), description, 320, Toast.Type.WARNING));
					}
					if (ClientData.CLIENT.options.getGraphicsMode().getValue().equals(GraphicsMode.FABULOUS)) {
						Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to take a panorama: Take Panorama Screenshot is not compatible with Fabulous graphics.", Data.PERSPECTIVE_VERSION.getLoggerPrefix());
						ClientData.CLIENT.getToastManager().add(new Toast(Translation.getTranslation("toasts.title", new Object[]{Translation.getTranslation("name"), Translation.getTranslation("toasts.take_panorama_screenshot.failure.title")}), Translation.getTranslation("toasts.take_panorama_screenshot.failure.description.fabulous"), 320, Toast.Type.WARNING));
					}
				}
			} catch (Exception error) {
				Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to take a panorama: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
				ClientData.CLIENT.getToastManager().add(new Toast(Translation.getTranslation("toasts.title", new Object[]{Translation.getTranslation("name"), Translation.getTranslation("toasts.take_panorama_screenshot.failure.title")}), Text.of(String.valueOf(error)), 320, Toast.Type.WARNING));
			}
		}
	}
}