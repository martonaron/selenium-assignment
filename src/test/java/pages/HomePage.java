package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.TestConfig;

/**
 * Page object for the site homepage.
 */
public class HomePage extends BasePage {

    private static final By NAV_LINKS = By.xpath("//header//a[@href]");
    private static final By CART_BUTTON = By.xpath("//button[@aria-label]");
    private static final By ACCOUNT_LINK = By.xpath("//header//a[contains(@href,'account')]");
    private static final By PRODUCTS_HEADING = By
            .xpath("//*[self::h1 or self::h2 or self::h3][string-length(normalize-space(text()))>0]");

    private final String baseUrl;

    public HomePage(WebDriver driver) {
        super(driver);
        this.baseUrl = TestConfig.getBaseUrl();
    }

    public void open() {
        driver.get(baseUrl + "/us/en/");
        waitForVisible(By.tagName("header"));
    }

    public boolean isHeaderVisible() {
        return waitForVisible(By.tagName("header")).isDisplayed();
    }

    public boolean isCartButtonPresent() {
        return !driver.findElements(CART_BUTTON).isEmpty();
    }

    public boolean isAccountLinkPresent() {
        return !driver.findElements(ACCOUNT_LINK).isEmpty();
    }

    public int countNavLinks() {
        return driver.findElements(NAV_LINKS).size();
    }

    public boolean hasPageHeading() {
        return !driver.findElements(PRODUCTS_HEADING).isEmpty();
    }

    public void navigateToUrl(String url) {
        driver.get(url);
    }

    public void goBack() {
        driver.navigate().back();
    }

    public void goForward() {
        driver.navigate().forward();
    }
}
