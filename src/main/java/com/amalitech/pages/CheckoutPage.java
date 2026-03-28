package com.amalitech.pages;

import com.amalitech.config.AppConfig;
import com.amalitech.utils.LoggerUtil;
import com.amalitech.utils.WaitUtil;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

public class CheckoutPage {

    private final WebDriver driver;
    private final AppConfig config = AppConfig.get();
    private static final Logger log = LoggerUtil.getLogger(CheckoutPage.class);

    // ── Step One ──────────────────────────────────────────────────
    @FindBy(css = "[data-test='firstName']")
    private WebElement firstNameInput;

    @FindBy(css = "[data-test='lastName']")
    private WebElement lastNameInput;

    @FindBy(css = "[data-test='postalCode']")
    private WebElement postalCodeInput;

    @FindBy(css = "[data-test='continue']")
    private WebElement continueButton;

    @FindBy(css = "[data-test='cancel']")
    private WebElement cancelButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    // ── Step Two ──────────────────────────────────────────────────
    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(css = ".summary_subtotal_label")
    private WebElement subtotalLabel;

    @FindBy(css = "[data-test='finish']")
    private WebElement finishButton;

    // ── Step Three ────────────────────────────────────────────────
    @FindBy(css = ".complete-header")
    private WebElement confirmationHeader;

    @FindBy(css = "[data-test='back-to-products']")
    private WebElement backHomeButton;

    // ── Constructor ───────────────────────────────────────────────
    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        log.info("CheckoutPage initialized");
    }

    // ── Actions ───────────────────────────────────────────────────
    @Step("Fill customer info")
    public CheckoutPage fillCustomerInfo(String firstName, String lastName, String postalCode) {
        WaitUtil.waitForVisible(driver, firstNameInput, config.explicitWait());
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);
        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);
        postalCodeInput.clear();
        postalCodeInput.sendKeys(postalCode);
        return this;
    }

    @Step("Click continue")
    public CheckoutPage clickContinue() {
        WaitUtil.waitForClickable(driver, continueButton, config.explicitWait());
        continueButton.click();
        return this;
    }

    @Step("Cancel checkout")
    public CartPage cancelCheckout() {
        WaitUtil.waitForClickable(driver, cancelButton, config.explicitWait());
        cancelButton.click();
        return new CartPage(driver);
    }

    @Step("Back to products after order")
    public ProductsPage backToProducts() {
        WaitUtil.waitForClickable(driver, backHomeButton, config.explicitWait());
        backHomeButton.click();
        return new ProductsPage(driver);
    }

    // ── Assertions ────────────────────────────────────────────────
    @Step("Verify step one is loaded")
    public CheckoutPage verifyStepOneLoaded() {
        WaitUtil.waitForVisible(driver, firstNameInput, config.explicitWait());
        WaitUtil.waitForVisible(driver, lastNameInput,  config.explicitWait());
        WaitUtil.waitForVisible(driver, postalCodeInput,config.explicitWait());
        return this;
    }

    @Step("Verify order summary is loaded")
    public CheckoutPage verifySummaryLoaded() {
        WaitUtil.waitForVisible(driver, pageTitle, config.explicitWait());
        WaitUtil.waitForText(driver, pageTitle, "Checkout: Overview", config.explicitWait());
        WaitUtil.waitForVisible(driver, finishButton, config.explicitWait());
        return this;
    }

    @Step("Click finish")
    public CheckoutPage clickFinish() {
        WaitUtil.waitForVisible(driver, finishButton, config.explicitWait());
        WaitUtil.waitForClickable(driver, finishButton, config.explicitWait());
        finishButton.click();
        return this;
    }

    @Step("Verify order confirmed")
    public CheckoutPage verifyOrderConfirmed() {
        WaitUtil.waitForVisible(driver, confirmationHeader, config.explicitWait());
        WaitUtil.waitForText(driver, confirmationHeader, "Thank you for your order!", config.explicitWait());
        return this;
    }

    @Step("Verify error message: {expectedText}")
    public void verifyErrorText(String expectedText) {
        WaitUtil.waitForVisible(driver, errorMessage, config.explicitWait());
        WaitUtil.waitForText(driver, errorMessage, expectedText, config.explicitWait());
    }

    @Step("Verify subtotal contains: {expectedSubtotal}")
    public void verifySubtotal(String expectedSubtotal) {
        WaitUtil.waitForVisible(driver, subtotalLabel, config.explicitWait());
        assert subtotalLabel.getText().contains(expectedSubtotal);
    }
}