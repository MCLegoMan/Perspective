/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.panorama;

import com.mclegoman.perspective.client.registry.PerspectiveKeybindings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.File;
import java.time.LocalDateTime;

@Environment(EnvType.CLIENT)
public class PerspectivePanoramaUtils {
    public static void tick(MinecraftClient client) {
        if (PerspectiveKeybindings.KEY_PANORAMA_SCREENSHOT.wasPressed()) {
            if (!FabricLoader.getInstance().isModLoaded("sodium")) {
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
                sendMessage(client, Text.translatable("chat.perspective.panorama.saved", Text.literal(time).formatted(Formatting.UNDERLINE).styled((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, directory.getAbsolutePath())))));
            } else sendMessage(client, Text.translatable("chat.perspective.panorama.incompatible", Text.literal("sodium")));
        }
    }
    private static String getTime() {
        return String.valueOf(LocalDateTime.now()).replace(":", "");
    }
    private static void sendMessage(MinecraftClient client, Text message) {
        client.inGameHud.getChatHud().addMessage(message);
    }
}