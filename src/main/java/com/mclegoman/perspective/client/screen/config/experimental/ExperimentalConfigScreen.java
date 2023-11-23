/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.experimental;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.ConfigScreenHelper;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.translation.TranslationType;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class ExperimentalConfigScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private boolean REFRESH;
    private final GridWidget GRID;
    private boolean SHOULD_CLOSE;
    public ExperimentalConfigScreen(Screen PARENT, boolean REFRESH) {
        super(Text.literal(""));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = PARENT;
        this.REFRESH = REFRESH;
    }
    public void init() {
        try {
            GRID.getMainPositioner().alignHorizontalCenter().margin(0);
            GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
            GRID_ADDER.add(ConfigScreenHelper.createTitle(client, new ExperimentalConfigScreen(PARENT_SCREEN, true), true, "experimental"));
            GRID_ADDER.add(createExperiments());
            GRID_ADDER.add(new EmptyWidget(4, 4));
            GRID_ADDER.add(createFooter());
            GRID.refreshPositions();
            GRID.forEachChild(this::addDrawableChild);
            initTabNavigation();
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize config>experimental screen: {}", Data.PERSPECTIVE_VERSION.getID(), error);
        }
    }

    public void tick() {
        try {
            if (this.REFRESH) {
                ClientData.CLIENT.setScreen(new ExperimentalConfigScreen(PARENT_SCREEN, false));
            }
            if (this.SHOULD_CLOSE) {
                ClientData.CLIENT.setScreen(PARENT_SCREEN);
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to tick perspective$config$experimental screen: {}", Data.PERSPECTIVE_VERSION.getID(), error);
        }
    }
    private GridWidget createEmpty() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(new MultilineTextWidget(Translation.getConfigTranslation("experimental.none", new Formatting[]{Formatting.RED, Formatting.BOLD}), ClientData.CLIENT.textRenderer).setCentered(true));
        return GRID;
    }
    private GridWidget createExperiments() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(new MultilineTextWidget(Translation.getConfigTranslation("experimental.warning", new Formatting[]{Formatting.RED, Formatting.BOLD}), ClientData.CLIENT.textRenderer).setCentered(true), 2);
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("experimental.allow_hide_players", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getExperimentalConfig("allow_hide_players"), TranslationType.ONFF)}), (button) -> {
            ConfigHelper.setExperimentalConfig("allow_hide_players", !(boolean) ConfigHelper.getExperimentalConfig("allow_hide_players"));
            REFRESH = true;
        }).build()).setTooltip(Tooltip.of(Translation.getConfigTranslation("experimental.allow_hide_players", true)));
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("experimental.hide_players", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getExperimentalConfig("hide_players"), TranslationType.ONFF)}), (button) -> {
            ConfigHelper.setExperimentalConfig("hide_players", !(boolean) ConfigHelper.getExperimentalConfig("hide_players"));
            REFRESH = true;
        }).build()).setTooltip(Tooltip.of(Translation.getConfigTranslation("experimental.hide_players", true)));
        GRID_ADDER.add(new EmptyWidget(4, 4), 2);
        return GRID;
    }
    private GridWidget createFooter() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("reset"), (button) -> {
            ConfigHelper.resetConfig();
            REFRESH = true;
        }).build());
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("back"), (button) -> this.SHOULD_CLOSE = true).build());
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
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == KeyBindingHelper.getBoundKeyOf(Keybindings.OPEN_CONFIG).getCode()) this.SHOULD_CLOSE = true;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}