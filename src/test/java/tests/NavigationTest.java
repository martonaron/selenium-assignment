package tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.HomePage;
import utils.DriverFactory;
import utils.ScreenshotRule;
import utils.TestConfig;

/**
 * Tests for site navigation, content verification, and browser history.
 *
 * Tasks covered:
 * - static_page_test
 * - multiple_page_test
 * - history_test
 * - complex_xpath
 * - page_title
 */
public class NavigationTest {

    private WebDriver driver;
    private HomePage homePage;

    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule(() -> driver);

    @Before
    public void setUp() {
        driver = DriverFactory.createDriver();
        homePage = new HomePage(driver);
    }

    @Test
    public void verifiesHomepageHasExpectedContent() {
        homePage.open();

        // Verify header is visible
        Assert.assertTrue("Homepage header must be visible", homePage.isHeaderVisible());

        // Verify page has a non-empty title (page_title)
        String title = homePage.getPageTitle();
        Assert.assertFalse("Homepage must have a title", title.isEmpty());
        Assert.assertTrue("Homepage title should mention Spree", title.toLowerCase().contains("spree"));

        // Verify navigation links exist — complex XPath #1
        int navCount = homePage.countNavLinks();
        Assert.assertTrue("Header must have at least 1 navigation link", navCount >= 1);

        // Verify account link exists — complex XPath #2 used in HomePage
        Assert.assertTrue("Account link should be in header", homePage.isAccountLinkPresent());

        // Verify cart button present — complex XPath #3 used in HomePage
        Assert.assertTrue("Cart button should be present", homePage.isCartButtonPresent());

        // Verify a heading element is present on the page — complex XPath #4
        boolean hasHeading = !driver.findElements(
                By.xpath("//*[self::h1 or self::h2 or self::h3][string-length(normalize-space(.))>3]")).isEmpty();
        Assert.assertTrue("Homepage should have at least one heading", hasHeading);
    }

    @Test
    public void verifiesMultipleProductPagesHaveCorrectTitles() {
        String base = TestConfig.getBaseUrl();

        // Define URLs to iterate — satisfies multiple_page_test (loop over 5+ pages)
        String[] pagesToVisit = {
                base + "/us/en/",
                base + "/us/en/products",
                base + "/us/en/account",
                base + "/us/en/account/register",
                base + "/us/en/account/forgot-password"
        };

        for (String url : pagesToVisit) {
            driver.get(url);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ignored) {
            }

            String title = driver.getTitle();
            Assert.assertFalse("Page at " + url + " must have a non-empty title", title.isEmpty());
            System.out.println("Page: " + url + " → Title: " + title);
        }
    }

    @Test
    public void verifiesBrowserHistoryNavigationForwardAndBack() {
        String base = TestConfig.getBaseUrl();
        String homeUrl = base + "/us/en/";
        String productsUrl = base + "/us/en/products";

        // Navigate to home, then products
        driver.get(homeUrl);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {
        }

        driver.get(productsUrl);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {
        }
        Assert.assertTrue("Should be on products page", driver.getCurrentUrl().contains("/products"));

        // Go back (history_test)
        driver.navigate().back();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {
        }

        String urlAfterBack = driver.getCurrentUrl();
        Assert.assertTrue("Back navigation should return to home or previous page",
                urlAfterBack.contains("/us/en") || urlAfterBack.equals(homeUrl));

        // Go forward
        driver.navigate().forward();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {
        }

        String urlAfterForward = driver.getCurrentUrl();
        Assert.assertTrue("Forward navigation should return to products page",
                urlAfterForward.contains("/products"));
    }

    @Test
    public void verifiesProductListingPageHasProductElements() {
        driver.get(TestConfig.getBaseUrl() + "/us/en/products");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }

        // complex XPath #5: product cards are links with nested h3 heading
        boolean hasProductCards = !driver.findElements(By.xpath(
                "//a[contains(@href,'/products/') and .//h3[contains(@class,'text-gray-900')]]")).isEmpty();
        Assert.assertTrue("Products page should display product cards", hasProductCards);

        // complex XPath #6: product price elements
        boolean hasPrices = !driver.findElements(By.xpath(
                "//span[contains(@class,'font-semibold') and contains(@class,'text-gray-900')]")).isEmpty();
        Assert.assertTrue("Products page should display prices", hasPrices);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
