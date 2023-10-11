/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.more_options.hide;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.screen.config.PerspectiveConfigScreenHelper;
import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.client.translation.PerspectiveTranslationType;
import com.mclegoman.perspective.client.util.PerspectiveKeybindings;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

@Environment(EnvType.CLIENT)
public class PerspectiveHideConfigScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private boolean REFRESH;
    private final GridWidget GRID;
    private boolean SHOULD_CLOSE;
    public PerspectiveHideConfigScreen(Screen PARENT, boolean REFRESH) {
        super(Text.literal(""));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = PARENT;
        this.REFRESH = REFRESH;
    }
    public void init() {
        try {
            GRID.getMainPositioner().alignHorizontalCenter().margin(0);
            GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
            GRID_ADDER.add(PerspectiveConfigScreenHelper.createTitle(PerspectiveClientData.CLIENT, new PerspectiveHideConfigScreen(PARENT_SCREEN, true), true, "hide"));
            GRID_ADDER.add(createHide());
            GRID_ADDER.add(new EmptyWidget(4, 4));
            GRID_ADDER.add(createFooter());
            GRID.refreshPositions();
            GRID.forEachChild(this::addDrawableChild);
            initTabNavigation();
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize config$hide screen: {}", PerspectiveData.PERSPECTIVE_VERSION.getID(), error);
        }
    }

    public void tick() {
        try {
            if (this.REFRESH) {
                PerspectiveClientData.CLIENT.setScreen(new PerspectiveHideConfigScreen(PARENT_SCREEN, false));
            }
            if (this.SHOULD_CLOSE) {
                PerspectiveClientData.CLIENT.setScreen(PARENT_SCREEN);
            }
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to tick config$hide screen: {}", PerspectiveData.PERSPECTIVE_VERSION.getID(), error);
        }
    }
    private GridWidget createHide() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);

        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("hide.hide_block_outline", new Object[]{PerspectiveTranslation.getVariableTranslation((boolean)PerspectiveConfigHelper.getConfig("hide_block_outline"), PerspectiveTranslationType.ONFF)}), (button) -> {
            PerspectiveConfigHelper.setConfig("hide_block_outline", !(boolean)PerspectiveConfigHelper.getConfig("hide_block_outline"));
            this.REFRESH = true;
        }).width(304).build(), 2).setTooltip(Tooltip.of(PerspectiveTranslation.getConfigTranslation("hide.hide_block_outline", true)));

        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("hide.hide_armor", new Object[]{PerspectiveTranslation.getVariableTranslation((boolean)PerspectiveConfigHelper.getConfig("hide_armor"), PerspectiveTranslationType.ONFF)}), (button) -> {
            PerspectiveConfigHelper.setConfig("hide_armor", !(boolean)PerspectiveConfigHelper.getConfig("hide_armor"));
            this.REFRESH = true;
        }).width(304).build(), 2).setTooltip(Tooltip.of(PerspectiveTranslation.getConfigTranslation("hide.hide_armor", new Object[]{PerspectiveTranslation.getVariableTranslation((boolean)PerspectiveConfigHelper.getConfig("hide_armor"), PerspectiveTranslationType.ONFF)}, true)));

        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("hide.hide_nametags", new Object[]{PerspectiveTranslation.getVariableTranslation((boolean)PerspectiveConfigHelper.getConfig("hide_nametags"), PerspectiveTranslationType.ONFF)}), (button) -> {
            PerspectiveConfigHelper.setConfig("hide_nametags", !(boolean)PerspectiveConfigHelper.getConfig("hide_nametags"));
            this.REFRESH = true;
        }).width(304).build(), 2).setTooltip(Tooltip.of(PerspectiveTranslation.getConfigTranslation("hide.hide_nametags", true)));
        return GRID;
    }
    private GridWidget createFooter() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("reset"), (button) -> {
            PerspectiveConfigHelper.resetConfig();
            this.REFRESH = true;
        }).build());
        GRID_ADDER.add(ButtonWidget.builder(PerspectiveTranslation.getConfigTranslation("back"), (button) -> this.SHOULD_CLOSE = true).build());
        return GRID;
    }
    public void initTabNavigation() {
        try {
            SimplePositioningWidget.setPos(GRID, getNavigationFocus());
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize config>more options screen TabNavigation: {}", PerspectiveData.PERSPECTIVE_VERSION.getID(), error);
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
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == KeyBindingHelper.getBoundKeyOf(PerspectiveKeybindings.OPEN_CONFIG).getCode()) this.SHOULD_CLOSE = true;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}