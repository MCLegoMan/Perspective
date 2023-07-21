/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.panorama;

import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.client.util.PerspectiveKeybindings;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class PerspectivePanorama {
    private static final String[] INCOMPATIBLE = new String[]{"sodium"};
    private static final List<String> INCOMPATIBLE_MODS_FOUND = new ArrayList<>();
    public static void init() {
        for (String mod : INCOMPATIBLE) if (FabricLoader.getInstance().isModLoaded(mod)) INCOMPATIBLE_MODS_FOUND.add(mod);
    }
    public static void tick(MinecraftClient client) {
        if (PerspectiveKeybindings.TAKE_PANORAMA_SCREENSHOT.wasPressed()) {
            if (INCOMPATIBLE_MODS_FOUND.size() == 0) {
                String time = getTime();
                File directory = new File(client.runDirectory.getPath() + "/panorama/" + time);
                if (directory.mkdirs()) {
                    float YAW = 0;
                    float PITCH = 0;
                    if (client.player != null) {
                        YAW = client.player.getYaw();
                        PITCH = client.player.getPitch();
                        client.player.setYaw(0);
                        client.player.setPitch(0);
                    }
                    client.takePanorama(directory, 1024, 1024);
                    if (client.player != null) {
                        client.player.setYaw(YAW);
                        client.player.setPitch(PITCH);
                    }
                }
                sendMessage(PerspectiveData.CLIENT, PerspectiveTranslation.getTranslation("message.take_panorama_screenshot.success", new Object[]{Text.literal(time).formatted(Formatting.UNDERLINE).styled((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, directory.getAbsolutePath())))}));
            } else sendMessage(PerspectiveData.CLIENT, PerspectiveTranslation.getTranslation("message.take_panorama_screenshot.failure", new Object[]{INCOMPATIBLE_MODS_FOUND.toString().replace("[", "").replace("]", "")}));
        }
    }
    private static String getTime() {
        return String.valueOf(LocalDateTime.now()).replace(":", "");
    }
    private static void sendMessage(MinecraftClient client, Text message) {
        client.inGameHud.getChatHud().addMessage(message);
    }
}