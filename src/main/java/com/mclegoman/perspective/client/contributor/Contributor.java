package com.mclegoman.perspective.client.contributor;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

import java.util.ArrayList;
import java.util.List;

public class Contributor {
	private static final List<String> allowedUuids = new ArrayList<>();
	public static void init() {
		// Whilst players still could technically use this through the april fools' prank, it would set all players not specific players.
		initAllowedUuids();
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ContributorDataloader());
	}
	private static void initAllowedUuids() {
		// MCLegoMan
		addAllowedUuid("772eb47b-a24e-4d43-a685-6ca9e9e132f7", "3445ebd7-25f8-41a6-8118-0d19d7f5559e");
		// Nettakrim
		addAllowedUuid("9c3adf8d-a723-40c9-845b-c6e78c3ac460");
	}
	protected static void addAllowedUuid(String... uuids) {
		for (String uuid : uuids) {
			if (!allowedUuids.contains(uuid)) allowedUuids.add(uuid);
		}
	}
	protected static boolean isAllowedUuid(String uuid) {
		return allowedUuids.contains(uuid);
	}
}