package com.mclegoman.perspective.mixin;

import com.mclegoman.perspective.PerspectiveClientMain;
import com.mclegoman.perspective.data.PerspectiveData;
import com.mclegoman.perspective.dataloader.PerspectiveSSSDataLoader;
import com.mclegoman.perspective.util.PerspectiveUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class PerspectiveRenderer {
    @Shadow @Final
    MinecraftClient client;
    @Shadow private boolean postProcessorEnabled;
    private final Random random = Random.create();
    @Shadow abstract void loadPostProcessor(Identifier id);
    @Shadow private boolean renderingPanorama;
    @Shadow private boolean renderHand;

    @Inject(method = "render", at = @At("RETURN"))
    private void render(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        try{
            keyPCycle();
            keyHoldTPB();
            keyHoldTPF();
            keySetTPB();
            keySetTPF();
            keySetFP();
            keySSSCycle();
            keySSSToggle();
        } catch (Exception e) {
            PerspectiveClientMain.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(method = "getFov", at = @At("HEAD"), cancellable = true)
    private void keyZoom(CallbackInfoReturnable<Double> callbackInfo) {
        try {
            if (!this.renderingPanorama) {
                if (PerspectiveUtils.KEY_ZOOM.isPressed()) {
                    this.renderHand = false;
                    int ZOOM_AMOUNT = PerspectiveUtils.ZOOM_LEVEL * client.options.getFov().getValue() / 100;
                    if (ZOOM_AMOUNT <= 0) ZOOM_AMOUNT = 1;
                    if (ZOOM_AMOUNT >= client.options.getFov().getValue()) ZOOM_AMOUNT = client.options.getFov().getValue();
                    callbackInfo.setReturnValue((double) ZOOM_AMOUNT);
                } else {
                    this.renderHand = true;
                }
            }
        } catch (Exception e) {
            PerspectiveClientMain.LOGGER.error(e.getLocalizedMessage());
        }
    }
    private void keySSSCycle() {
        try {
            if (PerspectiveUtils.KEY_CYCLE_SUPER_SECRET_SETTINGS.wasPressed()) {
                if (client.options.getPerspective().isFirstPerson()) {
                    try {
                        if (this.client.getCameraEntity() instanceof PlayerEntity) {
                            if (PerspectiveUtils.SSS_COUNT < (PerspectiveSSSDataLoader.SHADERS.size() - 1)) PerspectiveUtils.SSS_COUNT = PerspectiveUtils.SSS_COUNT + 1;
                            else PerspectiveUtils.SSS_COUNT = 0;
                            this.loadPostProcessor(PerspectiveSSSDataLoader.SHADERS.get(PerspectiveUtils.SSS_COUNT));
                            if (!this.postProcessorEnabled) client.inGameHud.getChatHud().addMessage(Text.translatable("chat.perspective.sss", Text.literal("[Super Secret Settings]:").formatted(PerspectiveUtils.COLORS[(random.nextInt(PerspectiveUtils.COLORS.length))], Formatting.BOLD), PerspectiveUtils.booleanToString(this.postProcessorEnabled)));
                            client.inGameHud.getChatHud().addMessage(Text.translatable("chat.perspective.sss", Text.literal("[Super Secret Settings]:").formatted(PerspectiveUtils.COLORS[(random.nextInt(PerspectiveUtils.COLORS.length))], Formatting.BOLD), String.valueOf(PerspectiveSSSDataLoader.SHADERS_NAME.get(PerspectiveUtils.SSS_COUNT))));
                        }
                    } catch (Exception e) {
                        PerspectiveClientMain.LOGGER.error(e.getLocalizedMessage());
                        this.postProcessorEnabled = false;
                    }
                }
            }
        } catch (Exception e) {
            PerspectiveClientMain.LOGGER.error(e.getLocalizedMessage());
            this.postProcessorEnabled = false;
            client.inGameHud.getChatHud().addMessage(Text.translatable("chat.perspective.sss", Text.literal("[Super Secret Settings]:").formatted(PerspectiveUtils.COLORS[(random.nextInt(PerspectiveUtils.COLORS.length))], Formatting.BOLD), PerspectiveUtils.booleanToString(this.postProcessorEnabled)));
        }
    }
    private void keyPCycle() {
        try {
            if (PerspectiveUtils.KEY_CYCLE_PERSPECTIVE.wasPressed()) {
                if (PerspectiveUtils.PERSPECTIVE_COUNT < (PerspectiveUtils.PERSPECTIVES.length - 1)) PerspectiveUtils.PERSPECTIVE_COUNT = PerspectiveUtils.PERSPECTIVE_COUNT + 1;
                else PerspectiveUtils.PERSPECTIVE_COUNT = 0;
                client.options.setPerspective(PerspectiveUtils.PERSPECTIVES[PerspectiveUtils.PERSPECTIVE_COUNT]);
            }
        } catch (Exception e) {
            PerspectiveClientMain.LOGGER.error(e.getLocalizedMessage());
        }
    }
    private void keySSSToggle() {
        try{
            if (this.client.getCameraEntity() instanceof PlayerEntity) {
                if (PerspectiveUtils.KEY_TOGGLE_SUPER_SECRET_SETTINGS.wasPressed()) {
                    if (client.options.getPerspective().isFirstPerson()) {
                        this.postProcessorEnabled = !this.postProcessorEnabled;
                        client.inGameHud.getChatHud().addMessage(Text.translatable("chat.perspective.sss", Text.literal("[Super Secret Settings]:").formatted(PerspectiveUtils.COLORS[(random.nextInt(PerspectiveUtils.COLORS.length))], Formatting.BOLD), PerspectiveUtils.booleanToString(this.postProcessorEnabled)));
                    }
                }
            }
        } catch (Exception e) {
            PerspectiveClientMain.LOGGER.error(e.getLocalizedMessage());
            this.postProcessorEnabled = false;
        }
    }
    private void keyHoldTPF() {
        try {
            if (PerspectiveUtils.KEY_HOLD_PERSPECTIVE_TPF.isPressed()) {
                PerspectiveUtils.KEY_HOLD_PERSPECTIVE_TPF_LOCK = true;
                client.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
            }
            else if (!PerspectiveUtils.KEY_HOLD_PERSPECTIVE_TPF.isPressed() && PerspectiveUtils.KEY_HOLD_PERSPECTIVE_TPF_LOCK) {
                PerspectiveUtils.KEY_HOLD_PERSPECTIVE_TPF_LOCK = false;
                client.options.setPerspective(Perspective.FIRST_PERSON);
            }
            else PerspectiveUtils.KEY_HOLD_PERSPECTIVE_TPF_LOCK = false;
        } catch (Exception e) {
            PerspectiveClientMain.LOGGER.error(e.getLocalizedMessage());
        }
    }
    private void keyHoldTPB() {
        try {
            if (PerspectiveUtils.KEY_HOLD_PERSPECTIVE_TPB.isPressed()) {
                PerspectiveUtils.KEY_HOLD_PERSPECTIVE_TPB_LOCK = true;
                client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            }
            else if (!PerspectiveUtils.KEY_HOLD_PERSPECTIVE_TPB.isPressed() && PerspectiveUtils.KEY_HOLD_PERSPECTIVE_TPB_LOCK) {
                PerspectiveUtils.KEY_HOLD_PERSPECTIVE_TPB_LOCK = false;
                client.options.setPerspective(Perspective.FIRST_PERSON);
            }
            else PerspectiveUtils.KEY_HOLD_PERSPECTIVE_TPB_LOCK = false;
        } catch (Exception e) {
            PerspectiveClientMain.LOGGER.error(e.getLocalizedMessage());
        }
    }
    private void keySetFP() {
        try {
            if (PerspectiveUtils.KEY_SET_PERSPECTIVE_FP.wasPressed()) {
                client.options.setPerspective(Perspective.FIRST_PERSON);
            }
        } catch (Exception e) {
            PerspectiveClientMain.LOGGER.error(e.getLocalizedMessage());
        }
    }
    private void keySetTPF() {
        try {
            if (PerspectiveUtils.KEY_SET_PERSPECTIVE_TPF.wasPressed()) {
                client.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
            }
        } catch (Exception e) {
            PerspectiveClientMain.LOGGER.error(e.getLocalizedMessage());
        }
    }
    private void keySetTPB() {
        try {
            if (PerspectiveUtils.KEY_SET_PERSPECTIVE_TPB.wasPressed()) {
                client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            }
        } catch (Exception e) {
            PerspectiveClientMain.LOGGER.error(e.getLocalizedMessage());
        }
    }
}