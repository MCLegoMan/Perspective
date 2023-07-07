/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.screen;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.input.KeyCodes;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class PerspectiveDevelopmentWarningScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private final GridWidget GRID;
    private int TIMER_TICKS;
    private int TIMER_SECS;
    private final boolean SHOW_CHECKBOX;
    @Nullable
    protected CheckboxWidget checkbox;
    public PerspectiveDevelopmentWarningScreen(Screen parent_screen, int timer_ticks, boolean show_checkbox) {
        super(Text.translatable("gui.perspective.config.title"));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = parent_screen;
        TIMER_TICKS = timer_ticks;
        TIMER_SECS = ticksToSeconds(timer_ticks);
        SHOW_CHECKBOX = show_checkbox;
    }

    @Override
    public void tick() {
        if (SHOW_CHECKBOX) {
            if (this.checkbox.isChecked() == PerspectiveConfig.SHOW_DEVELOPMENT_WARNING) {
                PerspectiveConfig.SHOW_DEVELOPMENT_WARNING = !this.checkbox.isChecked();
                PerspectiveConfig.write_to_file();
            }
        }
        if (TIMER_TICKS > 0) {
            TIMER_TICKS -= 1;
            client.setScreen(new PerspectiveDevelopmentWarningScreen(PARENT_SCREEN, TIMER_TICKS, SHOW_CHECKBOX));
        }
        else client.setScreen(PARENT_SCREEN);
        super.tick();
    }
    private int ticksToSeconds(int ticks) {
        return (ticks - 1) / 20;
    }
    private Text getTimerText(int seconds) {
        Text state;
        if (seconds != 1) state = Text.translatable("gui.perspective.development_warning.timer.seconds");
        else state = Text.translatable("gui.perspective.development_warning.timer.second");
        return Text.translatable("gui.perspective.development_warning.timer.message", seconds, state).formatted(Formatting.GOLD);
    }
    protected void init() {
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(createTitle());
        GRID_ADDER.add(createWarning());
        if (SHOW_CHECKBOX)  GRID_ADDER.add(createCheckbox());
        GRID.refreshPositions();
        GRID.forEachChild(this::addDrawableChild);
        initTabNavigation();
    }

    private GridWidget createTitle() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(new IconWidget(256, 48, new Identifier(PerspectiveData.ID, "textures/config/logo/development.png")));
        return GRID;
    }

    private GridWidget createWarning() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(new MultilineTextWidget(Text.translatable("gui.perspective.development_warning.description").formatted(Formatting.RED).formatted(Formatting.BOLD), textRenderer).setCentered(true));
        GRID_ADDER.add(new MultilineTextWidget(getTimerText(ticksToSeconds(TIMER_TICKS) + 1), textRenderer));
        return GRID;
    }

    private GridWidget createCheckbox() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        Text checkMessage = Text.translatable("gui.perspective.development_warning.checkbox");
        int j = this.textRenderer.getWidth(checkMessage);
        this.checkbox = new CheckboxWidget(this.width / 2 - j / 2 - 8, GRID.getY(), j + 24, 20, checkMessage, !PerspectiveConfig.SHOW_DEVELOPMENT_WARNING);
        GRID_ADDER.add(this.checkbox, 2);
        return GRID;
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        client.setScreen(PARENT_SCREEN);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    protected void initTabNavigation() {
        SimplePositioningWidget.setPos(GRID, this.getNavigationFocus());
    }

    public Text getNarratedTitle() {
        return ScreenTexts.joinSentences();
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }
}