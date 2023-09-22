/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.screen.config.shaders;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.screen.config.PerspectiveConfigScreenHelper;
import com.mclegoman.perspective.client.shaders.PerspectiveShader;
import com.mclegoman.perspective.client.shaders.PerspectiveShaderDataLoader;
import com.mclegoman.perspective.client.shaders.PerspectiveShaderRegistryValue;
import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.client.translation.PerspectiveTranslationType;
import com.mclegoman.perspective.client.util.PerspectiveKeybindings;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class PerspectiveShadersConfigScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private final GridWidget GRID;
    private boolean SHOULD_CLOSE;
    private final boolean SAVE_ON_CLOSE;
    private boolean REFRESH;
    private boolean CYCLE_DIRECTION = true;
    public PerspectiveShadersConfigScreen(Screen PARENT, boolean SAVE_ON_CLOSE, boolean REFRESH) {
        super(Text.literal(""));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = PARENT;
        this.SAVE_ON_CLOSE = SAVE_ON_CLOSE;
        this.REFRESH = REFRESH;
    }
    public void init() {
        try {
            GRID.getMainPositioner().alignHorizontalCenter().margin(0);
            GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
            GRID_ADDER.add(PerspectiveConfigScreenHelper.createTitle(PerspectiveClientData.CLIENT, new PerspectiveShadersConfigScreen(PARENT_SCREEN, SAVE_ON_CLOSE, false), true, "shaders"));
            GRID_ADDER.add(createShaders());
            GRID_ADDER.add(createShaderOptions());
            GRID_ADDER.add(new EmptyWidget(4, 4));
            GRID_ADDER.add(createFooter());
            GRID.refreshPositions();
            GRID.forEachChild(this::addDrawableChild);
            initTabNavigation();
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to initialize config>shaders screen: {}", (Object)error);
        }
    }

    public void tick() {
        try {
            if (this.REFRESH) {
                PerspectiveClientData.CLIENT.setScreen(new PerspectiveShadersConfigScreen(PARENT_SCREEN, SAVE_ON_CLOSE, false));
            }
            if (this.SHOULD_CLOSE) {
                if (this.SAVE_ON_CLOSE) PerspectiveConfigHelper.saveConfig(false);
                PerspectiveClientData.CLIENT.setScreen(PARENT_SCREEN);
            }
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to tick perspective$config$shaders screen: {}", (Object)error);
        }
    }
    private GridWidget createShaders() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);

        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("shaders.shader", new Object[]{PerspectiveShaderDataLoader.get((int)PerspectiveConfigHelper.getConfig("super_secret_settings"), PerspectiveShaderRegistryValue.NAME)}, new Formatting[]{PerspectiveShader.getRandomColor()}), (button) -> {
            PerspectiveShader.cycle(PerspectiveClientData.CLIENT, CYCLE_DIRECTION, true, false);
            this.REFRESH = true;
        }).width(280).build());

        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("shaders.random"), (button) -> {
            PerspectiveShader.random(true, false);
            this.REFRESH = true;
        }).width(20).build());
        return GRID;
    }
    private GridWidget createShaderOptions() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);

        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("shaders.mode", new Object[]{PerspectiveTranslation.getVariableTranslation((boolean) PerspectiveConfigHelper.getConfig("super_secret_settings_mode"), PerspectiveTranslationType.SHADER_MODE), PerspectiveTranslation.getVariableTranslation(PerspectiveShader.shouldDisableScreenMode(), PerspectiveTranslationType.DISABLE_SCREEN_MODE)}), (button) -> {
            PerspectiveConfigHelper.setConfig("super_secret_settings_mode", !(boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_mode"));
            this.REFRESH = true;
        }).build());

        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("shaders.toggle", new Object[]{PerspectiveTranslation.getVariableTranslation((boolean) PerspectiveConfigHelper.getConfig("super_secret_settings_enabled"), PerspectiveTranslationType.ENDISABLE)}), (button) -> {
            PerspectiveShader.toggle(PerspectiveClientData.CLIENT, true, false);
            this.REFRESH = true;
        }).build());
        return GRID;
    }
    private GridWidget createFooter() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("reset"), (button) -> {
            PerspectiveConfigHelper.resetConfig();
            this.REFRESH = true;
        }).build());
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("back"), (button) -> this.SHOULD_CLOSE = true).build());
        return GRID;
    }

    public void initTabNavigation() {
        SimplePositioningWidget.setPos(GRID, getNavigationFocus());
    }
    public Text getNarratedTitle() {
        return ScreenTexts.joinSentences();
    }
    public boolean shouldCloseOnEsc() {
        return false;
    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == KeyBindingHelper.getBoundKeyOf(PerspectiveKeybindings.OPEN_CONFIG).getCode()) this.SHOULD_CLOSE = true;
        if (keyCode == KeyBindingHelper.getBoundKeyOf(PerspectiveClientData.CLIENT.options.sneakKey).getCode()) this.CYCLE_DIRECTION = false;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT) this.CYCLE_DIRECTION = true;
        return super.keyReleased(keyCode, scanCode, modifiers);
    }
}