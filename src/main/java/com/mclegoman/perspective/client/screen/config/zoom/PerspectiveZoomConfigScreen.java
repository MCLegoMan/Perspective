/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.screen.config.zoom;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.screen.config.PerspectiveConfigScreenHelper;
import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.client.translation.PerspectiveTranslationType;
import com.mclegoman.perspective.client.util.PerspectiveKeybindings;
import com.mclegoman.perspective.client.zoom.PerspectiveZoom;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class PerspectiveZoomConfigScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private boolean REFRESH;
    private final GridWidget GRID;
    private boolean SHOULD_CLOSE;
    public PerspectiveZoomConfigScreen(Screen PARENT, boolean REFRESH) {
        super(Text.literal(""));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = PARENT;
        this.REFRESH = REFRESH;
    }
    public void init() {
        try {
            GRID.getMainPositioner().alignHorizontalCenter().margin(0);
            GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
            GRID_ADDER.add(PerspectiveConfigScreenHelper.createTitle(PerspectiveClientData.CLIENT, new PerspectiveZoomConfigScreen(PARENT_SCREEN, false), true, "zoom"));
            GRID_ADDER.add(createZoom());
            GRID_ADDER.add(new EmptyWidget(4, 4));
            GRID_ADDER.add(createFooter());
            GRID.refreshPositions();
            GRID.forEachChild(this::addDrawableChild);
            initTabNavigation();
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to initialize config$zoom screen: {}", (Object)error);
        }
    }

    public void tick() {
        try {
            if (this.REFRESH) {
                PerspectiveClientData.CLIENT.setScreen(new PerspectiveZoomConfigScreen(PARENT_SCREEN, false));
            }
            if (this.SHOULD_CLOSE) {
                PerspectiveClientData.CLIENT.setScreen(PARENT_SCREEN);
            }
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to tick config$zoom screen: {}", (Object)error);
        }
    }
    private GridWidget createZoom() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        double ZOOM_LEVEL = (double) (int) PerspectiveConfigHelper.getConfig("zoom_level") / 100;
        GRID_ADDER.add(new SliderWidget(GRID_ADDER.getGridWidget().getX(), GRID_ADDER.getGridWidget().getY(), 150, 20, PerspectiveTranslation.getConfigTranslation("zoom.level", new Object[]{Text.literal((int)PerspectiveConfigHelper.getConfig("zoom_level") + "%")}, false), ZOOM_LEVEL) {
            @Override
            protected void updateMessage() {
                setMessage(PerspectiveTranslation.getConfigTranslation("zoom.level", new Object[]{Text.literal((int)PerspectiveConfigHelper.getConfig("zoom_level") + "%")}, false));
            }
            @Override
            protected void applyValue() {
                PerspectiveConfigHelper.setConfig("zoom_level", (int) ((value) * 100));
            }
        }, 1);
        double ZOOM_INCREMENT_SIZE = (double) ((int)PerspectiveConfigHelper.getConfig("zoom_increment_size") - 1) / 9;
        GRID_ADDER.add(new SliderWidget(GRID_ADDER.getGridWidget().getX(), GRID_ADDER.getGridWidget().getY(), 150, 20, PerspectiveTranslation.getConfigTranslation("zoom.increment_size", new Object[]{Text.literal(String.valueOf((int)PerspectiveConfigHelper.getConfig("zoom_increment_size")))}, false), ZOOM_INCREMENT_SIZE) {
            @Override
            protected void updateMessage() {
                setMessage(PerspectiveTranslation.getConfigTranslation("zoom.increment_size", new Object[]{Text.literal(String.valueOf((int) PerspectiveConfigHelper.getConfig("zoom_increment_size")))}, false));
            }
            @Override
            protected void applyValue() {
                PerspectiveConfigHelper.setConfig("zoom_increment_size", (int) ((value) * 9) + 1);
            }
        }, 1).setTooltip(Tooltip.of(PerspectiveTranslation.getConfigTranslation("zoom.increment_size", true)));
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("zoom.mode", new Object[]{PerspectiveTranslation.getZoomModeTranslation((String)PerspectiveConfigHelper.getConfig("zoom_mode"))}), (button) -> {
            PerspectiveZoom.cycleZoomModes();
            this.REFRESH = true;
        }).build(), 1);
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("zoom.hide_hud", new Object[]{PerspectiveTranslation.getVariableTranslation((boolean)PerspectiveConfigHelper.getConfig("zoom_hide_hud"), PerspectiveTranslationType.ONFF)}), (button) -> {
            PerspectiveConfigHelper.setConfig("zoom_hide_hud", !(boolean)PerspectiveConfigHelper.getConfig("zoom_hide_hud"));
            this.REFRESH = true;
        }).build());
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("zoom.overlay_message", new Object[]{PerspectiveTranslation.getVariableTranslation((boolean)PerspectiveConfigHelper.getConfig("zoom_overlay_message"), PerspectiveTranslationType.ONFF)}), (button) -> {
            PerspectiveConfigHelper.setConfig("zoom_overlay_message", !(boolean)PerspectiveConfigHelper.getConfig("zoom_overlay_message"));
            this.REFRESH = true;
        }).width(304).build(), 2);
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
        try {
            SimplePositioningWidget.setPos(GRID, getNavigationFocus());
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to initialize config$zoom screen TabNavigation: {}", (Object)error);
        }
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
}