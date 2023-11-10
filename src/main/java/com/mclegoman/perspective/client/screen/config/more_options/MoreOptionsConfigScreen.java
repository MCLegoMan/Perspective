/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.more_options;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.ConfigScreenHelper;
import com.mclegoman.perspective.client.screen.config.more_options.hide.HideConfigScreen;
import com.mclegoman.perspective.client.screen.config.more_options.toasts.ToastsConfigScreen;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.translation.TranslationType;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class MoreOptionsConfigScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private boolean REFRESH;
    private final GridWidget GRID;
    private boolean SHOULD_CLOSE;
    public MoreOptionsConfigScreen(Screen PARENT, boolean REFRESH) {
        super(Text.literal(""));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = PARENT;
        this.REFRESH = REFRESH;
    }
    public void init() {
        try {
            GRID.getMainPositioner().alignHorizontalCenter().margin(0);
            GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
            GRID_ADDER.add(ConfigScreenHelper.createTitle(ClientData.CLIENT, new MoreOptionsConfigScreen(PARENT_SCREEN, true), true, "more_options"));
            GRID_ADDER.add(createMoreOptions());
            GRID_ADDER.add(new EmptyWidget(4, 4));
            GRID_ADDER.add(createFooter());
            GRID.refreshPositions();
            GRID.forEachChild(this::addDrawableChild);
            initTabNavigation();
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize config$more_options screen: {}", Data.PERSPECTIVE_VERSION.getID(), error);
        }
    }

    public void tick() {
        try {
            if (this.REFRESH) {
                ClientData.CLIENT.setScreen(new MoreOptionsConfigScreen(PARENT_SCREEN, false));
            }
            if (this.SHOULD_CLOSE) {
                ClientData.CLIENT.setScreen(PARENT_SCREEN);
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to tick config$more_options screen: {}", Data.PERSPECTIVE_VERSION.getID(), error);
        }
    }
    private GridWidget createMoreOptions() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);

        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("more_options.version_overlay", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig("version_overlay"), TranslationType.ONFF)}), (button) -> {
            ConfigHelper.setConfig("version_overlay", !(boolean) ConfigHelper.getConfig("version_overlay"));
            this.REFRESH = true;
        }).build()).setTooltip(Tooltip.of(Translation.getConfigTranslation("more_options.version_overlay", true)));

        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("more_options.force_pride", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig("force_pride"), TranslationType.ONFF)}), (button) -> {
            ConfigHelper.setConfig("force_pride", !(boolean) ConfigHelper.getConfig("force_pride"));
            this.REFRESH = true;
        }).build()).setTooltip(Tooltip.of(Translation.getConfigTranslation("more_options.force_pride", true)));

        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("more_options.show_death_coordinates", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig("show_death_coordinates"), TranslationType.ONFF)}), (button) -> {
            ConfigHelper.setConfig("show_death_coordinates", !(boolean) ConfigHelper.getConfig("show_death_coordinates"));
            this.REFRESH = true;
        }).width(304).build(), 2).setTooltip(Tooltip.of(Translation.getConfigTranslation("more_options.show_death_coordinates", true)));

        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("hide"), (button) -> {
            ClientData.CLIENT.setScreen(new HideConfigScreen(new MoreOptionsConfigScreen(PARENT_SCREEN, false), false));
        }).build()).setTooltip(Tooltip.of(Translation.getConfigTranslation("hide", true)));

        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("toasts"), (button) -> {
            ClientData.CLIENT.setScreen(new ToastsConfigScreen(new MoreOptionsConfigScreen(PARENT_SCREEN, false), false, false));
        }).build()).setTooltip(Tooltip.of(Translation.getConfigTranslation("toasts", true)));

        return GRID;
    }
    private GridWidget createFooter() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("reset"), (button) -> {
            ConfigHelper.resetConfig();
            this.REFRESH = true;
        }).build());
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("back"), (button) -> this.SHOULD_CLOSE = true).build());
        return GRID;
    }
    public void initTabNavigation() {
        try {
            SimplePositioningWidget.setPos(GRID, getNavigationFocus());
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize config>more options screen TabNavigation: {}", Data.PERSPECTIVE_VERSION.getID(), error);
        }
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