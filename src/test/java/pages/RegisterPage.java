package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.TestConfig;

/**
 * Page object for the registration page (/us/en/account/register).
 */
public class RegisterPage extends BasePage {

    private static final By FIRST_NAME_INPUT = By.id("firstName");
    private static final By LAST_NAME_INPUT = By.id("lastName");
    private static final By EMAIL_INPUT = By.id("email");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By PASSWORD_CONFIRM_INPUT = By.id("passwordConfirmation");
    private static final By POLICY_CONSENT_CHECKBOX = By.id("policy-consent");
    private static final By SUBMIT_BUTTON = By.xpath(
            "//button[@type='submit' and (contains(normalize-space(.),'Create Account') or contains(normalize-space(.),'Creating'))]");

    private final String registerUrl;

    public RegisterPage(WebDriver driver) {
        super(driver);
        this.registerUrl = TestConfig.getBaseUrl() + "/us/en/account/register";
    }

    public void open() {
        driver.get(registerUrl);
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

    public void fillPassword(String password) {
        typeIntoField(PASSWORD_INPUT, password);
    }

    public void fillPasswordConfirmation(String password) {
        typeIntoField(PASSWORD_CONFIRM_INPUT, password);
    }

    public void submitRegistrationForm() {
        // Scroll to and check the policy consent checkbox (required for form
        // submission)
        try {
            WebElement checkbox = waitForClickable(POLICY_CONSENT_CHECKBOX);
            scrollIntoView(checkbox);
            checkbox.click();
            Thread.sleep(300);
        } catch (Exception e) {
            System.err.println("Policy consent checkbox interaction failed: " + e.getMessage());
        }
        WebElement submitBtn = waitForClickable(SUBMIT_BUTTON);
        scrollIntoView(submitBtn);
        submitBtn.click();
    }

    public void registerNewUser(String firstName, String lastName, String email, String password) {
        open();
        fillFirstName(firstName);
        fillLastName(lastName);
        fillEmail(email);
        fillPassword(password);
        fillPasswordConfirmation(password);
        submitRegistrationForm();
    }

    public boolean isRegistrationSuccessful() {
        try {
            // Wait until the URL no longer contains /register (redirect to account page)
            wait.until(driver -> !driver.getCurrentUrl().contains("/register"));
            return !driver.getCurrentUrl().contains("/register");
        } catch (Exception e) {
            return false;
        }
    }
}
