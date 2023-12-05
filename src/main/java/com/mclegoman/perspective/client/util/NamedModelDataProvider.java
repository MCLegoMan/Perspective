/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.util;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class NamedModelDataProvider {
	public static void init() {
		ModelPredicateProviderRegistry.GLOBAL.put(new Identifier("named_model_data"), (stack, world, entity, seed) -> stack.hasCustomName() ? getSeedFloat(stack.getName().getString()) : 0.0F);
	}
	private static float getSeedFloat(String seed) {
		seed = seed.trim();
		if (seed.isEmpty()) {
			return 0.0F;
		} else {
			try {
				return Integer.parseInt(seed);
			} catch (NumberFormatException var2) {
				return seed.hashCode();
			}
		}
	}
}
