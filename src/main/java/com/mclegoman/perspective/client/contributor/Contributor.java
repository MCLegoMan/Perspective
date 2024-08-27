/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.contributor;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

import java.util.ArrayList;
import java.util.List;

public class Contributor {
	/**
	 * Developers Notes:
	 * 1. Having contributor features be set and obtained through an api would be pretty cool,
	 * but as this would require a connection to a server, dataloaders are best for now.
	 * 2. Players could technically use this through the April Fools' Prank,
	 * but it would set all players, not specific ones.
	**/
	private static final List<ContributorLockData> allowedUuids = new ArrayList<>();
	public static void init() {
		initAllowedUuids();
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ContributorDataLoader());
	}
	private static void initAllowedUuids() {
		initDeveloperUuids();
		initContributorUuids();
		initAttributionUuids();
	}
	private static void initDeveloperUuids() {
		addAllowedUuid(Type.DEVELOPER, "772eb47b-a24e-4d43-a685-6ca9e9e132f7", "3445ebd7-25f8-41a6-8118-0d19d7f5559e"); // MCLegoMan
	}
	private static void initContributorUuids() {
		addAllowedUuid(Type.CONTRIBUTOR, "9c3adf8d-a723-40c9-845b-c6e78c3ac460"); // Nettakrim
	}
	private static void initAttributionUuids() {
		addAllowedUuid(Type.ATTRIBUTION, "1f7a5ac1-664b-479d-9e2d-15ed891b080c"); // Phantazap: "Jester" Giant Textured Entity Texture.
	}
	private static void addAllowedUuid(Type type, String... uuids) {
		for (String uuid : uuids) {
			if (getUuid(uuid) == null) allowedUuids.add(new ContributorLockData(type, uuid));
		}
	}
	protected static ContributorLockData getUuid(String uuid) {
		for (ContributorLockData user : allowedUuids) {
			if (user.getUuids().contains(uuid)) return user;
		}
		return null;
	}
	public enum Type {
		DEVELOPER("developer"),
		CONTRIBUTOR("contributor"),
		ATTRIBUTION("attribution");
		private final String name;
		Type(String name) {
			this.name = name;
		}
		public String getName() {
			return this.name;
		}
	}
}