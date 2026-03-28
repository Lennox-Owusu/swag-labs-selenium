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

import java.util.List;

public class CartPage {

    private final WebDriver driver;
    private final AppConfig config = AppConfig.get();
    private static final Logger log = LoggerUtil.getLogger(CartPage.class);

    // ── Locators ──────────────────────────────────────────────────
    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(css = "[data-test='checkout']")
    private WebElement checkoutButton;

    @FindBy(css = "[data-test='continue-shopping']")
    private WebElement continueShoppingButton;

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> itemNames;

    // ── Constructor ───────────────────────────────────────────────
    public CartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        log.info("CartPage initialized");
    }

    // ── Actions ───────────────────────────────────────────────────
    @Step("Remove product from cart: {productName}")
    public CartPage removeProductByName(String productName) {
        cartItems.stream()
                .filter(item -> item.findElement(
                                org.openqa.selenium.By.cssSelector(".inventory_item_name"))
                        .getText().equals(productName))
                .findFirst()
                .ifPresent(item -> {
                    WebElement removeBtn = item.findElement(
                            org.openqa.selenium.By.cssSelector("[data-test^='remove']"));
                    WaitUtil.waitForClickable(driver, removeBtn, config.explicitWait());
                    removeBtn.click();
                    WaitUtil.waitForInvisible(driver, item, config.explicitWait());
                });
        return this;
    }

    @Step("Proceed to checkout")
    public CheckoutPage proceedToCheckout() {
        WaitUtil.waitForClickable(driver, checkoutButton, config.explicitWait());
        checkoutButton.click();
        return new CheckoutPage(driver);
    }

    @Step("Continue shopping")
    public ProductsPage continueShopping() {
        WaitUtil.waitForClickable(driver, continueShoppingButton, config.explicitWait());
        continueShoppingButton.click();
        return new ProductsPage(driver);
    }

    // ── Assertions ────────────────────────────────────────────────
    @Step("Verify cart page is loaded")
    public CartPage verifyPageLoaded() {
        WaitUtil.waitForVisible(driver, pageTitle, config.explicitWait());
        assert pageTitle.getText().equals("Your Cart");
        return this;
    }

    @Step("Verify cart contains: {productName}")
    public CartPage verifyCartContains(String productName) {
        boolean found = itemNames.stream()
                .anyMatch(el -> el.getText().equals(productName));
        assert found : "Product not in cart: " + productName;
        return this;
    }

    @Step("Verify cart item count: {expectedCount}")
    public CartPage verifyCartItemCount(int expectedCount) {
        assert cartItems.size() == expectedCount
                : "Expected " + expectedCount + " items but found " + cartItems.size();
        return this;
    }

    @Step("Verify cart is empty")
    public void verifyCartIsEmpty() {
        assert cartItems.isEmpty() : "Cart is not empty";
    }
}