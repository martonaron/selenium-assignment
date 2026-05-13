package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utils.TestConfig;

/**
 * Page object for the checkout flow.
 * Covers: form_with_user, dropdown, radio_button tasks.
 */
public class CheckoutPage extends BasePage {

    // Contact section
    private static final By CONTACT_EMAIL = By.id("email");

    // Shipping address fields
    private static final By SHIP_FIRST_NAME = By.id("ship-first_name");
    private static final By SHIP_LAST_NAME = By.id("ship-last_name");
    private static final By SHIP_ADDRESS1 = By.id("ship-address1");
    private static final By SHIP_CITY = By.id("ship-city");
    private static final By SHIP_POSTAL = By.id("ship-postal_code");
    private static final By SHIP_PHONE = By.id("ship-phone");
    private static final By SHIP_COUNTRY = By.id("ship-country");

    // Shipping method radio buttons
    private static final By SHIPPING_RADIO = By.xpath(
            "//label[.//*[@data-slot='radio-group-item']]");
    private static final By SHIPPING_RADIO_ITEM = By.xpath(
            "//*[@data-slot='radio-group-item']");

    // Continue / Save buttons
    private static final By CONTINUE_BTN = By.xpath(
            "//button[contains(normalize-space(.),'Continue') or contains(normalize-space(.),'Save') or contains(normalize-space(.),'Next')]");

    // Cart and checkout
    private static final By CHECKOUT_BTN = By.xpath(
            "//a[contains(normalize-space(.),'Checkout')] | //button[contains(normalize-space(.),'Checkout')]");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void openCartAndProceedToCheckout() {
        driver.get(TestConfig.getBaseUrl() + "/us/en/cart");
        waitForVisible(By.tagName("main"));
        waitForClickable(CHECKOUT_BTN).click();
    }

    public boolean isCheckoutPageLoaded() {
        return driver.getCurrentUrl().contains("/checkout");
    }

    public void fillContactEmail(String email) {
        try {
            WebElement emailField = waitForVisible(CONTACT_EMAIL);
            emailField.clear();
            emailField.sendKeys(email);
        } catch (Exception ignored) {
            // Contact email field may not appear when logged in
        }
    }

    public void fillShippingFirstName(String firstName) {
        typeIntoField(SHIP_FIRST_NAME, firstName);
    }

    public void fillShippingLastName(String lastName) {
        typeIntoField(SHIP_LAST_NAME, lastName);
    }

    public void fillShippingAddress(String address) {
        typeIntoField(SHIP_ADDRESS1, address);
    }

    public void fillShippingCity(String city) {
        typeIntoField(SHIP_CITY, city);
    }

    public void fillShippingPostalCode(String postalCode) {
        typeIntoField(SHIP_POSTAL, postalCode);
    }

    public void fillShippingPhone(String phone) {
        typeIntoField(SHIP_PHONE, phone);
    }

    /**
     * Selects a country from the native <select> dropdown — covers dropdown task.
     */
    public void selectShippingCountry(String countryText) {
        WebElement selectElement = waitForVisible(SHIP_COUNTRY);
        scrollIntoView(selectElement);
        Select dropdown = new Select(selectElement);
        dropdown.selectByVisibleText(countryText);
    }

    /**
     * Clicks the first available shipping method radio — covers radio_button task.
     */
    public void selectFirstShippingMethod() {
        waitForVisible(SHIPPING_RADIO_ITEM);
        WebElement firstRadio = driver.findElements(SHIPPING_RADIO).get(0);
        scrollIntoView(firstRadio);
        firstRadio.click();
    }

    public boolean hasShippingMethodOptions() {
        return !driver.findElements(SHIPPING_RADIO_ITEM).isEmpty();
    }

    public void clickContinue() {
        waitForClickable(CONTINUE_BTN).click();
    }

    public void clickSaveAddress() {
        By saveAddressBtn = By.xpath("//button[@type='submit' and not(@disabled)]");
        waitForClickable(saveAddressBtn).click();
    }

    public void clickCheckoutButtonOnCart() {
        By checkoutBtn = By.xpath(
                "//a[contains(normalize-space(.),'Checkout')] | //button[contains(normalize-space(.),'Checkout')]");
        waitForClickable(checkoutBtn).click();
    }

    public void fillCompleteShippingAddress(String email, String firstName, String lastName,
            String address, String city, String postalCode, String phone) {
        fillContactEmail(email);
        fillShippingFirstName(firstName);
        fillShippingLastName(lastName);
        fillShippingAddress(address);
        fillShippingCity(city);
        fillShippingPostalCode(postalCode);
        fillShippingPhone(phone);
    }
}
