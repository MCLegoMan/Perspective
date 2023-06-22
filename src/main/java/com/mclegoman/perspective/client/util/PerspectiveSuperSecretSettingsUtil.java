/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.client.dataloader.PerspectiveSuperSecretSettingsSoundsDataLoader;
import com.mclegoman.perspective.client.registry.PerspectiveKeybindings;
import com.mclegoman.perspective.common.data.PerspectiveData;
import com.mclegoman.perspective.client.dataloader.PerspectiveSuperSecretSettingsDataLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.resource.ResourceType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Random;

@Environment(EnvType.CLIENT)
public class PerspectiveSuperSecretSettingsUtil {
    private static Formatting[] COLORS = new Formatting[]{Formatting.DARK_BLUE, Formatting.DARK_GREEN, Formatting.DARK_AQUA, Formatting.DARK_RED, Formatting.DARK_PURPLE, Formatting.GOLD, Formatting.BLUE, Formatting.GREEN, Formatting.AQUA, Formatting.RED, Formatting.LIGHT_PURPLE, Formatting.YELLOW};
    private static Formatting LAST_COLOR;
    public static void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveSuperSecretSettingsDataLoader());
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveSuperSecretSettingsSoundsDataLoader());
    }
    public static void tick(MinecraftClient client) {
        if (PerspectiveKeybindings.KEY_CYCLE_SUPER_SECRET_SETTINGS.wasPressed()) cycle(client, client.options.sneakKey.isPressed());
        if (PerspectiveKeybindings.KEY_TOGGLE_SUPER_SECRET_SETTINGS.wasPressed()) toggle(client);
    }
    public static void toggle(MinecraftClient client) {
        try {
            client.gameRenderer.togglePostProcessorEnabled();
            sendMessage(client, booleanToString(client.gameRenderer.postProcessorEnabled));
            if (client.gameRenderer.postProcessorEnabled) set(client);
            else client.gameRenderer.postProcessor.close();
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
            try {
                client.world.playSound(client.player, client.player.getBlockPos(), PerspectiveSuperSecretSettingsSoundsDataLoader.getRandomSound(), SoundCategory.MASTER);
            } catch (Exception e) {
                PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to play random Super Secret Settings sound.");
                PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
            }
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to set Super Secret Settings.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
            PerspectiveConfig.SUPER_SECRET_SETTINGS = 0;
            PerspectiveConfig.write_to_file();
        }
    }
    private static Text booleanToString(Boolean BOOLEAN) {
        if (BOOLEAN) return Text.translatable("chat.perspective.enabled");
        else return Text.translatable("chat.perspective.disabled");
    }
    private static void sendMessage(MinecraftClient client, Text message) {
        client.inGameHud.getChatHud().addMessage(Text.translatable("chat.perspective.sss.message", Text.translatable("name.perspective.sss.title").formatted(getRandomColor(), Formatting.BOLD), message));
    }
    private static Formatting getRandomColor() {
        Random random = new Random();
        Formatting COLOR = LAST_COLOR;
        while (COLOR == LAST_COLOR) COLOR = COLORS[(random.nextInt(COLORS.length))];
        LAST_COLOR = COLOR;
        return COLOR;
    }
}