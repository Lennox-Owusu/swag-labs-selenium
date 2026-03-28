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

public class ProductsPage {

    private final WebDriver driver;
    private final AppConfig config = AppConfig.get();
    private static final Logger log = LoggerUtil.getLogger(ProductsPage.class);

    // ── Locators ──────────────────────────────────────────────────
    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartIcon;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> productNames;

    @FindBy(css = ".inventory_item")
    private List<WebElement> productItems;

    // ── Constructor ───────────────────────────────────────────────
    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        log.info("ProductsPage initialized");
    }

    // ── Actions ───────────────────────────────────────────────────
    @Step("Add product to cart: {productName}")
    public ProductsPage addProductToCartByName(String productName) {
        productNames.stream()
                .filter(el -> el.getText().equals(productName))
                .findFirst()
                .ifPresent(el -> {
                    WebElement addBtn = el
                            .findElement(org.openqa.selenium.By.xpath(
                                    "./ancestor::div[@class='inventory_item']"
                                            + "/descendant::button"));
                    WaitUtil.waitForClickable(driver, addBtn, config.explicitWait());
                    addBtn.click();
                });
        return this;
    }

    @Step("Open product details: {productName}")
    public ProductDetailsPage openProductDetails(String productName) {
        productNames.stream()
                .filter(el -> el.getText().equals(productName))
                .findFirst()
                .ifPresent(el -> {
                    WaitUtil.waitForClickable(driver, el, config.explicitWait());
                    el.click();
                });
        return new ProductDetailsPage(driver);
    }

    @Step("Go to cart")
    public CartPage goToCart() {
        WaitUtil.waitForClickable(driver, cartIcon, config.explicitWait());
        cartIcon.click();
        return new CartPage(driver);
    }

    // ── Assertions ────────────────────────────────────────────────
    @Step("Verify products page is loaded")
    public ProductsPage verifyPageLoaded() {
        WaitUtil.waitForVisible(driver, pageTitle, config.explicitWait());
        assert pageTitle.getText().equals("Products");
        return this;
    }

    @Step("Verify product count is: {expectedCount}")
    public void verifyProductCount(int expectedCount) {
        assert productItems.size() == expectedCount;
    }

    @Step("Verify cart badge count is: {expectedCount}")
    public ProductsPage verifyCartBadgeCount(int expectedCount) {
        WaitUtil.waitForVisible(driver, cartBadge, config.explicitWait());
        WaitUtil.waitForText(driver, cartBadge, String.valueOf(expectedCount), config.explicitWait());
        return this;
    }

    @Step("Verify cart badge is not visible")
    public void verifyCartBadgeNotVisible() {
        WaitUtil.waitForInvisible(driver, cartBadge, config.explicitWait());
    }

    @Step("Verify product is listed: {productName}")
    public void verifyProductListed(String productName) {
        boolean found = productNames.stream()
                .anyMatch(el -> el.getText().equals(productName));
        assert found : "Product not found: " + productName;
    }
}