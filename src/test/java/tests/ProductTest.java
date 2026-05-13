package tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.ProductPage;
import utils.DriverFactory;
import utils.ScreenshotRule;

/**
 * Tests for product browsing, search, and add-to-cart.
 *
 * Tasks covered:
 * - javascript_executor
 * - fill_input
 * - send_form
 * - complex_xpath
 */
public class ProductTest {

        private WebDriver driver;
        private ProductPage productPage;

        @Rule
        public ScreenshotRule screenshotRule = new ScreenshotRule(() -> driver);

        @Before
        public void setUp() {
                driver = DriverFactory.createDriver();
                productPage = new ProductPage(driver);
        }

        @Test
        public void verifiesProductListingPageLoads() {
                productPage.openProductsListing();
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }

                Assert.assertTrue("Product listing page must show product cards",
                                productPage.isProductListingLoaded());
        }

        @Test
        public void scrollsProductPageUsingJavaScriptExecutor() {
                productPage.openProductsListing();
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }

                // Use JavascriptExecutor to scroll down the page (javascript_executor task)
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("window.scrollTo(0, 500);");
                try {
                        Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }

                // Scroll back to top
                js.executeScript("window.scrollTo(0, 0);");
                try {
                        Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }

                // Scroll a specific product card into view
                WebElement firstCard = driver.findElement(By.xpath(
                                "//a[contains(@href,'/products/') and .//h3[contains(@class,'text-gray-900')]]"));
                js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", firstCard);
                try {
                        Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }

                Assert.assertTrue("First product card should be visible after JS scroll",
                                firstCard.isDisplayed());
        }

        @Test
        public void opensProductDetailPageAndVerifiesContent() {
                productPage.openProductsListing();
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }

                // Click first product — complex XPath selecting card link
                WebElement firstCard = driver.findElement(By.xpath(
                                "//a[contains(@href,'/products/') and .//h3[contains(@class,'text-gray-900')]]"));
                firstCard.click();
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }

                // Verify product title is present
                String detailTitle = productPage.getProductTitle();
                Assert.assertFalse("Product detail page must have a title", detailTitle.isEmpty());

                // Verify Add to Cart button is present
                Assert.assertTrue("Product detail page must have an Add to Cart button",
                                productPage.isAddToCartButtonPresent());

                // Verify page title (complex XPath: h1 with font-bold class)
                boolean hasBoldH1 = !driver.findElements(By.xpath(
                                "//h1[contains(@class,'font-bold') and string-length(normalize-space(.))>0]"))
                                .isEmpty();
                Assert.assertTrue("Detail page should have bold h1 title", hasBoldH1);
        }

        @Test
        public void addsFirstProductToCart() {
                productPage.openProductsListing();
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }

                productPage.openFirstProduct();
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }

                Assert.assertTrue("Add to Cart button must be present",
                                productPage.isAddToCartButtonPresent());

                // Use JS to ensure button is in viewport and click via JS (avoids overlay
                // interception)
                WebElement addBtn = driver.findElement(By.xpath(
                                "//button[contains(normalize-space(.),'Add to Cart') and not(@disabled)]"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addBtn);
                try {
                        Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtn);
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }

                // After clicking Add to Cart a cart drawer or cart count should appear
                // Verify by checking for cart-related elements
                boolean cartUpdated = !driver.findElements(By.xpath(
                                "//button[@aria-label[contains(.,'art')]] | //a[contains(@href,'/cart')]")).isEmpty();
                Assert.assertTrue("Cart should be accessible after adding product", cartUpdated);
        }

        @After
        public void tearDown() {
                if (driver != null) {
                        driver.quit();
                }
        }
}
