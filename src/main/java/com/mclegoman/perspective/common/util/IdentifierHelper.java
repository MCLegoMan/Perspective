package com.mclegoman.perspective.common.util;

import net.minecraft.util.Identifier;

public class IdentifierHelper {
	public static Identifier identifierFromString(String identifier) {
		String namespace = !identifier.contains(":") ? identifier.substring(0, identifier.indexOf(":")) : "minecraft";
		String key = identifier.substring(identifier.indexOf(":") + 1);
		return new Identifier(namespace, key);
	}
	public static String stringFromIdentifier(Identifier identifier) {
		return identifier.getNamespace() + ":" + identifier.getPath();
	}
	public static String getStringPart(Type type, String identifier) {
		return type.equals(Type.NAMESPACE) ? (!identifier.contains(":") ? identifier.substring(0, identifier.indexOf(":")) : "minecraft") : (type.equals(Type.KEY) ? (!identifier.contains(":") ? identifier : identifier.substring(identifier.indexOf(":") + 1)) : null);
	}
	public enum Type {
		NAMESPACE,
		KEY
	}
}
