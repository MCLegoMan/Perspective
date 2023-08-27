/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.screen.downgradewarning;

import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.IconWidget;
import net.minecraft.client.gui.widget.MultilineTextWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class PerspectiveDowngradeWarningScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private final GridWidget GRID;
    private int TIMER_TICKS;
    private boolean SHOULD_CLOSE;
    public PerspectiveDowngradeWarningScreen(Screen parent_screen, int timer_ticks) {
        super(Text.literal(""));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = parent_screen;
        TIMER_TICKS = timer_ticks;
    }

    @Override
    public void tick() {
        if (TIMER_TICKS > 0) {
            TIMER_TICKS -= 1;
            PerspectiveClientData.CLIENT.setScreen(new PerspectiveDowngradeWarningScreen(PARENT_SCREEN, TIMER_TICKS));
        }
        else this.SHOULD_CLOSE = true;
        if (this.SHOULD_CLOSE) PerspectiveClientData.CLIENT.setScreen(PARENT_SCREEN);
        super.tick();
    }
    private int ticksToSeconds(int ticks) {
        return (ticks - 1) / 20;
    }
    protected void init() {
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(createTitle());
        GRID_ADDER.add(createWarning());
        GRID.refreshPositions();
        GRID.forEachChild(this::addDrawableChild);
        initTabNavigation();
    }

    private GridWidget createTitle() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(new IconWidget(256, 48, PerspectiveClientData.getLogo()));
        return GRID;
    }
    private GridWidget createWarning() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(new MultilineTextWidget(PerspectiveTranslation.getTranslation("downgrade_warning", new Formatting[]{Formatting.RED, Formatting.BOLD}), textRenderer).setCentered(true));
        GRID_ADDER.add(new MultilineTextWidget(PerspectiveTranslation.getTranslation("warning.timer", new Object[]{ticksToSeconds(TIMER_TICKS) + 1, PerspectiveTranslation.getPlural(ticksToSeconds(TIMER_TICKS) + 1, "time.seconds")}, new Formatting[]{Formatting.GOLD}), textRenderer));
        return GRID;
    }
    public boolean shouldCloseOnEsc() {
        return false;
    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.SHOULD_CLOSE = true;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    protected void initTabNavigation() {
        SimplePositioningWidget.setPos(GRID, this.getNavigationFocus());
    }
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }
}