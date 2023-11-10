/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.more_options.hide;

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
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class HideConfigScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private boolean REFRESH;
    private final GridWidget GRID;
    private boolean SHOULD_CLOSE;
    public HideConfigScreen(Screen PARENT, boolean REFRESH) {
        super(Text.literal(""));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = PARENT;
        this.REFRESH = REFRESH;
    }
    public void init() {
        try {
            GRID.getMainPositioner().alignHorizontalCenter().margin(0);
            GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
            GRID_ADDER.add(ConfigScreenHelper.createTitle(ClientData.CLIENT, new HideConfigScreen(PARENT_SCREEN, true), true, "hide"));
            GRID_ADDER.add(createHide());
            GRID_ADDER.add(new EmptyWidget(4, 4));
            GRID_ADDER.add(createFooter());
            GRID.refreshPositions();
            GRID.forEachChild(this::addDrawableChild);
            initTabNavigation();
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize config$hide screen: {}", Data.PERSPECTIVE_VERSION.getID(), error);
        }
    }

    public void tick() {
        try {
            if (this.REFRESH) {
                ClientData.CLIENT.setScreen(new HideConfigScreen(PARENT_SCREEN, false));
            }
            if (this.SHOULD_CLOSE) {
                ClientData.CLIENT.setScreen(PARENT_SCREEN);
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to tick config$hide screen: {}", Data.PERSPECTIVE_VERSION.getID(), error);
        }
    }
    private GridWidget createHide() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);

        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("hide.hide_block_outline", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig("hide_block_outline"), TranslationType.ONFF)}), (button) -> {
            ConfigHelper.setConfig("hide_block_outline", !(boolean) ConfigHelper.getConfig("hide_block_outline"));
            this.REFRESH = true;
        }).width(304).build(), 2).setTooltip(Tooltip.of(Translation.getConfigTranslation("hide.hide_block_outline", true)));

        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("hide.hide_crosshair", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig("hide_crosshair"), TranslationType.ONFF)}), (button) -> {
            ConfigHelper.setConfig("hide_crosshair", !(boolean) ConfigHelper.getConfig("hide_crosshair"));
            this.REFRESH = true;
        }).width(304).build(), 2).setTooltip(Tooltip.of(Translation.getConfigTranslation("hide.hide_crosshair", true)));

        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("hide.hide_armor", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig("hide_armor"), TranslationType.ONFF)}), (button) -> {
            ConfigHelper.setConfig("hide_armor", !(boolean) ConfigHelper.getConfig("hide_armor"));
            this.REFRESH = true;
        }).width(304).build(), 2).setTooltip(Tooltip.of(Translation.getConfigTranslation("hide.hide_armor", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig("hide_armor"), TranslationType.ONFF)}, true)));

        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("hide.hide_nametags", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig("hide_nametags"), TranslationType.ONFF)}), (button) -> {
            ConfigHelper.setConfig("hide_nametags", !(boolean) ConfigHelper.getConfig("hide_nametags"));
            this.REFRESH = true;
        }).width(304).build(), 2).setTooltip(Tooltip.of(Translation.getConfigTranslation("hide.hide_nametags", true)));
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