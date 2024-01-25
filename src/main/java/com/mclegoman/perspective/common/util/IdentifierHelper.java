package com.mclegoman.perspective.common.util;

import net.minecraft.util.Identifier;

public class IdentifierHelper {
	public static Identifier identifierFromString(String identifier) {
		String namespace = identifier.substring(0, identifier.indexOf(":"));
		String key = identifier.substring(identifier.indexOf(":") + 1);
		return new Identifier(namespace, key);
	}
	public static String stringFromIdentifier(Identifier identifier) {
		return identifier.getNamespace() + ":" + identifier.getPath();
	}
}
