/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TexturedEntityData {
	private final String namespace;
	private final String type;
	private final String name;
	private final JsonObject entity_specific;
	private final JsonArray overrides;
	public TexturedEntityData(String namespace, String type, String name, JsonObject entity_specific, JsonArray overrides) {
		this.namespace = namespace;
		this.type = type;
		this.name = name;
		this.entity_specific = entity_specific;
		this.overrides = overrides;
	}
	public String getNamespace() {
		return this.namespace;
	}
	public String getType() {
		return this.type;
	}
	public String getName() {
		return this.name;
	}
	public JsonObject getEntitySpecific() {
		return this.entity_specific;
	}
	public JsonArray getOverrides() {
		return this.overrides;
	}
}
