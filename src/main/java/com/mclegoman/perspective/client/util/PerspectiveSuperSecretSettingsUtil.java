/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0

*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.client.registry.PerspectiveKeybindings;
import com.mclegoman.perspective.common.data.PerspectiveData;
import com.mclegoman.perspective.client.dataloader.PerspectiveSuperSecretSettingsDataLoader;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Random;

public class PerspectiveSuperSecretSettingsUtil {
    public static Formatting[] COLORS = new Formatting[]{Formatting.DARK_BLUE, Formatting.DARK_GREEN, Formatting.DARK_AQUA, Formatting.DARK_RED, Formatting.DARK_PURPLE, Formatting.GOLD, Formatting.BLUE, Formatting.GREEN, Formatting.AQUA, Formatting.RED, Formatting.LIGHT_PURPLE, Formatting.YELLOW};
    public static void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveSuperSecretSettingsDataLoader());
    }
    public static void tick(MinecraftClient client) {
        if (PerspectiveKeybindings.KEY_CYCLE_SUPER_SECRET_SETTINGS.wasPressed()) cycle(client);
        if (PerspectiveKeybindings.KEY_TOGGLE_SUPER_SECRET_SETTINGS.wasPressed()) toggle(client);
    }
    public static void toggle(MinecraftClient client) {
        try {
            client.gameRenderer.togglePostProcessorEnabled();
            sendMessage(client, booleanToString(client.gameRenderer.postProcessorEnabled));
            if (client.gameRenderer.postProcessorEnabled) set(client);
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to toggle Super Secret Settings.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
    }
    public static void cycle(MinecraftClient client) {
        try {
            if (PerspectiveConfig.SUPER_SECRET_SETTINGS < PerspectiveSuperSecretSettingsDataLoader.getShaderAmount()) PerspectiveConfig.SUPER_SECRET_SETTINGS = PerspectiveConfig.SUPER_SECRET_SETTINGS + 1;
            else PerspectiveConfig.SUPER_SECRET_SETTINGS = 0;
            if (!client.gameRenderer.postProcessorEnabled) toggle(client);
            else set(client);
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to cycle Super Secret Settings.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
    }
    private static void set(MinecraftClient client) {
        try {
            client.gameRenderer.loadPostProcessor(PerspectiveSuperSecretSettingsDataLoader.SHADERS.get(PerspectiveConfig.SUPER_SECRET_SETTINGS));
            sendMessage(client, Text.of(PerspectiveSuperSecretSettingsDataLoader.SHADERS_NAME.get(PerspectiveConfig.SUPER_SECRET_SETTINGS)));
            PerspectiveConfig.write_to_file();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to set Super Secret Settings.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
            PerspectiveConfig.SUPER_SECRET_SETTINGS = 0;
            PerspectiveConfig.write_to_file();
        }
    }
    private static Text booleanToString(boolean BOOLEAN) {
        if (BOOLEAN) return Text.translatable("chat.perspective.enabled");
        else return Text.translatable("chat.perspective.disabled");
    }
    private static void sendMessage(MinecraftClient client, Text message) {
        Random random = new Random();
        client.inGameHud.getChatHud().addMessage(Text.translatable("chat.perspective.sss.message", Text.translatable("name.perspective.sss.title").formatted(COLORS[(random.nextInt(COLORS.length))], Formatting.BOLD), message));
    }
}