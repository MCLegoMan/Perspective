/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.april_fools_prank.AprilFoolsPrankConfigScreen;
import com.mclegoman.perspective.client.screen.config.experimental.ExperimentalConfigScreen;
import com.mclegoman.perspective.client.screen.config.hold_perspective.HoldPerspectiveConfigScreen;
import com.mclegoman.perspective.client.screen.config.information.InformationScreen;
import com.mclegoman.perspective.client.screen.config.more_options.MoreOptionsConfigScreen;
import com.mclegoman.perspective.client.screen.config.shaders.ShadersConfigScreen;
import com.mclegoman.perspective.client.screen.config.textured_entity.TexturedEntityConfigScreen;
import com.mclegoman.perspective.client.screen.config.zoom.ZoomConfigScreen;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ConfigScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private boolean REFRESH;
    private final GridWidget GRID;
    private boolean SHOULD_CLOSE;
    public ConfigScreen(Screen PARENT, boolean REFRESH) {
        super(Text.literal(""));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = PARENT;
        this.REFRESH = REFRESH;
    }
    public void init() {
        try {
            GRID.getMainPositioner().alignHorizontalCenter().margin(0);
            GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
            GRID_ADDER.add(ConfigScreenHelper.createTitle(client, new ConfigScreen(PARENT_SCREEN, true), false, ""));
            GRID_ADDER.add(createConfig());
            GRID_ADDER.add(createInformationAndExperiments());
            GRID_ADDER.add(new EmptyWidget(4, 4));
            GRID_ADDER.add(createFooter());
            GRID.refreshPositions();
            GRID.forEachChild(this::addDrawableChild);
            initTabNavigation();
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize config screen: {}", Data.PERSPECTIVE_VERSION.getID(), error);
        }
    }

    public void tick() {
        try {
            if (this.REFRESH) {
                ClientData.CLIENT.setScreen(new ConfigScreen(PARENT_SCREEN, false));
            }
            if (this.SHOULD_CLOSE) {
                ConfigHelper.saveConfig(false);
                ClientData.CLIENT.setScreen(PARENT_SCREEN);
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to tick perspective$config screen: {}", Data.PERSPECTIVE_VERSION.getID(), error);
        }
    }
    private GridWidget createConfig() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("zoom"), (button) -> ClientData.CLIENT.setScreen(new ZoomConfigScreen(new ConfigScreen(PARENT_SCREEN, true), false))).build());
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("shaders"), (button) -> ClientData.CLIENT.setScreen(new ShadersConfigScreen(new ConfigScreen(PARENT_SCREEN, true), false, false))).build());
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("hold_perspective"), (button) -> ClientData.CLIENT.setScreen(new HoldPerspectiveConfigScreen(new ConfigScreen(PARENT_SCREEN, true), false))).build());
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("textured_entity"), (button) -> ClientData.CLIENT.setScreen(new TexturedEntityConfigScreen(new ConfigScreen(PARENT_SCREEN, true), false))).build());
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("april_fools_prank"), (button) -> ClientData.CLIENT.setScreen(new AprilFoolsPrankConfigScreen(new ConfigScreen(PARENT_SCREEN, true), false))).build());
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("more_options"), (button) -> ClientData.CLIENT.setScreen(new MoreOptionsConfigScreen(new ConfigScreen(PARENT_SCREEN, true), false))).build());
        return GRID;
    }
    private GridWidget createInformationAndExperiments() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("information"), (button) -> ClientData.CLIENT.setScreen(new InformationScreen(new ConfigScreen(PARENT_SCREEN, true), false))).build());
        ButtonWidget EXPERIMENTAL = ButtonWidget.builder(Translation.getConfigTranslation("experimental"), (button) -> ClientData.CLIENT.setScreen(new ExperimentalConfigScreen(new ConfigScreen(PARENT_SCREEN, true), false))).build();
        EXPERIMENTAL.active = ConfigHelper.EXPERIMENTS_AVAILABLE;
        GRID_ADDER.add(EXPERIMENTAL);
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