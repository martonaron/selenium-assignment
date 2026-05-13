package utils;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.function.Supplier;

/**
 * JUnit @Rule that captures a screenshot whenever a test fails.
 * Accepts a Supplier<WebDriver> so the driver can be set after @Before.
 * Covers: screenshot_on_failure advanced task.
 */
public class ScreenshotRule extends TestWatcher {

    private final Supplier<WebDriver> driverSupplier;

    public ScreenshotRule(Supplier<WebDriver> driverSupplier) {
        this.driverSupplier = driverSupplier;
    }

    @Override
    protected void failed(Throwable e, Description description) {
        WebDriver driver = driverSupplier != null ? driverSupplier.get() : null;
        if (driver instanceof TakesScreenshot) {
            String screenshotsDir = TestConfig.getScreenshotsDir();
            try {
                Files.createDirectories(Paths.get(screenshotsDir));
                File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String fileName = description.getClassName() + "_" + description.getMethodName() + ".png";
                Path dest = Paths.get(screenshotsDir, fileName);
                Files.copy(srcFile.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Screenshot saved: " + dest.toAbsolutePath());
            } catch (IOException ex) {
                System.err.println("Failed to save screenshot: " + ex.getMessage());
            }
        }
    }
}
