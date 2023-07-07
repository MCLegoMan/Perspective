/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.screen;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.client.util.PerspectiveTranslationUtils;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class PerspectiveConfigScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private final GridWidget GRID;
    public PerspectiveConfigScreen(Screen PARENT) {
        super(Text.translatable("gui.perspective.config.title"));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = PARENT;
    }
    protected void init() {
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(createTitle());
        GRID_ADDER.add(createZoom());
        GRID_ADDER.add(createTexturedEntity());
        GRID_ADDER.add(createAprilFools());
        GRID_ADDER.add(createFooter());
        GRID.refreshPositions();
        GRID.forEachChild(this::addDrawableChild);
        initTabNavigation();
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
                client.setScreen(new PerspectiveConfigScreen(PARENT_SCREEN));
            }).build()).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.show_development_warning.hover"), Text.translatable("gui.perspective.config.show_development_warning.hover")));
            DEVELOPMENT_BUTTONS_ADDER.add(ButtonWidget.builder(Text.literal("?"), (button) -> {
                client.setScreen(new PerspectiveDevelopmentWarningScreen(client.currentScreen, 200, false));
            }).width(20).build());
            GRID_ADDER.add(DEVELOPMENT_BUTTONS);
        } else {
            GRID_ADDER.add(new IconWidget(224, 42, new Identifier(PerspectiveData.ID, "textures/config/logo/release.png")), 2);
        }
        return GRID;
    }
    private GridWidget createZoom() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        double ZOOM_LEVEL_VALUE = 1 - ((double) PerspectiveConfig.ZOOM_LEVEL / 100);
        GRID_ADDER.add(new SliderWidget(GRID_ADDER.getGridWidget().getX(), GRID_ADDER.getGridWidget().getY(), 150, 20, Text.translatable("gui.perspective.config.zoom_level", Text.literal((int) (100 - PerspectiveConfig.ZOOM_LEVEL) + "%")), ZOOM_LEVEL_VALUE) {
            @Override
            protected void updateMessage() {
                setMessage(Text.translatable("gui.perspective.config.zoom_level", Text.literal((int) (100 - PerspectiveConfig.ZOOM_LEVEL) + "%")));
            }

            @Override
            protected void applyValue() {
                PerspectiveConfig.ZOOM_LEVEL = (int) ((1 - value) * 100);
            }
        }, 1).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.zoom_level.hover"), Text.translatable("gui.perspective.config.zoom_level.hover")));
        double OVERLAY_DELAY_VALUE = (double) PerspectiveConfig.OVERLAY_DELAY / 1000;
        GRID_ADDER.add(new SliderWidget(GRID_ADDER.getGridWidget().getX(), GRID_ADDER.getGridWidget().getY(), 150, 20, Text.translatable("gui.perspective.config.overlay_delay", Text.literal(String.valueOf((int)PerspectiveConfig.OVERLAY_DELAY / 20))), OVERLAY_DELAY_VALUE) {
            @Override
            protected void updateMessage() {
                setMessage(Text.translatable("gui.perspective.config.overlay_delay", Text.literal(String.valueOf((int)PerspectiveConfig.OVERLAY_DELAY / 20))));
            }

            @Override
            protected void applyValue() {
                PerspectiveConfig.OVERLAY_DELAY = (int) ((value) * 1000);
            }
        }, 1).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.overlay_delay.hover"), Text.translatable("gui.perspective.config.overlay_delay.hover")));
        return GRID;
    }
    private GridWidget createTexturedEntity() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);

        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.textured_named_entity", PerspectiveTranslationUtils.booleanTranslate(PerspectiveConfig.TEXTURED_NAMED_ENTITY)), (button) -> {
            PerspectiveConfig.TEXTURED_NAMED_ENTITY = !PerspectiveConfig.TEXTURED_NAMED_ENTITY;
            client.setScreen(new PerspectiveConfigScreen(PARENT_SCREEN));
        }).build(), 1).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.textured_named_entity.hover"), Text.translatable("gui.perspective.config.textured_named_entity.hover")));
       GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.textured_random_entity", PerspectiveTranslationUtils.booleanTranslate(PerspectiveConfig.TEXTURED_RANDOM_ENTITY)), (button) -> {
            PerspectiveConfig.TEXTURED_RANDOM_ENTITY = !PerspectiveConfig.TEXTURED_RANDOM_ENTITY;
            client.setScreen(new PerspectiveConfigScreen(PARENT_SCREEN));
        }).build(), 1).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.textured_random_entity.hover"), Text.translatable("gui.perspective.config.textured_random_entity.hover")));
        return GRID;
    }
    private GridWidget createAprilFools() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.allow_april_fools", PerspectiveTranslationUtils.booleanTranslate(PerspectiveConfig.ALLOW_APRIL_FOOLS)), (button) -> {
            PerspectiveConfig.ALLOW_APRIL_FOOLS = !PerspectiveConfig.ALLOW_APRIL_FOOLS;
            client.setScreen(new PerspectiveConfigScreen(PARENT_SCREEN));
        }).build(), 1).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.allow_april_fools.hover"), Text.translatable("gui.perspective.config.allow_april_fools.hover")));
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.force_april_fools", PerspectiveTranslationUtils.booleanTranslate(PerspectiveConfig.FORCE_APRIL_FOOLS)), (button) -> {
            PerspectiveConfig.FORCE_APRIL_FOOLS = !PerspectiveConfig.FORCE_APRIL_FOOLS;
            client.setScreen(new PerspectiveConfigScreen(PARENT_SCREEN));
        }).build(), 1).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.force_april_fools.hover"), Text.translatable("gui.perspective.config.force_april_fools.hover")));
        return GRID;
    }
    private GridWidget createFooter() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.contribute"), ConfirmLinkScreen.opening("https://github.com/MCLegoMan/Perspective", this, true)).build()).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.contribute.hover"), Text.translatable("gui.perspective.config.contribute.hover")));
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.bug_report"), ConfirmLinkScreen.opening("https://github.com/MCLegoMan/Perspective/issues", this, true)).build()).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.bug_report.hover"), Text.translatable("gui.perspective.config.bug_report.hover")));
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.reset"), (button) -> {
            PerspectiveConfig.reset();
            client.setScreen(new PerspectiveConfigScreen(PARENT_SCREEN));
        }).build()).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.reset.hover"), Text.translatable("gui.perspective.config.reset.hover")));
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.back"), (button) -> {
            PerspectiveConfig.write_to_file();
            client.setScreen(PARENT_SCREEN);
        }).build()).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.back.hover"), Text.translatable("gui.perspective.config.back.hover")));
        return GRID;
    }

    protected void initTabNavigation() {
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
        if (keyCode == 256) {
            PerspectiveConfig.write_to_file();
            client.setScreen(PARENT_SCREEN);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }
}