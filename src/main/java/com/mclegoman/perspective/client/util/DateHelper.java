/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.util;

import java.time.LocalDate;
import java.util.TimeZone;

public class DateHelper {
	public static LocalDate getDate() {
		// We use the GMT+12 timezone to make sure all players have the same experience.
		return LocalDate.now(TimeZone.getTimeZone("GMT+12").toZoneId());
	}
}
