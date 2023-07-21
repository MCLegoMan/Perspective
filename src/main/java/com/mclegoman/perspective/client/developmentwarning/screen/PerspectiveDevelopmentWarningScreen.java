/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.developmentwarning.screen;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class PerspectiveDevelopmentWarningScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private final GridWidget GRID;
    private int TIMER_TICKS;
    private final boolean SHOW_CHECKBOX;
    @Nullable
    protected CheckboxWidget checkbox;
    private boolean SHOULD_CLOSE;
    private boolean SHOULD_SAVE;
    public PerspectiveDevelopmentWarningScreen(Screen parent_screen, int timer_ticks, boolean show_checkbox) {
        super(Text.literal(""));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = parent_screen;
        TIMER_TICKS = timer_ticks;
        SHOW_CHECKBOX = show_checkbox;
    }

    @Override
    public void tick() {
        if (SHOW_CHECKBOX) {
            if (this.checkbox.isChecked() == (boolean)PerspectiveConfigHelper.getConfig("show_development_warning")) {
                PerspectiveConfigHelper.setConfig("show_development_warning", !this.checkbox.isChecked());
                SHOULD_SAVE = true;
            }
        }
        if (TIMER_TICKS > 0) {
            TIMER_TICKS -= 1;
            PerspectiveClientData.CLIENT.setScreen(new PerspectiveDevelopmentWarningScreen(PARENT_SCREEN, TIMER_TICKS, SHOW_CHECKBOX));
        }
        else this.SHOULD_CLOSE = true;
        if (this.SHOULD_CLOSE) {
            if (SHOULD_SAVE) {
                PerspectiveConfigHelper.saveConfig(false);
                SHOULD_SAVE = false;
            }
            PerspectiveClientData.CLIENT.setScreen(PARENT_SCREEN);
        }
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
        if (SHOW_CHECKBOX)  GRID_ADDER.add(createCheckbox());
        GRID.refreshPositions();
        GRID.forEachChild(this::addDrawableChild);
        initTabNavigation();
    }

    private GridWidget createTitle() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(new IconWidget(256, 48, PerspectiveClientData.LOGO));
        return GRID;
    }

    private GridWidget createWarning() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(new MultilineTextWidget(PerspectiveTranslation.getTranslation("development_warning", new Formatting[]{Formatting.RED, Formatting.BOLD}), textRenderer).setCentered(true));
        GRID_ADDER.add(new MultilineTextWidget(PerspectiveTranslation.getTranslation("development_warning.timer", new Object[]{ticksToSeconds(TIMER_TICKS) + 1, PerspectiveTranslation.getPlural(ticksToSeconds(TIMER_TICKS) + 1, "time.seconds")}, new Formatting[]{Formatting.GOLD}), textRenderer));
        return GRID;
    }

    private GridWidget createCheckbox() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        Text checkMessage = Text.translatable("gui.perspective.development_warning.checkbox");
        int j = this.textRenderer.getWidth(checkMessage);
        this.checkbox = new CheckboxWidget(this.width / 2 - j / 2 - 8, GRID.getY(), j + 24, 20, checkMessage, !(boolean)PerspectiveConfigHelper.getConfig("show_development_warning"));
        GRID_ADDER.add(this.checkbox, 2);
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