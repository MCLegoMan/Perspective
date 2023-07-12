/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.screen.config;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.client.screen.PerspectiveDevelopmentWarningScreen;
import com.mclegoman.perspective.client.util.PerspectiveTranslationUtils;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class PerspectiveExperimentalFeaturesScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private final GridWidget GRID;
    private boolean SHOULD_CLOSE;
    public PerspectiveExperimentalFeaturesScreen(Screen PARENT) {
        super(Text.translatable("gui.perspective.config.title"));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = PARENT;
    }
    public void init() {
        GRID.getMainPositioner().alignHorizontalCenter().margin(0);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(createTitle());
        GRID_ADDER.add(createTexturedEntity());
        GRID_ADDER.add(createAprilFools());
        GRID_ADDER.add(createPerspective());
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

    private GridWidget createTitle() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        if (PerspectiveData.IS_DEVELOPMENT) {
            GRID_ADDER.add(new IconWidget(224, 42, new Identifier(PerspectiveData.ID, "textures/config/logo/development.png")));
            GridWidget DEVELOPMENT_BUTTONS = new GridWidget();
            DEVELOPMENT_BUTTONS.getMainPositioner().alignHorizontalCenter().margin(2);
            GridWidget.Adder DEVELOPMENT_BUTTONS_ADDER = DEVELOPMENT_BUTTONS.createAdder(2);
            DEVELOPMENT_BUTTONS_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.show_development_warning", PerspectiveTranslationUtils.booleanTranslate(PerspectiveConfig.SHOW_DEVELOPMENT_WARNING)), (button) -> {
                PerspectiveConfig.SHOW_DEVELOPMENT_WARNING = !PerspectiveConfig.SHOW_DEVELOPMENT_WARNING;
                client.setScreen(new PerspectiveExperimentalFeaturesScreen(PARENT_SCREEN));
            }).build()).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.show_development_warning.hover"), Text.translatable("gui.perspective.config.show_development_warning.hover")));
            DEVELOPMENT_BUTTONS_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.test_development_warning"), (button) -> {
                client.setScreen(new PerspectiveDevelopmentWarningScreen(client.currentScreen, 200, false));
            }).width(20).build()).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.test_development_warning.hover"), Text.translatable("gui.perspective.config.test_development_warning.hover")));;
            GRID_ADDER.add(DEVELOPMENT_BUTTONS);
        } else {
            GRID_ADDER.add(new IconWidget(224, 42, new Identifier(PerspectiveData.ID, "textures/config/logo/release.png")), 2);
        }
        return GRID;
    }
    private GridWidget createTexturedEntity() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.textured_named_entity", PerspectiveTranslationUtils.booleanTranslate(PerspectiveConfig.TEXTURED_NAMED_ENTITY)), (button) -> {
            PerspectiveConfig.TEXTURED_NAMED_ENTITY = !PerspectiveConfig.TEXTURED_NAMED_ENTITY;
            client.setScreen(new PerspectiveExperimentalFeaturesScreen(PARENT_SCREEN));
        }).build(), 1).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.textured_named_entity.hover"), Text.translatable("gui.perspective.config.textured_named_entity.hover")));
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.textured_random_entity", PerspectiveTranslationUtils.booleanTranslate(PerspectiveConfig.TEXTURED_RANDOM_ENTITY)), (button) -> {
            PerspectiveConfig.TEXTURED_RANDOM_ENTITY = !PerspectiveConfig.TEXTURED_RANDOM_ENTITY;
            client.setScreen(new PerspectiveExperimentalFeaturesScreen(PARENT_SCREEN));
        }).build(), 1).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.textured_random_entity.hover"), Text.translatable("gui.perspective.config.textured_random_entity.hover")));
        return GRID;
    }
    private GridWidget createAprilFools() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.allow_april_fools", PerspectiveTranslationUtils.booleanTranslate(PerspectiveConfig.ALLOW_APRIL_FOOLS)), (button) -> {
            PerspectiveConfig.ALLOW_APRIL_FOOLS = !PerspectiveConfig.ALLOW_APRIL_FOOLS;
            client.setScreen(new PerspectiveExperimentalFeaturesScreen(PARENT_SCREEN));
        }).build(), 1).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.allow_april_fools.hover"), Text.translatable("gui.perspective.config.allow_april_fools.hover")));
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.force_april_fools", PerspectiveTranslationUtils.booleanTranslate(PerspectiveConfig.FORCE_APRIL_FOOLS)), (button) -> {
            PerspectiveConfig.FORCE_APRIL_FOOLS = !PerspectiveConfig.FORCE_APRIL_FOOLS;
            client.setScreen(new PerspectiveExperimentalFeaturesScreen(PARENT_SCREEN));
            PerspectiveData.LOGGER.info(String.valueOf(button.getWidth()));
        }).build(), 1).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.force_april_fools.hover"), Text.translatable("gui.perspective.config.force_april_fools.hover")));
        return GRID;
    }
    private GridWidget createPerspective() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.perspective_hold", PerspectiveTranslationUtils.perspectiveHoldTranslate(PerspectiveConfig.PERSPECTIVE_HOLD)), (button) -> {
            PerspectiveConfig.PERSPECTIVE_HOLD = !PerspectiveConfig.PERSPECTIVE_HOLD;
            client.setScreen(new PerspectiveExperimentalFeaturesScreen(PARENT_SCREEN));
        }).width(304).build(), 2).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.perspective_hold.hover"), Text.translatable("gui.perspective.config.perspective_hold.hover")));
        return GRID;
    }
    private GridWidget createFooter() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.reset"), (button) -> {
            PerspectiveConfig.reset();
            client.setScreen(new PerspectiveExperimentalFeaturesScreen(PARENT_SCREEN));
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