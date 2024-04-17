/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.hide;

import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hud.MessageOverlay;
import com.mclegoman.perspective.client.perspective.Perspective;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.translation.TranslationType;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Hide {
	public static final String[] hideCrosshairModes = new String[]{"false", "dynamic", "true"};
	public static void init() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new HideArmorDataLoader());
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new HideNameTagsDataLoader());
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new HidePlayerDataLoader());
	}
	public static void tick() {
		if (Keybindings.toggleArmour.wasPressed()) {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "hide_armor", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_armor"));
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_show_message"))
				MessageOverlay.setOverlay(Text.translatable("gui.perspective.message.hide.armor", Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_armor"), TranslationType.ENDISABLE)).formatted(Formatting.GOLD));
		}
		if (Keybindings.toggleBlockOutline.wasPressed()) {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "hide_block_outline", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_block_outline"));
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_show_message"))
				MessageOverlay.setOverlay(Text.translatable("gui.perspective.message.hide.block_outline", Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_block_outline"), TranslationType.ENDISABLE)).formatted(Formatting.GOLD));
		}
		if (Keybindings.cycleCrosshair.wasPressed()) {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "hide_crosshair", nextCrosshairMode());
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_show_message"))
				MessageOverlay.setOverlay(Text.translatable("gui.perspective.message.hide.crosshair", Translation.getHideCrosshairModeTranslation(Data.VERSION.getID(), (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_crosshair"))).formatted(Formatting.GOLD));
		}
		if (Keybindings.toggleNametags.wasPressed()) {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "hide_nametags", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_nametags"));
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_show_message"))
				MessageOverlay.setOverlay(Text.translatable("gui.perspective.message.hide.nametags", Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_nametags"), TranslationType.ENDISABLE)).formatted(Formatting.GOLD));
		}
		if (Keybindings.togglePlayers.wasPressed()) {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "hide_players", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_players"));
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_show_message"))
				MessageOverlay.setOverlay(Text.translatable("gui.perspective.message.hide.players", Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_nametags"), TranslationType.ENDISABLE)).formatted(Formatting.GOLD));
		}
	}
	public static boolean shouldHidePlayer(PlayerEntity player) {
		if (ClientData.minecraft.player != null) {
			UUID uuid = player.getGameProfile().getId();
			if (!uuid.equals(ClientData.minecraft.player.getGameProfile().getId()))
				return (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_players") || HidePlayerDataLoader.REGISTRY.contains(String.valueOf(player.getGameProfile().getId()));
		}
		return false;
	}
	public static String nextCrosshairMode() {
		List<String> crosshairModes = Arrays.stream(hideCrosshairModes).toList();
		return crosshairModes.contains((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_crosshair")) ? hideCrosshairModes[(crosshairModes.indexOf((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hide_crosshair")) + 1) % hideCrosshairModes.length] : hideCrosshairModes[0];
	}
	public static boolean shouldHideHud(HideHudTypes type) {
		switch (type) {
			case zoom -> {return Zoom.isZooming() && (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_hide_hud");}
			case holdPerspective -> {return Perspective.isHoldingPerspective() && (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "hold_perspective_hide_hud");}
			default -> {return false;}
		}
	}
}