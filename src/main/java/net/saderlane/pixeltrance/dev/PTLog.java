package net.saderlane.pixeltrance.dev;

import com.mojang.logging.LogUtils;
import net.saderlane.pixeltrance.PixelTrance;

import org.slf4j.Logger;

public class PTLog {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String PREFIX = "[PixelTrance]";

    public static void info(String message) {
        LOGGER.info(PREFIX + message);
    }

    public static void warn(String message) {
        LOGGER.warn(PREFIX + message);
    }

    public static void error(String message) {
        LOGGER.error(PREFIX + message);
    }

    public static void debug(String message) {
        LOGGER.debug(PREFIX + message);
    }
}
