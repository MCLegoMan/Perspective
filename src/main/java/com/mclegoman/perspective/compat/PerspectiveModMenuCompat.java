package com.mclegoman.perspective.compat;

import com.mclegoman.perspective.screen.PerspectiveConfigScreen;
import com.mclegoman.perspective.util.PerspectiveUtils;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class PerspectiveModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new PerspectiveConfigScreen(MinecraftClient.getInstance().currentScreen);
    }
}
