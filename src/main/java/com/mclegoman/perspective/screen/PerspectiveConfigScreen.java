package com.mclegoman.perspective.screen;

import com.mclegoman.perspective.config.PerspectiveConfig;
import com.mclegoman.perspective.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

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
        GRID.getMainPositioner().alignHorizontalCenter().margin(4);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(new TextWidget(Text.translatable("gui.perspective.config.title"), textRenderer), 2);
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
        GRID_ADDER.add(new SliderWidget(GRID_ADDER.getGridWidget().getX(), GRID_ADDER.getGridWidget().getY(), 150, 20, Text.translatable("gui.perspective.config.overlay_delay", Text.literal(String.valueOf(PerspectiveConfig.OVERLAY_DELAY))), OVERLAY_DELAY_VALUE) {
            @Override
            protected void updateMessage() {
                setMessage(Text.translatable("gui.perspective.config.overlay_delay", Text.literal(String.valueOf(PerspectiveConfig.OVERLAY_DELAY))));
            }

            @Override
            protected void applyValue() {
                PerspectiveConfig.OVERLAY_DELAY = (int) ((value) * 1000);
            }
        }, 1).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.overlay_delay.hover"), Text.translatable("gui.perspective.config.overlay_delay.hover")));
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.reset"), (button) -> {
            PerspectiveConfig.ZOOM_LEVEL = 20;
            PerspectiveConfig.OVERLAY_DELAY = 200;
            PerspectiveConfig.write_to_file();
            client.setScreen(new PerspectiveConfigScreen(PARENT_SCREEN));
        }).build(), 1).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.reset.hover"), Text.translatable("gui.perspective.config.reset.hover")));
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.perspective.config.back"), (button) -> {
            PerspectiveConfig.write_to_file();
            client.setScreen(PARENT_SCREEN);
        }).build(), 1).setTooltip(Tooltip.of(Text.translatable("gui.perspective.config.back.hover"), Text.translatable("gui.perspective.config.back.hover")));
        GRID.refreshPositions();
        GRID.forEachChild(this::addDrawableChild);
        initTabNavigation();
    }
    protected void initTabNavigation() {
        SimplePositioningWidget.setPos(GRID, getNavigationFocus());
    }
    public Text getNarratedTitle() {
        return ScreenTexts.joinSentences(this.title);
    }
    public boolean shouldCloseOnEsc() {
        return false;
    }
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        if (PerspectiveData.DEVELOPMENT_BUILD) {
            MultilineText WARNING = MultilineText.create(this.textRenderer, Text.translatable("overlay.perspective.warning"), this.width - 100);
            WARNING.drawCenterWithShadow(context, this.width / 2, 16, 9, 0xFFFFFF);
        }
    }
}