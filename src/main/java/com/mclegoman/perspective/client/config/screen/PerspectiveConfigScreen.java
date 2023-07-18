/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.config.screen;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.config.screen.aprilfoolsprank.PerspectiveAprilFoolsPrankScreen;
import com.mclegoman.perspective.client.config.screen.experimental.PerspectiveExperimentalFeaturesScreen;
import com.mclegoman.perspective.client.config.screen.information.PerspectiveInformationScreen;
import com.mclegoman.perspective.client.config.screen.supersecretsettings.PerspectiveSuperSecretSettingsScreen;
import com.mclegoman.perspective.client.config.screen.texturedentity.PerspectiveTexturedEntityScreen;
import com.mclegoman.perspective.client.util.PerspectiveTranslationUtils;
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

@Environment(EnvType.CLIENT)
public class PerspectiveConfigScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private final GridWidget GRID;
    private boolean SHOULD_CLOSE;
    public PerspectiveConfigScreen(Screen PARENT) {
        super(Text.translatable("gui.perspective.config.title"));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = PARENT;
    }
    public void init() {
        GRID.getMainPositioner().alignHorizontalCenter().margin(0);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(PerspectiveConfigScreenHelper.createTitle(client, new PerspectiveConfigScreen(PARENT_SCREEN)));
        GRID_ADDER.add(createZoom());
        GRID_ADDER.add(createSuperSecretSettingsAndShowHUD());
        GRID_ADDER.add(createTexturedEntityAndAprilFools());
        GRID_ADDER.add(createFooter());
        GRID.refreshPositions();
        GRID.forEachChild(this::addDrawableChild);
        initTabNavigation();
    }

    public void tick() {
        if (this.SHOULD_CLOSE) {
            PerspectiveConfigHelper.saveConfig(false);
            client.setScreen(PARENT_SCREEN);
        }
    }
    private GridWidget createZoom() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        double ZOOM_LEVEL_VALUE = (double) (int) PerspectiveConfigHelper.getConfig("zoom_level") / 100;
        GRID_ADDER.add(new SliderWidget(GRID_ADDER.getGridWidget().getX(), GRID_ADDER.getGridWidget().getY(), 150, 20, Text.translatable("gui.perspective.config.zoom_level", Text.literal((int) ((int)PerspectiveConfigHelper.getConfig("zoom_level")) + "%")), ZOOM_LEVEL_VALUE) {
            @Override
            protected void updateMessage() {
                setMessage(Text.translatable("gui.perspective.config.zoom_level", Text.literal((int)PerspectiveConfigHelper.getConfig("zoom_level") + "%")));
            }

            @Override
            protected void applyValue() {
                PerspectiveConfigHelper.setConfig("zoom_level", (int) ((value) * 100));
            }
        }, 1).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.zoom_level.hover"), Text.translatable("gui.perspective.config.zoom_level.hover")));
        double OVERLAY_VALUE = (double) (int) PerspectiveConfigHelper.getConfig("overlay_delay") / 10;
        GRID_ADDER.add(new SliderWidget(GRID_ADDER.getGridWidget().getX(), GRID_ADDER.getGridWidget().getY(), 150, 20, Text.translatable("gui.perspective.config.overlay_delay", Text.literal(String.valueOf(PerspectiveConfigHelper.getConfig("overlay_delay")))), OVERLAY_VALUE) {
            @Override
            protected void updateMessage() {
                setMessage(Text.translatable("gui.perspective.config.overlay_delay", Text.literal(String.valueOf(PerspectiveConfigHelper.getConfig("overlay_delay")))));
            }

            @Override
            protected void applyValue() {
                PerspectiveConfigHelper.setConfig("overlay_delay", (int) ((value) * 10));
            }
        }, 1).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.overlay_delay.hover"), Text.translatable("gui.perspective.config.overlay_delay.hover")));
        return GRID;
    }
    private GridWidget createSuperSecretSettingsAndShowHUD() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.super_secret_settings"), (button) -> {
            client.setScreen(new PerspectiveSuperSecretSettingsScreen(client.currentScreen));
        }).build()).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.super_secret_settings.hover"), Text.translatable("gui.perspective.config.super_secret_settings.hover")));
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.hide_hud", PerspectiveTranslationUtils.onOffTranslate((boolean)PerspectiveConfigHelper.getConfig("hide_hud"))), (button) -> {
            PerspectiveConfigHelper.setConfig("hide_hud", !(boolean)PerspectiveConfigHelper.getConfig("hide_hud"));
            client.setScreen(new PerspectiveConfigScreen(PARENT_SCREEN));
        }).build()).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.hide_hud.hover"), Text.translatable("gui.perspective.config.hide_hud.hover")));
        return GRID;
    }
    private GridWidget createTexturedEntityAndAprilFools() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.textured_entity"), (button) -> {
            client.setScreen(new PerspectiveTexturedEntityScreen(client.currentScreen));
        }).build(), 1).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.textured_entity.hover"), Text.translatable("gui.perspective.config.textured_entity.hover")));
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.april_fools_prank"), (button) -> {
            client.setScreen(new PerspectiveAprilFoolsPrankScreen(client.currentScreen));
        }).build(), 1).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.april_fools_prank.hover"), Text.translatable("gui.perspective.config.april_fools_prank.hover")));
        return GRID;
    }
    private GridWidget createFooter() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.info"), (button) -> {
            client.setScreen(new PerspectiveInformationScreen(client.currentScreen));
        }).build()).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.info.hover"), Text.translatable("gui.perspective.config.info.hover")));
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.experimental"), (button) -> {
            client.setScreen(new PerspectiveExperimentalFeaturesScreen(client.currentScreen));
        }).build()).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.experimental.hover"), Text.translatable("gui.perspective.config.experimental.hover")));
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.reset"), (button) -> {
            PerspectiveConfigHelper.resetConfig();
            client.setScreen(new PerspectiveConfigScreen(PARENT_SCREEN));
        }).build()).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.reset.hover"), Text.translatable("gui.perspective.config.reset.hover")));
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.back"), (button) -> {
            this.SHOULD_CLOSE = true;
        }).build()).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.back.hover"), Text.translatable("gui.perspective.config.back.hover")));
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
        if (keyCode == 256) this.SHOULD_CLOSE = true;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }
}