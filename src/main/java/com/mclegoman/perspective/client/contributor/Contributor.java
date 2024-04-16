package com.mclegoman.perspective.client.contributor;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Contributor {
	private static final List<String> allowedUuids = new ArrayList<>();
	public static void init() {
		addAllowedUuid("772eb47b-a24e-4d43-a685-6ca9e9e132f7"); // MCLegoMan
		addAllowedUuid("9c3adf8d-a723-40c9-845b-c6e78c3ac460"); // Nettakrim
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ContributorDataloader());
	}
	protected static void addAllowedUuid(String uuid) {
		if (!allowedUuids.contains(uuid)) allowedUuids.add(uuid);
	}
	protected static boolean isAllowedUuid(String uuid) {
		return allowedUuids.contains(uuid);
	}
}