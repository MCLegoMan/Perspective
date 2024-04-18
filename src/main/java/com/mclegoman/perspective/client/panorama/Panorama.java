/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.panorama;

import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.toasts.Toast;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.option.Perspective;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Panorama {
	private static final List<String> incompatibleMods = new ArrayList<>();

	public static void addIncompatibleMod(String modID) {
		if (!incompatibleMods.contains(modID)) incompatibleMods.add(modID);
	}

	public static List<String> getIncompatibleMods() {
		List<String> incompatibleModsFound = new ArrayList<>();
		for (String modID : incompatibleMods) {
			if (Data.isModInstalled(modID)) {
				incompatibleModsFound.add(Data.getModContainer(modID).getMetadata().getName());
			}
		}
		return incompatibleModsFound;
	}

	public static void init() {
		addIncompatibleMod("canvas");
		addIncompatibleMod("iris");
	}

	public static void tick() {
		if (Keybindings.takePanoScreenshot.wasPressed()) takePanorama(1024, 0.0F);
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
		return (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "debug") || getIncompatibleMods().isEmpty() && !ClientData.minecraft.options.getGraphicsMode().getValue().equals(GraphicsMode.FABULOUS);
	}

	private static void takePanorama(int resolution, float startingYaw) {
		try {
			if (shouldTakePanorama()) {
				if (ClientData.minecraft.player != null) {
					float playerPitch = ClientData.minecraft.player.getPitch();
					float playerYaw = ClientData.minecraft.player.getYaw();

					int framebufferWidth = ClientData.minecraft.getWindow().getFramebufferWidth();
					int framebufferHeight = ClientData.minecraft.getWindow().getFramebufferHeight();
					Framebuffer framebuffer = new SimpleFramebuffer(resolution, resolution, true, MinecraftClient.IS_SYSTEM_MAC);
					ClientData.minecraft.getWindow().setFramebufferWidth(resolution);
					ClientData.minecraft.getWindow().setFramebufferHeight(resolution);

					if (Shader.shouldRenderShader()) Shader.set(true, false, false, true, framebuffer, resolution, resolution);

					Perspective playerPerspective = ClientData.minecraft.options.getPerspective();
					if (!playerPerspective.isFirstPerson()) ClientData.minecraft.options.setPerspective(Perspective.FIRST_PERSON);

					ClientData.minecraft.gameRenderer.setBlockOutlineEnabled(false);
					ClientData.minecraft.gameRenderer.setRenderingPanorama(true);

					String panoramaName = getFilename();
					File resourcePackDir = new File(ClientData.minecraft.runDirectory.getPath() + "/resourcepacks/" + panoramaName);
					File screenshotsDir = new File(resourcePackDir + "/assets/minecraft/textures/gui/title/background");

					if (screenshotsDir.mkdirs()) {
						// Pre-render world
						for (int l = 0; l < 100; ++l) {
							framebuffer.beginWrite(true);
							ClientData.minecraft.gameRenderer.renderWorld(0.0F, 0L);
						}
						// Create panoramic screenshots
						for (int l = 0; l < 6; ++l) {
							switch (l) {
								case 0 -> {
									ClientData.minecraft.player.setYaw((startingYaw + 0.0F) % 360.0F);
									ClientData.minecraft.player.setPitch(0.0F);
								}
								case 1 -> {
									ClientData.minecraft.player.setYaw((startingYaw + 90.0F) % 360.0F);
									ClientData.minecraft.player.setPitch(0.0F);
								}
								case 2 -> {
									ClientData.minecraft.player.setYaw((startingYaw + 180.0F) % 360.0F);
									ClientData.minecraft.player.setPitch(0.0F);
								}
								case 3 -> {
									ClientData.minecraft.player.setYaw((startingYaw + 270.0F) % 360.0F);
									ClientData.minecraft.player.setPitch(0.0F);
								}
								case 4 -> {
									ClientData.minecraft.player.setYaw((startingYaw + 0.0F) % 360.0F);
									ClientData.minecraft.player.setPitch(-90.0F);
								}
								case 5 -> {
									ClientData.minecraft.player.setYaw((startingYaw + 0.0F) % 360.0F);
									ClientData.minecraft.player.setPitch(90.0F);
								}
							}
							framebuffer.beginWrite(true);
							ClientData.minecraft.gameRenderer.renderWorld(0.0F, 0L);
							ScreenshotRecorder.saveScreenshot(screenshotsDir, "panorama_" + l + ".png", framebuffer);
						}

						// Create pack.mcmeta
						File packFile = new File(resourcePackDir + "/pack.mcmeta");
						if (packFile.createNewFile()) {
							FileWriter packWriter = new FileWriter(packFile);
							packWriter.write("{\"pack\": {\"pack_format\": " + SharedConstants.getGameVersion().getResourceVersion(ResourceType.CLIENT_RESOURCES) + ", \"supported_formats\": {\"min_inclusive\": 1, \"max_inclusive\": 2147483647}, \"description\": \"" + panoramaName + "\"}}\"}}");
							packWriter.close();
						}
						// Create pack.png

					}

					ClientData.minecraft.getWindow().setFramebufferWidth(framebufferWidth);
					ClientData.minecraft.getWindow().setFramebufferHeight(framebufferHeight);
					if (Shader.shouldRenderShader()) Shader.set(true, false, false, true);
					framebuffer.delete();

					ClientData.minecraft.player.setPitch(playerPitch);
					ClientData.minecraft.player.setYaw(playerYaw);
					if (!playerPerspective.isFirstPerson()) ClientData.minecraft.options.setPerspective(playerPerspective);
					ClientData.minecraft.gameRenderer.setBlockOutlineEnabled(true);
					ClientData.minecraft.gameRenderer.setRenderingPanorama(false);
					ClientData.minecraft.getFramebuffer().beginWrite(true);
					ClientData.minecraft.player.sendMessage(Translation.getTranslation(Data.version.getID(), "message.take_panorama_screenshot.success", new Object[]{Text.literal(panoramaName).formatted(Formatting.UNDERLINE).styled((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, resourcePackDir.getAbsolutePath())))}));
				}
			} else {
				Text errorTitle = Translation.getTranslation(Data.version.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.take_panorama_screenshot.failure.title")});
				if (getIncompatibleMods().size() != 0) {
					String incompatibleMods = getIncompatibleMods().toString().replace("[", "").replace("]", "");
					Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to take panoramic screenshot: Incompatible Mod(s): {}", incompatibleMods));
					Text errorDescription = (getIncompatibleMods().size() == 1) ? Translation.getTranslation(Data.version.getID(), "toasts.take_panorama_screenshot.failure.description.incompatible_mod", new Object[]{incompatibleMods}) : Translation.getTranslation(Data.version.getID(), "toasts.take_panorama_screenshot.failure.description.incompatible_mods", new Object[]{incompatibleMods});
					ClientData.minecraft.getToastManager().add(new Toast(errorTitle, errorDescription, 320, Toast.Type.WARNING));
				}
				if (ClientData.minecraft.options.getGraphicsMode().getValue().equals(GraphicsMode.FABULOUS)) {
					Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to take panoramic screenshot: Unsupported Graphics Mode: Fabulous"));
					Text errorDescription = Translation.getTranslation(Data.version.getID(), "toasts.take_panorama_screenshot.failure.description.fabulous");
					ClientData.minecraft.getToastManager().add(new Toast(errorTitle, errorDescription, 320, Toast.Type.WARNING));
				}
			}
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to take panoramic screenshot: {}", error));
		}
	}
}