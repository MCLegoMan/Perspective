/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.config.screen.april_fools_prank;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.config.screen.PerspectiveConfigScreenHelper;
import com.mclegoman.perspective.client.util.PerspectiveTranslationUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class PerspectiveAprilFoolsPrankScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private final GridWidget GRID;
    private boolean SHOULD_CLOSE;
    public PerspectiveAprilFoolsPrankScreen(Screen PARENT) {
        super(Text.translatable("gui.perspective.config.title"));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = PARENT;
    }
    public void init() {
        GRID.getMainPositioner().alignHorizontalCenter().margin(0);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(PerspectiveConfigScreenHelper.createTitle(client, new PerspectiveAprilFoolsPrankScreen(PARENT_SCREEN)));
        GRID_ADDER.add(createAprilFools());
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
    private GridWidget createAprilFools() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.allow_april_fools", PerspectiveTranslationUtils.onOffTranslate((boolean)PerspectiveConfigHelper.getConfig("allow_april_fools"))), (button) -> {
            PerspectiveConfigHelper.setConfig("allow_april_fools", !(boolean)PerspectiveConfigHelper.getConfig("allow_april_fools"));
            client.setScreen(new PerspectiveAprilFoolsPrankScreen(PARENT_SCREEN));
        }).build(), 1).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.allow_april_fools.hover"), Text.translatable("gui.perspective.config.allow_april_fools.hover")));
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.force_april_fools", PerspectiveTranslationUtils.onOffTranslate((boolean)PerspectiveConfigHelper.getConfig("force_april_fools"))), (button) -> {
            PerspectiveConfigHelper.setConfig("force_april_fools", !(boolean)PerspectiveConfigHelper.getConfig("force_april_fools"));
            client.setScreen(new PerspectiveAprilFoolsPrankScreen(PARENT_SCREEN));
        }).build(), 1).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.force_april_fools.hover"), Text.translatable("gui.perspective.config.force_april_fools.hover")));
        return GRID;
    }
    private GridWidget createFooter() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.reset"), (button) -> {
            PerspectiveConfigHelper.resetConfig();
            client.setScreen(new PerspectiveAprilFoolsPrankScreen(PARENT_SCREEN));
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