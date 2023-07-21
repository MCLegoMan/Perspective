/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.config.screen;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.config.screen.april_fools_prank.PerspectiveAprilFoolsPrankConfigScreen;
import com.mclegoman.perspective.client.config.screen.experimental.PerspectiveExperimentalConfigScreen;
import com.mclegoman.perspective.client.config.screen.information.PerspectiveInformationScreen;
import com.mclegoman.perspective.client.config.screen.shaders.PerspectiveShadersConfigScreen;
import com.mclegoman.perspective.client.config.screen.textured_entity.PerspectiveTexturedEntityConfigScreen;
import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.client.translation.PerspectiveTranslationType;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class PerspectiveConfigScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private final GridWidget GRID;
    private boolean SHOULD_CLOSE;
    public PerspectiveConfigScreen(Screen PARENT) {
        super(Text.literal(""));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = PARENT;
    }
    public void init() {
        try {
            GRID.getMainPositioner().alignHorizontalCenter().margin(0);
            GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
            GRID_ADDER.add(PerspectiveConfigScreenHelper.createTitle(client, new PerspectiveConfigScreen(PARENT_SCREEN)));
            GRID_ADDER.add(createZoom());
            GRID_ADDER.add(createSuperSecretSettingsAndTexturedEntity());
            GRID_ADDER.add(createAprilFools());
            GRID_ADDER.add(createFooter());
            GRID.refreshPositions();
            GRID.forEachChild(this::addDrawableChild);
            initTabNavigation();
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to initialize config screen: {}", (Object)error);
        }
    }

    public void tick() {
        if (this.SHOULD_CLOSE) {
            PerspectiveConfigHelper.saveConfig(false);
            PerspectiveData.CLIENT.setScreen(PARENT_SCREEN);
        }
    }
    private GridWidget createZoom() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        double ZOOM_LEVEL_VALUE = (double) (int) PerspectiveConfigHelper.getConfig("zoom_level") / 100;
        GRID_ADDER.add(new SliderWidget(GRID_ADDER.getGridWidget().getX(), GRID_ADDER.getGridWidget().getY(), 150, 20, PerspectiveTranslation.getConfigTranslation("zoom_level", new Object[]{Text.literal((int)PerspectiveConfigHelper.getConfig("zoom_level") + "%")}, false), ZOOM_LEVEL_VALUE) {
            @Override
            protected void updateMessage() {
                setMessage(PerspectiveTranslation.getConfigTranslation("zoom_level", new Object[]{Text.literal((int)PerspectiveConfigHelper.getConfig("zoom_level") + "%")}, false));
            }

            @Override
            protected void applyValue() {
                PerspectiveConfigHelper.setConfig("zoom_level", (int) ((value) * 100));
            }
        }).setTooltip(Tooltip.of(PerspectiveTranslation.getConfigTranslation("zoom_level", true)));
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("hide_hud", new Object[]{PerspectiveTranslation.getVariableTranslation((boolean)PerspectiveConfigHelper.getConfig("hide_hud"), PerspectiveTranslationType.ONFF)}), (button) -> {
            PerspectiveConfigHelper.setConfig("hide_hud", !(boolean)PerspectiveConfigHelper.getConfig("hide_hud"));
            PerspectiveData.CLIENT.setScreen(new PerspectiveConfigScreen(PARENT_SCREEN));
        }).build()).setTooltip(Tooltip.of(PerspectiveTranslation.getConfigTranslation("hide_hud", new Object[]{PerspectiveTranslation.getVariableTranslation((boolean)PerspectiveConfigHelper.getConfig("hide_hud"), PerspectiveTranslationType.ONFF)}, true)));
        return GRID;
    }
    private GridWidget createSuperSecretSettingsAndTexturedEntity() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("shaders"), (button) -> PerspectiveData.CLIENT.setScreen(new PerspectiveShadersConfigScreen(PerspectiveData.CLIENT.currentScreen))).build()).setTooltip(Tooltip.of(PerspectiveTranslation.getConfigTranslation("shaders", true)));
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("textured_entity"), (button) -> PerspectiveData.CLIENT.setScreen(new PerspectiveTexturedEntityConfigScreen(PerspectiveData.CLIENT.currentScreen))).build()).setTooltip(Tooltip.of(PerspectiveTranslation.getConfigTranslation("textured_entity", true)));
        return GRID;
    }
    private GridWidget createAprilFools() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("april_fools_prank"), (button) -> PerspectiveData.CLIENT.setScreen(new PerspectiveAprilFoolsPrankConfigScreen(PerspectiveData.CLIENT.currentScreen))).build()).setTooltip(Tooltip.of(PerspectiveTranslation.getConfigTranslation("april_fools_prank", true)));
        return GRID;
    }
    private GridWidget createFooter() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("information"), (button) -> PerspectiveData.CLIENT.setScreen(new PerspectiveInformationScreen(PerspectiveData.CLIENT.currentScreen))).build()).setTooltip(Tooltip.of(PerspectiveTranslation.getConfigTranslation("information", true)));
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("experimental"), (button) -> PerspectiveData.CLIENT.setScreen(new PerspectiveExperimentalConfigScreen(PerspectiveData.CLIENT.currentScreen))).build()).setTooltip(Tooltip.of(PerspectiveTranslation.getConfigTranslation("experimental", true)));
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("reset"), (button) -> {
            PerspectiveConfigHelper.resetConfig();
            PerspectiveData.CLIENT.setScreen(new PerspectiveConfigScreen(PARENT_SCREEN));
        }).build()).setTooltip(Tooltip.of(PerspectiveTranslation.getConfigTranslation("reset", true)));
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("back"), (button) -> this.SHOULD_CLOSE = true).build()).setTooltip(Tooltip.of(PerspectiveTranslation.getConfigTranslation("back", true)));
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
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) this.SHOULD_CLOSE = true;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }
}