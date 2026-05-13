package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;

/**
 * Creates a configured RemoteWebDriver instance pointing at the Selenium Grid.
 * Covers: webdriver_config and dockerized_execution advanced tasks.
 */
public class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        try {
            ChromeOptions options = buildChromeOptions();
            WebDriver driver = new RemoteWebDriver(
                    URI.create(TestConfig.getSeleniumUrl()).toURL(), options);
            driver.manage().window().setSize(
                    new org.openqa.selenium.Dimension(
                            TestConfig.getWindowWidth(),
                            TestConfig.getWindowHeight()));
            return driver;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create RemoteWebDriver", e);
        }
    }

    private static ChromeOptions buildChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
        return options;
    }
}
