package com.amalitech.pages;

import com.amalitech.utils.LoggerUtil;
import com.amalitech.utils.WaitUtil;
import com.amalitech.config.AppConfig;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

public class LoginPage {

    private final WebDriver driver;
    private final AppConfig config = AppConfig.get();
    private static final Logger log = LoggerUtil.getLogger(LoginPage.class);

    // ── Locators ──────────────────────────────────────────────────
    @FindBy(id = "user-name")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    // ── Constructor ───────────────────────────────────────────────
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        log.info("LoginPage initialized");
    }

    // ── Actions ───────────────────────────────────────────────────
    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        WaitUtil.waitForVisible(driver, usernameInput, config.explicitWait());
        usernameInput.clear();
        usernameInput.sendKeys(username);
        return this;
    }

    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        WaitUtil.waitForVisible(driver, passwordInput, config.explicitWait());
        passwordInput.clear();
        passwordInput.sendKeys(password);
        return this;
    }

    @Step("Click login button")
    public ProductsPage clickLogin() {
        WaitUtil.waitForClickable(driver, loginButton, config.explicitWait());
        loginButton.click();
        return new ProductsPage(driver);
    }

    @Step("Login with username: {username}")
    public ProductsPage loginWith(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }

    @Step("Login with invalid credentials")
    public LoginPage loginWithInvalidCredentials(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        WaitUtil.waitForClickable(driver, loginButton, config.explicitWait());
        loginButton.click();
        return this;
    }

    // ── Assertions ────────────────────────────────────────────────
    @Step("Verify login page is loaded")
    public void verifyPageLoaded() {
        WaitUtil.waitForVisible(driver, usernameInput, config.explicitWait());
        WaitUtil.waitForVisible(driver, passwordInput, config.explicitWait());
        WaitUtil.waitForVisible(driver, loginButton,   config.explicitWait());
    }

    @Step("Verify error message is visible")
    public LoginPage verifyErrorVisible() {
        WaitUtil.waitForVisible(driver, errorMessage, config.explicitWait());
        return this;
    }

    @Step("Verify error message text: {expectedText}")
    public void verifyErrorText(String expectedText) {
        WaitUtil.waitForText(driver, errorMessage, expectedText, config.explicitWait());
    }
}