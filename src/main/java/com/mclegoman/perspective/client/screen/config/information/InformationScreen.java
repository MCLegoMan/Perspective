/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.information;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.ConfigScreenHelper;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class InformationScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private final boolean REFRESH;
    private final GridWidget GRID;
    private boolean SHOULD_CLOSE;
    public InformationScreen(Screen PARENT, boolean REFRESH) {
        super(Text.literal(""));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = PARENT;
        this.REFRESH = REFRESH;
    }
    public void init() {
        try {
            GRID.getMainPositioner().alignHorizontalCenter().margin(0);
            GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
            GRID_ADDER.add(ConfigScreenHelper.createTitle(client, new InformationScreen(PARENT_SCREEN, true), true, "information"));
            GRID_ADDER.add(createInformation());
            GRID_ADDER.add(new EmptyWidget(4, 4));
            GRID_ADDER.add(createFooter());
            GRID.refreshPositions();
            GRID.forEachChild(this::addDrawableChild);
            initTabNavigation();
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize information screen: {}", Data.PERSPECTIVE_VERSION.getID(), error);
        }
    }

    public void tick() {
        try {
            if (this.REFRESH) {
                ClientData.CLIENT.setScreen(new InformationScreen(PARENT_SCREEN, false));
            }
            if (this.SHOULD_CLOSE) {
                ClientData.CLIENT.setScreen(PARENT_SCREEN);
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to tick perspective$config$info screen: {}", Data.PERSPECTIVE_VERSION.getID(), error);
        }
    }
    private GridWidget createInformation() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("information.documentation"), ConfirmLinkScreen.opening(this, "https://mclegoman.github.io/Perspective")).width(304).build(), 2).setTooltip(Tooltip.of(Translation.getConfigTranslation("information.documentation", true)));
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("information.contribute"), ConfirmLinkScreen.opening(this, "https://mclegoman.github.io/Perspective/source_code/")).build(), 1).setTooltip(Tooltip.of(Translation.getConfigTranslation("information.contribute", true)));
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("information.report"), ConfirmLinkScreen.opening(this, "https://github.com/MCLegoMan/Perspective/issues")).build(), 1).setTooltip(Tooltip.of(Translation.getConfigTranslation("information.report", true)));
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("information.credits_attribution"), ConfirmLinkScreen.opening(this, "https://mclegoman.github.io/Perspective/credits_attribution")).build(), 1).setTooltip(Tooltip.of(Translation.getConfigTranslation("information.credits_attribution", true)));
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("information.licenses"), ConfirmLinkScreen.opening(this, "https://mclegoman.github.io/Perspective/licenses")).build(), 1).setTooltip(Tooltip.of(Translation.getConfigTranslation("information.licenses", true)));
        return GRID;
    }
    private GridWidget createFooter() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
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