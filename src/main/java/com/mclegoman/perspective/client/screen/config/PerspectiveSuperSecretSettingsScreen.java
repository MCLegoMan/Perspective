/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.screen.config;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.dataloader.PerspectiveSuperSecretSettingsDataLoader;
import com.mclegoman.perspective.client.screen.PerspectiveDevelopmentWarningScreen;
import com.mclegoman.perspective.client.util.PerspectiveConfigScreenUtils;
import com.mclegoman.perspective.client.util.PerspectiveSuperSecretSettingsUtil;
import com.mclegoman.perspective.client.util.PerspectiveTranslationUtils;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class PerspectiveSuperSecretSettingsScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private final GridWidget GRID;
    private boolean SHOULD_CLOSE;
    public PerspectiveSuperSecretSettingsScreen(Screen PARENT) {
        super(Text.translatable("gui.perspective.config.title"));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = PARENT;
    }
    public void init() {
        GRID.getMainPositioner().alignHorizontalCenter().margin(0);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(PerspectiveConfigScreenUtils.createTitle(client, new PerspectiveSuperSecretSettingsScreen(PARENT_SCREEN)));
        GRID_ADDER.add(createSuperSecretSettings());
        GRID_ADDER.add(createFooter());
        GRID.refreshPositions();
        GRID.forEachChild(this::addDrawableChild);
        initTabNavigation();
    }

    public void tick() {
        if (this.SHOULD_CLOSE) {
            client.setScreen(PARENT_SCREEN);
        }
    }
    private GridWidget createSuperSecretSettings() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);

        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.super_secret_settings_cycle", Text.literal(PerspectiveSuperSecretSettingsDataLoader.SHADERS_NAME.get((int)PerspectiveConfigHelper.getConfig("super_secret_settings"))).formatted(PerspectiveSuperSecretSettingsUtil.getRandomColor())), (button) -> {
            PerspectiveSuperSecretSettingsUtil.cycle(client, true, true);
            client.setScreen(new PerspectiveSuperSecretSettingsScreen(PARENT_SCREEN));
        }).width(304).build(), 2).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.super_secret_settings_cycle.hover"), Text.translatable("gui.perspective.config.super_secret_settings_cycle.hover")));


        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.super_secret_settings_mode", PerspectiveTranslationUtils.superSecretSettingsModeTranslate((boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_mode"))), (button) -> {
            PerspectiveConfigHelper.setConfig("super_secret_settings_mode", !(boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_mode"));
            client.setScreen(new PerspectiveSuperSecretSettingsScreen(PARENT_SCREEN));
        }).build()).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.super_secret_settings_mode.hover"), Text.translatable("gui.perspective.config.super_secret_settings_mode.hover")));


        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.super_secret_settings_enabled", PerspectiveTranslationUtils.enabledDisabledTranslate((boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_enabled"))), (button) -> {
            PerspectiveConfigHelper.setConfig("super_secret_settings_enabled", !(boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_enabled"));
            client.setScreen(new PerspectiveSuperSecretSettingsScreen(PARENT_SCREEN));
        }).build()).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.super_secret_settings_enabled.hover"), Text.translatable("gui.perspective.config.super_secret_settings_enabled.hover")));
        return GRID;
    }
    private GridWidget createFooter() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.reset"), (button) -> {
            PerspectiveConfigHelper.resetConfig();
            client.setScreen(new PerspectiveSuperSecretSettingsScreen(PARENT_SCREEN));
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