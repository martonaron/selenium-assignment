package tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;
import pages.RegisterPage;
import pages.AccountPage;
import utils.DriverFactory;
import utils.RandomDataGenerator;
import utils.ScreenshotRule;

/**
 * Tests authentication: registration, login, profile update (form_with_user),
 * and logout.
 *
 * Tasks covered:
 * - login_form
 * - form_with_user
 * - logout
 * - random_data
 * - page_title
 * - screenshot_on_failure
 * - fill_input
 * - send_form
 */
public class AuthTest {

    private WebDriver driver;
    private LoginPage loginPage;
    private RegisterPage registerPage;
    private AccountPage accountPage;

    // Credentials generated once per test method (each test is independent)
    private String testEmail;
    private String testPassword;
    private String testFirstName;
    private String testLastName;

    @Rule
    public TestName testName = new TestName();

    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule(() -> driver);

    @Before
    public void setUp() {
        driver = DriverFactory.createDriver();
        loginPage = new LoginPage(driver);
        registerPage = new RegisterPage(driver);
        accountPage = new AccountPage(driver);

        testEmail = RandomDataGenerator.generateEmail();
        testPassword = RandomDataGenerator.generatePassword();
        testFirstName = RandomDataGenerator.generateFirstName();
        testLastName = RandomDataGenerator.generateLastName();
    }

    @Test
    public void registersNewUserWithRandomData() {
        registerPage.registerNewUser(testFirstName, testLastName, testEmail, testPassword);
        Assert.assertTrue("Registration should redirect away from /register page",
                registerPage.isRegistrationSuccessful());
    }

    @Test
    public void logsInWithValidCredentials() {
        // First register so the account exists (registration auto-logs in)
        registerPage.registerNewUser(testFirstName, testLastName, testEmail, testPassword);
        Assert.assertTrue("Registration must succeed before login test",
                registerPage.isRegistrationSuccessful());

        // Log out via the account sidebar Sign Out button
        loginPage.clickSignOut();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }

        // Now open the login page — should show the login form
        loginPage.open();
        String titleBeforeLogin = loginPage.getPageTitle();
        Assert.assertFalse("Login page should have a title", titleBeforeLogin.isEmpty());

        loginPage.fillEmail(testEmail);
        loginPage.fillPassword(testPassword);
        loginPage.submitLoginForm();

        // After login the URL should move away from /account (no login form)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
        Assert.assertTrue("After login, user should be on an account-related page",
                driver.getCurrentUrl().contains("/account"));
        Assert.assertTrue("After login, login form should no longer be visible",
                loginPage.isLoggedIn());
    }

    @Test
    public void updatesUserProfileWhileLoggedIn() {
        // Register + auto-login
        registerPage.registerNewUser(testFirstName, testLastName, testEmail, testPassword);
        Assert.assertTrue("Registration must succeed", registerPage.isRegistrationSuccessful());

        // Navigate to profile page
        accountPage.open();
        Assert.assertTrue("Profile page should be accessible after login",
                accountPage.isProfilePageLoaded());

        // Update first and last name
        String newFirst = "Updated" + RandomDataGenerator.generateFirstName();
        String newLast = "Updated" + RandomDataGenerator.generateLastName();
        accountPage.fillFirstName(newFirst);
        accountPage.fillLastName(newLast);
        accountPage.saveProfileChanges();

        // Verify the field retained the new value (or page reloaded with it)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
        accountPage.open();
        String savedFirst = accountPage.getFirstNameValue();
        Assert.assertEquals("First name should be updated", newFirst, savedFirst);
    }

    @Test
    public void logsOutSuccessfully() {
        // Register + auto-login
        registerPage.registerNewUser(testFirstName, testLastName, testEmail, testPassword);
        Assert.assertTrue("Registration must succeed", registerPage.isRegistrationSuccessful());

        // Click Sign Out from the account sidebar
        loginPage.clickSignOut();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }

        // After logout, the account page should show the login form (not a profile)
        Assert.assertTrue("After logout, account page should show login form",
                loginPage.isOnLoginPage());
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
