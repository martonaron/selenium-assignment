package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.TestConfig;

import java.util.List;

/**
 * Page object for the products listing page (/us/en/products).
 */
public class ProductPage extends BasePage {

    // Product listing selectors
    private static final By PRODUCT_CARDS = By.xpath(
            "//a[contains(@href,'/products/') and .//h3[contains(@class,'text-gray-900')]]");

    // Product detail selectors
    private static final By PRODUCT_TITLE = By.xpath("//h1[contains(@class,'font-bold')]");
    private static final By ADD_TO_CART_BTN = By
            .xpath("//button[contains(normalize-space(.),'Add to Cart') and not(@disabled)]");

    // Search
    private static final By SEARCH_INPUT = By.xpath("//input[@role='combobox' or @type='search']");
    private static final By SEARCH_RESULTS = By.id("search-suggestions");

    private final String productsUrl;

    public ProductPage(WebDriver driver) {
        super(driver);
        this.productsUrl = TestConfig.getBaseUrl() + "/us/en/products";
    }

    public void openProductsListing() {
        driver.get(productsUrl);
        waitForVisible(By.tagName("main"));
    }

    public List<WebElement> getProductCards() {
        return driver.findElements(PRODUCT_CARDS);
    }

    public void openFirstProduct() {
        List<WebElement> cards = getProductCards();
        if (!cards.isEmpty()) {
            cards.get(0).click();
        }
    }

    public void openProductByIndex(int index) {
        List<WebElement> cards = getProductCards();
        if (index < cards.size()) {
            scrollIntoView(cards.get(index));
            cards.get(index).click();
        }
    }

    public String getProductTitle() {
        return waitForVisible(PRODUCT_TITLE).getText();
    }

    public boolean isAddToCartButtonPresent() {
        return !driver.findElements(ADD_TO_CART_BTN).isEmpty();
    }

    public void addProductToCart() {
        WebElement btn = waitForClickable(ADD_TO_CART_BTN);
        scrollIntoView(btn);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }
        // Use JavaScript click to bypass any overlay interception
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public void scrollToProductDetails() {
        // Use JavaScript executor to scroll (covers javascript_executor task)
        WebElement main = waitForVisible(By.tagName("main"));
        scrollIntoView(main);
        scrollToBottom();
    }

    public void openSearchOverlay() {
        // Click the search toggle button in the header
        By searchToggle = By.xpath("//button[@aria-controls='search-overlay' or @aria-label[contains(.,'earch')]]");
        waitForClickable(searchToggle).click();
        waitForVisible(SEARCH_INPUT);
    }

    public void searchFor(String keyword) {
        WebElement input = waitForVisible(SEARCH_INPUT);
        input.clear();
        input.sendKeys(keyword);
    }

    public boolean hasSearchResults() {
        try {
            waitForVisible(SEARCH_RESULTS);
            return !driver.findElements(By.xpath("//ul[@id='search-suggestions']/li")).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isProductListingLoaded() {
        return !driver.findElements(PRODUCT_CARDS).isEmpty();
    }
}
