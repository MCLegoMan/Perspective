/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.screen.config;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.screen.config.april_fools_prank.PerspectiveAprilFoolsPrankConfigScreen;
import com.mclegoman.perspective.client.screen.config.experimental.PerspectiveExperimentalConfigScreen;
import com.mclegoman.perspective.client.screen.config.hold_perspective.PerspectiveHoldPerspectiveConfigScreen;
import com.mclegoman.perspective.client.screen.config.information.PerspectiveInformationScreen;
import com.mclegoman.perspective.client.screen.config.more_options.PerspectiveMoreOptionsConfigScreen;
import com.mclegoman.perspective.client.screen.config.shaders.PerspectiveShadersConfigScreen;
import com.mclegoman.perspective.client.screen.config.textured_entity.PerspectiveTexturedEntityConfigScreen;
import com.mclegoman.perspective.client.screen.config.zoom.PerspectiveZoomConfigScreen;
import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.client.util.PerspectiveKeybindings;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class PerspectiveConfigScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private boolean REFRESH;
    private final GridWidget GRID;
    private boolean SHOULD_CLOSE;
    public PerspectiveConfigScreen(Screen PARENT, boolean REFRESH) {
        super(Text.literal(""));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = PARENT;
        this.REFRESH = REFRESH;
    }
    public void init() {
        try {
            GRID.getMainPositioner().alignHorizontalCenter().margin(0);
            GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
            GRID_ADDER.add(PerspectiveConfigScreenHelper.createTitle(client, new PerspectiveConfigScreen(PARENT_SCREEN, true), false, ""));
            GRID_ADDER.add(createConfig());
            GRID_ADDER.add(createInformationAndExperiments());
            GRID_ADDER.add(new EmptyWidget(4, 4));
            GRID_ADDER.add(createFooter());
            GRID.refreshPositions();
            GRID.forEachChild(this::addDrawableChild);
            initTabNavigation();
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to initialize config screen: {}", (Object)error);
        }
    }

    public void tick() {
        try {
            if (this.REFRESH) {
                PerspectiveClientData.CLIENT.setScreen(new PerspectiveConfigScreen(PARENT_SCREEN, false));
            }
            if (this.SHOULD_CLOSE) {
                PerspectiveConfigHelper.saveConfig(false);
                PerspectiveClientData.CLIENT.setScreen(PARENT_SCREEN);
            }
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to tick perspective$config screen: {}", (Object)error);
        }
    }
    private GridWidget createConfig() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("zoom"), (button) -> PerspectiveClientData.CLIENT.setScreen(new PerspectiveZoomConfigScreen(new PerspectiveConfigScreen(PARENT_SCREEN, true), false))).build());
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("shaders"), (button) -> PerspectiveClientData.CLIENT.setScreen(new PerspectiveShadersConfigScreen(new PerspectiveConfigScreen(PARENT_SCREEN, true), false, false))).build());
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("hold_perspective"), (button) -> PerspectiveClientData.CLIENT.setScreen(new PerspectiveHoldPerspectiveConfigScreen(new PerspectiveConfigScreen(PARENT_SCREEN, true), false))).build());
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("textured_entity"), (button) -> PerspectiveClientData.CLIENT.setScreen(new PerspectiveTexturedEntityConfigScreen(new PerspectiveConfigScreen(PARENT_SCREEN, true)))).build());
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("april_fools_prank"), (button) -> PerspectiveClientData.CLIENT.setScreen(new PerspectiveAprilFoolsPrankConfigScreen(new PerspectiveConfigScreen(PARENT_SCREEN, true)))).build());
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("more_options"), (button) -> PerspectiveClientData.CLIENT.setScreen(new PerspectiveMoreOptionsConfigScreen(new PerspectiveConfigScreen(PARENT_SCREEN, true), false))).build());
        return GRID;
    }
    private GridWidget createInformationAndExperiments() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("information"), (button) -> PerspectiveClientData.CLIENT.setScreen(new PerspectiveInformationScreen(new PerspectiveConfigScreen(PARENT_SCREEN, true)))).build());
        ButtonWidget EXPERIMENTAL = ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("experimental"), (button) -> PerspectiveClientData.CLIENT.setScreen(new PerspectiveExperimentalConfigScreen(new PerspectiveConfigScreen(PARENT_SCREEN, true), false))).build();
        EXPERIMENTAL.active = PerspectiveConfigHelper.EXPERIMENTS_AVAILABLE;
        GRID_ADDER.add(EXPERIMENTAL);
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
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }
}