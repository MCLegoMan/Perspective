/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.ambience;

import com.mclegoman.perspective.client.ambience.particles.ParticleEffects;
import com.mclegoman.perspective.client.ambience.sounds.SoundEvents;

public class Ambience {
	public static void init() {
		SoundEvents.init();
		ParticleEffects.init();
	}
}
