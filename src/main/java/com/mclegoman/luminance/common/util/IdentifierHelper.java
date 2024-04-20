package com.mclegoman.luminance.common.util;

import net.minecraft.util.Identifier;

public class IdentifierHelper {
	public static Identifier identifierFromString(String identifier) {
		String namespace = getStringPart(Type.NAMESPACE, identifier);
		String key = getStringPart(Type.KEY, identifier);
		return namespace != null && key != null ? new Identifier(namespace, key) : null;
	}
	public static String stringFromIdentifier(Identifier identifier) {
		return identifier.getNamespace() + ":" + identifier.getPath();
	}
	public static String getStringPart(Type type, String identifier) {
		return type.equals(Type.NAMESPACE) ? (identifier.contains(":") ? identifier.substring(0, identifier.indexOf(":")) : "minecraft") : (type.equals(Type.KEY) ? (identifier.contains(":") ? identifier.substring(identifier.indexOf(":") + 1) : identifier) : null);
	}
	public static String getStringPart(Type type, String identifier, String fallback) {
		String part = getStringPart(type, identifier);
		return part != null ? part : fallback;
	}
	public enum Type {
		NAMESPACE,
		KEY
	}
}
