package com.mclegoman.perspective.util;

import com.mclegoman.perspective.config.PerspectiveConfig;
import com.mclegoman.perspective.dataloader.PerspectiveSSSDataLoader;
import com.mclegoman.perspective.registry.PerspectiveKeybindRegistry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Random;

public class PerspectiveUtils {
    public static int SHOW_OVERLAY;
    public static Formatting[] COLORS = new Formatting[]{Formatting.DARK_BLUE, Formatting.DARK_GREEN, Formatting.DARK_AQUA, Formatting.DARK_RED, Formatting.DARK_PURPLE, Formatting.GOLD, Formatting.BLUE, Formatting.GREEN, Formatting.AQUA, Formatting.RED, Formatting.LIGHT_PURPLE, Formatting.YELLOW};
    public static Perspective[] PERSPECTIVES = new Perspective[]{Perspective.FIRST_PERSON, Perspective.THIRD_PERSON_BACK, Perspective.THIRD_PERSON_FRONT};
    public static int PERSPECTIVE_COUNT = 0;
    public static int SSS_COUNT = 0;
    private static final Random RANDOM = new Random();
    public static void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveSSSDataLoader());
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (PerspectiveKeybindRegistry.IS_ZOOMING_IN) zoomIn();
            if (PerspectiveKeybindRegistry.IS_ZOOMING_OUT) zoomOut();
        });
    }
    public static void zoomIn() {
        if (!((PerspectiveConfig.ZOOM_LEVEL - 1) < 0)) {
            PerspectiveConfig.ZOOM_LEVEL = PerspectiveConfig.ZOOM_LEVEL - 1;
            PerspectiveConfig.write_to_file();
        }
    }
    public static void zoomOut() {
        if (!((PerspectiveConfig.ZOOM_LEVEL + 1) > 100)) {
            PerspectiveConfig.ZOOM_LEVEL = PerspectiveConfig.ZOOM_LEVEL + 1;
            PerspectiveConfig.write_to_file();
        }
    }
    public static String booleanToString(boolean BOOLEAN) {
        if (BOOLEAN) return "enabled";
        else return "disabled";
    }
    public static void sendSSSMessage(String STRING) {
        MinecraftClient CLIENT = MinecraftClient.getInstance();
        CLIENT.inGameHud.getChatHud().addMessage(Text.translatable("chat.perspective.sss", Text.literal("[Super Secret Settings]:").formatted(COLORS[(RANDOM.nextInt(PerspectiveUtils.COLORS.length))], Formatting.BOLD), STRING));
    }
    static {
        SHOW_OVERLAY = 0;
    }
}
