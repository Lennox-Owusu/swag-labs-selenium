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

public class ProductDetailsPage {

    private final WebDriver driver;
    private final AppConfig config = AppConfig.get();
    private static final Logger log = LoggerUtil.getLogger(ProductDetailsPage.class);

    // ── Locators ──────────────────────────────────────────────────
    @FindBy(css = "[data-test='inventory-item-name']")
    private WebElement productName;

    @FindBy(css = "[data-test='inventory-item-desc']")
    private WebElement productDescription;

    @FindBy(css = "[data-test='inventory-item-price']")
    private WebElement productPrice;

    @FindBy(css = "[data-test^='add-to-cart']")
    private WebElement addToCartButton;

    @FindBy(css = "[data-test='back-to-products']")
    private WebElement backButton;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartIcon;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    // ── Constructor ───────────────────────────────────────────────
    public ProductDetailsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        log.info("ProductDetailsPage initialized");
    }

    // ── Actions ───────────────────────────────────────────────────
    @Step("Add product to cart from details page")
    public ProductDetailsPage addToCart() {
        WaitUtil.waitForClickable(driver, addToCartButton, config.explicitWait());
        addToCartButton.click();
        return this;
    }

    @Step("Go back to products page")
    public ProductsPage goBackToProducts() {
        WaitUtil.waitForClickable(driver, backButton, config.explicitWait());
        backButton.click();
        return new ProductsPage(driver);
    }

    @Step("Go to cart from details page")
    public CartPage goToCart() {
        WaitUtil.waitForClickable(driver, cartIcon, config.explicitWait());
        cartIcon.click();
        return new CartPage(driver);
    }

    // ── Assertions ────────────────────────────────────────────────
    @Step("Verify product details page is loaded")
    public ProductDetailsPage verifyPageLoaded() {
        WaitUtil.waitForVisible(driver, productName,        config.explicitWait());
        WaitUtil.waitForVisible(driver, productDescription, config.explicitWait());
        WaitUtil.waitForVisible(driver, productPrice,       config.explicitWait());
        return this;
    }

    @Step("Verify product name: {expectedName}")
    public ProductDetailsPage verifyProductName(String expectedName) {
        WaitUtil.waitForText(driver, productName, expectedName, config.explicitWait());
        return this;
    }

    @Step("Verify product price: {expectedPrice}")
    public void verifyProductPrice(String expectedPrice) {
        WaitUtil.waitForText(driver, productPrice, expectedPrice, config.explicitWait());
    }

    @Step("Verify product description: {expectedDescription}")
    public void verifyProductDescription(String expectedDescription) {
        WaitUtil.waitForText(driver, productDescription, expectedDescription, config.explicitWait());
    }

    @Step("Verify cart badge count: {expectedCount}")
    public void verifyCartBadgeCount(int expectedCount) {
        WaitUtil.waitForVisible(driver, cartBadge, config.explicitWait());
        assert cartBadge.getText().equals(String.valueOf(expectedCount));
    }
}