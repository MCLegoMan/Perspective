/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.screen;

import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.MultilineTextWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class PerspectiveDevelopmentWarningScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private final GridWidget GRID;
    private int TIMER_TICKS;
    private int TIMER_SECS;
    public PerspectiveDevelopmentWarningScreen(Screen PARENT, int timerTicks) {
        super(Text.translatable("gui.perspective.config.title"));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = PARENT;
        TIMER_TICKS = timerTicks;
        TIMER_SECS = ticksToSeconds(TIMER_TICKS);
    }

    @Override
    public void tick() {
        if (TIMER_SECS != ticksToSeconds(TIMER_TICKS)) {
            client.setScreen(new PerspectiveDevelopmentWarningScreen(PARENT_SCREEN, TIMER_TICKS));
        }
        if (TIMER_TICKS > 0) {
            TIMER_TICKS -= 1;
        }
        else client.setScreen(PARENT_SCREEN);
        super.tick();
    }
    private int ticksToSeconds(int ticks) {
        return ticks / 20;
    }
    private Text getTimerText(int seconds) {
        Text state;
        if (seconds != 1) state = Text.translatable("gui.perspective.development_warning.timer.seconds");
        else state = Text.translatable("gui.perspective.development_warning.timer.second");
        return Text.translatable("gui.perspective.development_warning.timer.message", seconds, state).formatted(Formatting.GOLD);
    }
    protected void init() {
        GRID.getMainPositioner().alignHorizontalCenter().margin(4);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(new MultilineTextWidget(Text.translatable("gui.perspective.development_warning.title", PerspectiveData.NAME).formatted(Formatting.WHITE), textRenderer), 1);
        GRID_ADDER.add(new MultilineTextWidget(Text.translatable("gui.perspective.development_warning.description").formatted(Formatting.RED).formatted(Formatting.BOLD), textRenderer).setCentered(true), 1);
        GRID_ADDER.add(new MultilineTextWidget(getTimerText(ticksToSeconds(TIMER_TICKS) + 1), textRenderer), 1);
        GRID.refreshPositions();
        GRID.forEachChild(this::addDrawableChild);
        initTabNavigation();
    }
    public boolean shouldCloseOnEsc() {
        return true;
    }

    protected void initTabNavigation() {
        SimplePositioningWidget.setPos(GRID, this.getNavigationFocus());
    }

    public Text getNarratedTitle() {
        return ScreenTexts.joinSentences(Text.translatable("gui.perspective.development_warning.description"));
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }
}