/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.ambience.sounds;

import com.mclegoman.perspective.common.data.Data;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundEvents {
	public static SoundEvent splash;
	public static void init() {
	}
	static {
		splash = Registry.register(Registries.SOUND_EVENT, Identifier.of(Data.version.getID(), "ambience.splash"), SoundEvent.of(Identifier.of(Data.version.getID(), "ambience.splash")));
	}
}
