/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client;

import com.mclegoman.perspective.client.april_fools_prank.AprilFoolsPrank;
import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.contributor.Contributor;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hide.Hide;
import com.mclegoman.perspective.client.panorama.Panorama;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import com.mclegoman.perspective.client.util.CustomNamedIntItemModel;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.client.util.ResourcePacks;
import com.mclegoman.perspective.client.util.Tick;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

import java.util.OptionalLong;

public class PerspectiveClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		try {
			Data.PERSPECTIVE_VERSION.getLogger().info("{} Initializing {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), Data.PERSPECTIVE_VERSION.getID());
			ConfigHelper.init();
			ResourcePacks.init();
			Contributor.init();
			Hide.init();
			Keybindings.init();
			Shader.init();
			Panorama.init();
			TexturedEntity.init();
			CustomNamedIntItemModel.init();
			AprilFoolsPrank.init();
			Tick.init();
			ClientData.finishedInitializing();
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to run onInitializeClient: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
		}
	}
}