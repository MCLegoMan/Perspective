/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.panorama;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.shaders.Shader;
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
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Panorama {
    @Nullable
    private static PostEffectProcessor panoramaPostProcessor;
    private static final String[] INCOMPATIBLE = new String[]{"sodium"};
    private static final List<String> INCOMPATIBLE_MODS_FOUND = new ArrayList<>();

    public static void init() {
        for (String mod : INCOMPATIBLE)
            if (FabricLoader.getInstance().isModLoaded(mod)) INCOMPATIBLE_MODS_FOUND.add(mod);
    }

    public static void tick(MinecraftClient client) {
        if (Keybindings.TAKE_PANORAMA_SCREENSHOT.wasPressed()) takePanorama(1024);
    }

    private static String getTime() {
        return String.valueOf(LocalDateTime.now()).replace(":", "");
    }
    private static void takePanorama(int resolution) {
        boolean shouldRenderShader = (boolean) ConfigHelper.getConfig("super_secret_settings_enabled");
        if (ClientData.CLIENT.player != null) {
            try {
                if (INCOMPATIBLE_MODS_FOUND.size() == 0) {
                    String panoramaName = getTime();
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
                        for(int l = 0; l < 6; ++l) {
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
                            ClientData.CLIENT.gameRenderer.render(ClientData.CLIENT.getTickDelta(), Util.getMeasuringTimeNano(), true);
                            Shader.render(ClientData.CLIENT.getTickDelta(), "panorama");

                            if (shouldRenderShader) {
                                panoramaPostProcessor = new PostEffectProcessor(ClientData.CLIENT.getTextureManager(), ClientData.CLIENT.getResourceManager(), framebuffer, (Identifier) Objects.requireNonNull(ShaderDataLoader.get((int) ConfigHelper.getConfig("super_secret_settings"), ShaderRegistryValue.ID)));
                                panoramaPostProcessor.setupDimensions(resolution, resolution);
                                panoramaPostProcessor.render(ClientData.CLIENT.getTickDelta());
                            }

                            ScreenshotRecorder.saveScreenshot(new File(assetsDirLoc), "panorama_" + l + ".png", framebuffer);
                            if (panoramaPostProcessor != null) panoramaPostProcessor.close();
                        }
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
                        framebuffer.delete();
                        ClientData.CLIENT.gameRenderer.setRenderingPanorama(false);
                        ClientData.CLIENT.getFramebuffer().beginWrite(true);
                    }
                } else {
                    Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to take a panorama: Incompatible Mods: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), INCOMPATIBLE_MODS_FOUND.toString().replace("[", "").replace("]", ""));
                    ClientData.CLIENT.getToastManager().add(new Toast(Translation.getTranslation("toasts.title", new Object[]{Translation.getTranslation("name"), Translation.getTranslation("toasts.take_panorama_screenshot.failure.title")}), Translation.getTranslation("toasts.take_panorama_screenshot.failure.description", new Object[]{INCOMPATIBLE_MODS_FOUND.toString().replace("[", "").replace("]", "")}), 320, Toast.Type.WARNING));
                }
            } catch (Exception error) {
                Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to take a panorama: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
                ClientData.CLIENT.getToastManager().add(new Toast(Translation.getTranslation("toasts.title", new Object[]{Translation.getTranslation("name"), Translation.getTranslation("toasts.take_panorama_screenshot.failure.title")}), Text.of(String.valueOf(error)), 320, Toast.Type.WARNING));
            }
        }
    }
}