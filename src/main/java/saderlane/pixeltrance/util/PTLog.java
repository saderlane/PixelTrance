package saderlane.pixeltrance.util;

import org.slf4j.Logger;
import saderlane.pixeltrance.PixelTrance;

// Utility for quick logging in console
public class PTLog {

    private static final Logger LOGGER = PixelTrance.LOGGER;

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void warn(String message) {
        LOGGER.warn(message);
    }

    public static void error(String message) {
        LOGGER.error(message);
    }

    public static void debug(String message) {
        LOGGER.debug(message);
    }
}