/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.screen.config.experimental;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.screen.config.PerspectiveConfigScreenHelper;
import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.client.translation.PerspectiveTranslationType;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class PerspectiveExperimentalConfigScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private boolean REFRESH;
    private final GridWidget GRID;
    private boolean SHOULD_CLOSE;
    public PerspectiveExperimentalConfigScreen(Screen PARENT, boolean REFRESH) {
        super(Text.literal(""));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = PARENT;
        this.REFRESH = REFRESH;
    }
    public void init() {
        try {
            GRID.getMainPositioner().alignHorizontalCenter().margin(0);
            GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
            GRID_ADDER.add(PerspectiveConfigScreenHelper.createTitle(client, new PerspectiveExperimentalConfigScreen(PARENT_SCREEN, true), true, "experimental"));
            GRID_ADDER.add(createExperiments());
            GRID_ADDER.add(new EmptyWidget(4, 4));
            GRID_ADDER.add(createFooter());
            GRID.refreshPositions();
            GRID.forEachChild(this::addDrawableChild);
            initTabNavigation();
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to initialize config>experimental screen: {}", (Object)error);
        }
    }

    public void tick() {
        try {
            if (this.REFRESH) {
                PerspectiveClientData.CLIENT.setScreen(new PerspectiveExperimentalConfigScreen(PARENT_SCREEN, false));
            }
            if (this.SHOULD_CLOSE) {
                PerspectiveClientData.CLIENT.setScreen(PARENT_SCREEN);
            }
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to tick perspective$config$experimental screen: {}", (Object)error);
        }
    }
    private GridWidget createEmpty() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(new MultilineTextWidget(PerspectiveTranslation.getConfigTranslation("experimental.none", new Formatting[]{Formatting.RED, Formatting.BOLD}), PerspectiveClientData.CLIENT.textRenderer).setCentered(true));
        return GRID;
    }
    private GridWidget createExperiments() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(new MultilineTextWidget(PerspectiveTranslation.getConfigTranslation("experimental.warning", new Formatting[]{Formatting.RED, Formatting.BOLD}), PerspectiveClientData.CLIENT.textRenderer).setCentered(true));
        GRID_ADDER.add(new EmptyWidget(4, 4));
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("experimental.hide_armor", new Object[]{PerspectiveTranslation.getVariableTranslation((boolean)PerspectiveConfigHelper.getConfig("hide_armor"), PerspectiveTranslationType.ONFF)}), (button) -> {
            PerspectiveConfigHelper.setConfig("hide_armor", !(boolean)PerspectiveConfigHelper.getConfig("hide_armor"));
            this.REFRESH = true;
        }).width(304).build()).setTooltip(Tooltip.of(PerspectiveTranslation.getConfigTranslation("experimental.hide_armor", new Object[]{PerspectiveTranslation.getVariableTranslation((boolean)PerspectiveConfigHelper.getConfig("hide_armor"), PerspectiveTranslationType.ONFF)}, true)));
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("experimental.hide_nametags", new Object[]{PerspectiveTranslation.getVariableTranslation((boolean)PerspectiveConfigHelper.getConfig("hide_nametags"), PerspectiveTranslationType.ONFF)}), (button) -> {
            PerspectiveConfigHelper.setConfig("hide_nametags", !(boolean)PerspectiveConfigHelper.getConfig("hide_nametags"));
            this.REFRESH = true;
        }).width(304).build()).setTooltip(Tooltip.of(PerspectiveTranslation.getConfigTranslation("experimental.hide_nametags", new Object[]{PerspectiveTranslation.getVariableTranslation((boolean)PerspectiveConfigHelper.getConfig("hide_nametags"), PerspectiveTranslationType.ONFF)}, true)));
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("experimental.smooth_zoom", new Object[]{PerspectiveTranslation.getVariableTranslation((boolean)PerspectiveConfigHelper.getConfig("smooth_zoom"), PerspectiveTranslationType.ONFF)}), (button) -> {
            PerspectiveConfigHelper.setConfig("smooth_zoom", !(boolean)PerspectiveConfigHelper.getConfig("smooth_zoom"));
            this.REFRESH = true;
        }).width(304).build()).setTooltip(Tooltip.of(PerspectiveTranslation.getConfigTranslation("experimental.smooth_zoom", new Object[]{PerspectiveTranslation.getVariableTranslation((boolean)PerspectiveConfigHelper.getConfig("smooth_zoom"), PerspectiveTranslationType.ONFF)}, true)));
        return GRID;
    }
    private GridWidget createFooter() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("reset"), (button) -> {
            PerspectiveConfigHelper.resetConfig();
            REFRESH = true;
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