/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.common.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.TimeZone;

public class DateHelper {
	// We use the GMT+12 timezone to make sure all players have the same experience.
	public static LocalDate getDate() {
		return LocalDate.now(TimeZone.getTimeZone("GMT+12").toZoneId());
	}
	public static LocalTime getTime() {
		return LocalTime.now(TimeZone.getTimeZone("GMT+12").toZoneId());
	}
}
