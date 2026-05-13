package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads test configuration from config.properties on the classpath.
 * Covers: config_file advanced task.
 */
public class TestConfig {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = TestConfig.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new RuntimeException("config.properties not found on classpath");
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private TestConfig() {
    }

    public static String getBaseUrl() {
        return PROPS.getProperty("base.url");
    }

    public static String getSeleniumUrl() {
        return PROPS.getProperty("selenium.url");
    }

    public static String getScreenshotsDir() {
        return PROPS.getProperty("screenshots.dir", "/tmp/screenshots");
    }

    public static int getWindowWidth() {
        return Integer.parseInt(PROPS.getProperty("window.width", "1920"));
    }

    public static int getWindowHeight() {
        return Integer.parseInt(PROPS.getProperty("window.height", "1080"));
    }
}
