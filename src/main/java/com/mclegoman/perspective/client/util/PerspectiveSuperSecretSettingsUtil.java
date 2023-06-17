/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.client.dataloader.PerspectiveSuperSecretSettingsDataLoader;
import com.mclegoman.perspective.client.registry.PerspectiveKeybindings;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Random;

public class PerspectiveSuperSecretSettingsUtil {
    private static Formatting[] COLORS = new Formatting[]{Formatting.DARK_BLUE, Formatting.DARK_GREEN, Formatting.DARK_AQUA, Formatting.DARK_RED, Formatting.DARK_PURPLE, Formatting.GOLD, Formatting.BLUE, Formatting.GREEN, Formatting.AQUA, Formatting.RED, Formatting.LIGHT_PURPLE, Formatting.YELLOW};
    private static Formatting LAST_COLOR;
    public static void init() {
        //ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveSuperSecretSettingsDataLoader());
    }
    public static void tick(MinecraftClient client) {
        if (PerspectiveKeybindings.KEY_CYCLE_SUPER_SECRET_SETTINGS.wasPressed()) cycle(client, client.options.sneakKey.isPressed());
        if (PerspectiveKeybindings.KEY_TOGGLE_SUPER_SECRET_SETTINGS.wasPressed()) toggle(client);
    }
    public static void toggle(MinecraftClient client) {
        try {
            client.gameRenderer.toggleShadersEnabled();
            sendMessage(client, booleanToString(client.gameRenderer.shadersEnabled));
            if (client.gameRenderer.shadersEnabled) set(client);
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to toggle Super Secret Settings.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
    }
    public static void cycle(MinecraftClient client, Boolean forwards) {
        try {
            if (forwards) {
                if (PerspectiveConfig.SUPER_SECRET_SETTINGS < PerspectiveSuperSecretSettingsDataLoader.getShaderAmount()) PerspectiveConfig.SUPER_SECRET_SETTINGS = PerspectiveConfig.SUPER_SECRET_SETTINGS + 1;
                else PerspectiveConfig.SUPER_SECRET_SETTINGS = 0;
            } else {
                if (PerspectiveConfig.SUPER_SECRET_SETTINGS > 0) PerspectiveConfig.SUPER_SECRET_SETTINGS = PerspectiveConfig.SUPER_SECRET_SETTINGS - 1;
                else PerspectiveConfig.SUPER_SECRET_SETTINGS = PerspectiveSuperSecretSettingsDataLoader.getShaderAmount();
            }
            if (!client.gameRenderer.shadersEnabled) toggle(client);
            else set(client);
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to cycle Super Secret Settings.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
    }
    private static void set(MinecraftClient client) {
        try {
            client.gameRenderer.loadShader(PerspectiveSuperSecretSettingsDataLoader.SHADERS.get(PerspectiveConfig.SUPER_SECRET_SETTINGS));
            sendMessage(client, PerspectiveSuperSecretSettingsDataLoader.SHADERS_NAME.get(PerspectiveConfig.SUPER_SECRET_SETTINGS));
            PerspectiveConfig.write_to_file();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to set Super Secret Settings.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
            PerspectiveConfig.SUPER_SECRET_SETTINGS = 0;
            PerspectiveConfig.write_to_file();
        }
    }
    private static String booleanToString(Boolean BOOLEAN) {
        if (BOOLEAN) return I18n.translate("chat.perspective.enabled");
        else return I18n.translate("chat.perspective.disabled");
    }
    private static void sendMessage(MinecraftClient client, String message) {
        client.inGameHud.getChatHud().addMessage(Text.Serializer.deserializeText(I18n.translate("chat.perspective.sss.message", I18n.translate("name.perspective.sss.title"), message)));
    }
    private static Formatting getRandomColor() {
        Random random = new Random();
        Formatting COLOR = LAST_COLOR;
        while (COLOR == LAST_COLOR) COLOR = COLORS[(random.nextInt(COLORS.length))];
        LAST_COLOR = COLOR;
        return COLOR;
    }
}