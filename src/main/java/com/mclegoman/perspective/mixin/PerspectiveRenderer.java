package com.mclegoman.perspective.mixin;

import com.mclegoman.perspective.PerspectiveMain;
import com.mclegoman.perspective.config.PerspectiveConfig;
import com.mclegoman.perspective.dataloader.PerspectiveSSSDataLoader;
import com.mclegoman.perspective.util.PerspectiveUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class PerspectiveRenderer {
    @Shadow @Final
    MinecraftClient client;
    @Shadow
    int superSecretSettingIndex;
    @Shadow private boolean postProcessorEnabled;
    private final Random random = Random.create();

    @Shadow @Nullable PostEffectProcessor postProcessor;
    @Shadow @Final public static int SUPER_SECRET_SETTING_COUNT;

    @Shadow abstract void loadPostProcessor(Identifier id);

    private final Formatting[] COLORS = new Formatting[]{Formatting.DARK_BLUE, Formatting.DARK_GREEN, Formatting.DARK_AQUA, Formatting.DARK_RED, Formatting.DARK_PURPLE, Formatting.GOLD, Formatting.BLUE, Formatting.GREEN, Formatting.AQUA, Formatting.RED, Formatting.LIGHT_PURPLE, Formatting.YELLOW};
    @Inject(method = "render", at = @At("RETURN"))
    private void render(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        try{
            keyHoldTPF();
            keyHoldTPB();
            keySetFP();
            keySetTPF();
            keySetTPB();
            keySSSCycle();
            keySSSToggle();
        } catch (Exception e) {
            PerspectiveMain.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void keyZoom(CallbackInfoReturnable<Double> callbackInfo) {
        try {
            if (PerspectiveUtils.KEY_ZOOM.isPressed()) {
                int FOV = callbackInfo.getReturnValue().intValue();
                int LEVEL = PerspectiveUtils.getZoomLevel(FOV, PerspectiveConfig.ZOOM_LEVEL);
                callbackInfo.setReturnValue((double) LEVEL);
            }
        } catch (Exception e) {
            PerspectiveMain.LOGGER.error(e.getLocalizedMessage());
        }
    }
    private void keySSSCycle() {
        try {
            if (PerspectiveUtils.KEY_CYCLE_SUPER_SECRET_SETTINGS.wasPressed()) {
                if (client.options.getPerspective().isFirstPerson()) {
                    boolean WAS_ENABLED = this.postProcessorEnabled;
                    try {
                        if (this.client.getCameraEntity() instanceof PlayerEntity) {
                            if (this.postProcessor != null) {
                                this.postProcessor.close();
                            }
                            this.superSecretSettingIndex = (this.superSecretSettingIndex + 1) % (PerspectiveSSSDataLoader.SHADERS.size() + 1);
                            if (this.superSecretSettingIndex == SUPER_SECRET_SETTING_COUNT) {
                                this.postProcessor = null;
                            } else {
                                this.loadPostProcessor(PerspectiveSSSDataLoader.SHADERS.get(this.superSecretSettingIndex));
                            }

                        }
                    } catch (Exception e) {
                        PerspectiveMain.LOGGER.error(e.getLocalizedMessage());
                        this.postProcessorEnabled = false;
                    }
                    if (!WAS_ENABLED && this.postProcessorEnabled)
                        client.inGameHud.getChatHud().addMessage(Text.translatable("chat.perspective.sss", Text.literal("[Super Secret Settings]:").formatted(COLORS[(random.nextInt(COLORS.length))], Formatting.BOLD), PerspectiveUtils.booleanToString(this.postProcessorEnabled)));
                    client.inGameHud.getChatHud().addMessage(Text.translatable("chat.perspective.sss", Text.literal("[Super Secret Settings]:").formatted(COLORS[(random.nextInt(COLORS.length))], Formatting.BOLD), String.valueOf(PerspectiveSSSDataLoader.SHADERS_NAME.get(this.superSecretSettingIndex))));
                }
            }
        } catch (Exception e) {
            PerspectiveMain.LOGGER.error(e.getLocalizedMessage());
            this.postProcessorEnabled = false;
            client.inGameHud.getChatHud().addMessage(Text.translatable("chat.perspective.sss", Text.literal("[Super Secret Settings]:").formatted(COLORS[(random.nextInt(COLORS.length))], Formatting.BOLD), PerspectiveUtils.booleanToString(this.postProcessorEnabled)));
        }
    }
    private void keySSSToggle() {
        try{
            if (this.client.getCameraEntity() instanceof PlayerEntity) {
                if (PerspectiveUtils.KEY_TOGGLE_SUPER_SECRET_SETTINGS.wasPressed()) {
                    if (client.options.getPerspective().isFirstPerson()) {
                        this.postProcessorEnabled = !this.postProcessorEnabled;
                        client.inGameHud.getChatHud().addMessage(Text.translatable("chat.perspective.sss", Text.literal("[Super Secret Settings]:").formatted(COLORS[(random.nextInt(COLORS.length))], Formatting.BOLD), PerspectiveUtils.booleanToString(this.postProcessorEnabled)));
                    }
                }
            }
        } catch (Exception e) {
            PerspectiveMain.LOGGER.error(e.getLocalizedMessage());
            this.postProcessorEnabled = false;
        }
    }
    private void keyHoldTPF() {
        try {
            if (PerspectiveUtils.KEY_HOLD_TPF.isPressed()) {
                PerspectiveUtils.KEY_HOLD_TPF_LOCK = true;
                client.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
            }
            else if (!PerspectiveUtils.KEY_HOLD_TPF.isPressed() && PerspectiveUtils.KEY_HOLD_TPF_LOCK) {
                PerspectiveUtils.KEY_HOLD_TPF_LOCK = false;
                client.options.setPerspective(Perspective.FIRST_PERSON);
            }
            else PerspectiveUtils.KEY_HOLD_TPF_LOCK = false;
        } catch (Exception e) {
            PerspectiveMain.LOGGER.error(e.getLocalizedMessage());
        }
    }
    private void keyHoldTPB() {
        try {
            if (PerspectiveUtils.KEY_HOLD_TPB.isPressed()) {
                PerspectiveUtils.KEY_HOLD_TPB_LOCK = true;
                client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            }
            else if (!PerspectiveUtils.KEY_HOLD_TPB.isPressed() && PerspectiveUtils.KEY_HOLD_TPB_LOCK) {
                PerspectiveUtils.KEY_HOLD_TPB_LOCK = false;
                client.options.setPerspective(Perspective.FIRST_PERSON);
            }
            else PerspectiveUtils.KEY_HOLD_TPB_LOCK = false;
        } catch (Exception e) {
            PerspectiveMain.LOGGER.error(e.getLocalizedMessage());
        }
    }
    private void keySetFP() {
        try {
            if (PerspectiveUtils.KEY_SET_FP.wasPressed()) {
                client.options.setPerspective(Perspective.FIRST_PERSON);
            }
        } catch (Exception e) {
            PerspectiveMain.LOGGER.error(e.getLocalizedMessage());
        }
    }
    private void keySetTPF() {
        try {
            if (PerspectiveUtils.KEY_SET_TPF.wasPressed()) {
                client.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
            }
        } catch (Exception e) {
            PerspectiveMain.LOGGER.error(e.getLocalizedMessage());
        }
    }
    private void keySetTPB() {
        try {
            if (PerspectiveUtils.KEY_SET_TPB.wasPressed()) {
                client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            }
        } catch (Exception e) {
            PerspectiveMain.LOGGER.error(e.getLocalizedMessage());
        }
    }
}