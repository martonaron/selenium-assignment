package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.TestConfig;

/**
 * Page object for account profile page (/us/en/account/profile).
 */
public class AccountPage extends BasePage {

    private static final By FIRST_NAME_INPUT = By.id("first_name");
    private static final By LAST_NAME_INPUT = By.id("last_name");
    private static final By EMAIL_INPUT = By.id("email");
    private static final By SAVE_BUTTON = By.xpath("//button[@type='submit']");

    private final String profileUrl;

    public AccountPage(WebDriver driver) {
        super(driver);
        this.profileUrl = TestConfig.getBaseUrl() + "/us/en/account/profile";
    }

    public void open() {
        driver.get(profileUrl);
        waitForVisible(FIRST_NAME_INPUT);
    }

    public void fillFirstName(String firstName) {
        typeIntoField(FIRST_NAME_INPUT, firstName);
    }

    public void fillLastName(String lastName) {
        typeIntoField(LAST_NAME_INPUT, lastName);
    }

    public void fillEmail(String email) {
        typeIntoField(EMAIL_INPUT, email);
    }

    public void saveProfileChanges() {
        WebElement saveBtn = waitForClickable(SAVE_BUTTON);
        scrollIntoView(saveBtn);
        saveBtn.click();
    }

    public boolean isProfilePageLoaded() {
        return !driver.findElements(FIRST_NAME_INPUT).isEmpty();
    }

    public String getFirstNameValue() {
        return waitForVisible(FIRST_NAME_INPUT).getAttribute("value");
    }

    public String getLastNameValue() {
        return waitForVisible(LAST_NAME_INPUT).getAttribute("value");
    }
}
