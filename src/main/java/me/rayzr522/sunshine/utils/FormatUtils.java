package me.rayzr522.sunshine.utils;

import me.rayzr522.sunshine.Sunshine;

public class FormatUtils {
    public static String time(int minutes) {
        int hours = minutes / 60;
        minutes %= 60;

        if (hours > 0) {
            return Sunshine.getInstance().trRaw("system.time-format.hour-or-more", hours, minutes);
        } else {
            return Sunshine.getInstance().trRaw("system.time-format.less-than-an-hour", minutes);
        }
    }
}
