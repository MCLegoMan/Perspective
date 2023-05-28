package com.mclegoman.perspective.util;

import com.mclegoman.perspective.config.PerspectiveConfig;
import com.mclegoman.perspective.data.PerspectiveData;
import com.mclegoman.perspective.dataloader.PerspectiveSSSDataLoader;
import com.mclegoman.perspective.screen.PerspectiveConfigScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.player.PlayerEntity;
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
    public static boolean IS_ZOOMING_HOLD;
    public static boolean IS_ZOOMING_SET;
    public static boolean IS_ZOOMING_IN;
    public static boolean IS_ZOOMING_OUT;
    public static boolean HOLD_PERSPECTIVE_TPF_LOCK;
    public static boolean HOLD_PERSPECTIVE_TPB_LOCK;
    public static void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveSSSDataLoader());
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (PerspectiveUtils.IS_ZOOMING_IN) zoomIn();
            if (PerspectiveUtils.IS_ZOOMING_OUT) zoomOut();
        });
    }
    public static void cycleSSS(MinecraftClient client) {
        if (client.getCameraEntity() instanceof PlayerEntity) {
            if (PerspectiveUtils.SSS_COUNT < (PerspectiveSSSDataLoader.SHADERS.size() - 1)) PerspectiveUtils.SSS_COUNT = PerspectiveUtils.SSS_COUNT + 1;
            else PerspectiveUtils.SSS_COUNT = 0;
            if (!client.gameRenderer.postProcessorEnabled) {
                client.gameRenderer.togglePostProcessorEnabled();
                PerspectiveUtils.sendSSSMessage(PerspectiveUtils.booleanToString(client.gameRenderer.postProcessorEnabled));
            }
            client.gameRenderer.loadPostProcessor(PerspectiveSSSDataLoader.SHADERS.get(PerspectiveUtils.SSS_COUNT));
            PerspectiveUtils.sendSSSMessage(Text.literal(PerspectiveSSSDataLoader.SHADERS_NAME.get(PerspectiveUtils.SSS_COUNT)));
        }
    }
    public static void toggleSSS(MinecraftClient client) {
        client.gameRenderer.togglePostProcessorEnabled();
        PerspectiveUtils.sendSSSMessage(PerspectiveUtils.booleanToString(client.gameRenderer.postProcessorEnabled));
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
    public static Text booleanToString(boolean BOOLEAN) {
        if (BOOLEAN) return Text.translatable("chat.perspective.enabled");
        else return Text.translatable("chat.perspective.disabled");
    }
    public static void sendSSSMessage(Text TEXT) {
        MinecraftClient CLIENT = MinecraftClient.getInstance();
        CLIENT.inGameHud.getChatHud().addMessage(Text.translatable("chat.perspective.sss.message", Text.translatable("name.perspective.sss.title").formatted(COLORS[(RANDOM.nextInt(PerspectiveUtils.COLORS.length))], Formatting.BOLD), TEXT));
    }
    static {
        SHOW_OVERLAY = 0;
    }
}
