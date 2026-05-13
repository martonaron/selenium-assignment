package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.TestConfig;

/**
 * Page object for the login / account page (/us/en/account).
 */
public class LoginPage extends BasePage {

    private static final By EMAIL_INPUT = By.id("email");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By SUBMIT_BUTTON = By.xpath(
            "//button[@type='submit' and (contains(normalize-space(.),'Sign In') or contains(normalize-space(.),'Signing'))]");
    private static final By ERROR_MESSAGE = By
            .xpath("//*[contains(@class,'text-red') or contains(@class,'error') or contains(@role,'alert')]");

    private final String loginUrl;

    public LoginPage(WebDriver driver) {
        super(driver);
        this.loginUrl = TestConfig.getBaseUrl() + "/us/en/account";
    }

    public void open() {
        driver.get(loginUrl);
        waitForVisible(EMAIL_INPUT);
    }

    public void fillEmail(String email) {
        typeIntoField(EMAIL_INPUT, email);
    }

    public void fillPassword(String password) {
        typeIntoField(PASSWORD_INPUT, password);
    }

    public void submitLoginForm() {
        clickElement(SUBMIT_BUTTON);
    }

    public void loginAs(String email, String password) {
        open();
        fillEmail(email);
        fillPassword(password);
        submitLoginForm();
    }

    public boolean isLoggedIn() {
        // After login the account page hides the login form; password input disappears
        try {
            // Wait briefly for React to re-render after login
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
        // If password input (login form) is gone, we're logged in
        boolean loginFormGone = driver.findElements(PASSWORD_INPUT).isEmpty();
        // Or the "Sign Out" button is present (account sidebar)
        boolean signOutPresent = !driver.findElements(
                By.xpath("//button[contains(normalize-space(.),'Sign Out')]")).isEmpty();
        return loginFormGone || signOutPresent;
    }

    public boolean hasErrorMessage() {
        return !driver.findElements(ERROR_MESSAGE).isEmpty();
    }

    public void clickSignOut() {
        // Navigate to account page first, then click Sign Out in the sidebar
        driver.get(loginUrl);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {
        }
        By signOutBtn = By.xpath(
                "//button[contains(normalize-space(.), 'Sign Out') or contains(normalize-space(.), 'Log Out')]");
        waitForClickable(signOutBtn).click();
    }

    public boolean isOnLoginPage() {
        try {
            waitForVisible(EMAIL_INPUT);
            return driver.findElements(By.id("password")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
