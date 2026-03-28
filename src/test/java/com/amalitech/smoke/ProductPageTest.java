package com.amalitech.smoke;

import com.amalitech.base.BaseTest;
import com.amalitech.dataprovider.TestDataProvider;
import com.amalitech.pages.LoginPage;
import com.amalitech.pages.ProductsPage;
import com.amalitech.testdata.TestData;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Feature("Products")
public class ProductPageTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod
    public void loginAndGoToProducts() {
        productsPage = new LoginPage(driver)
                .loginWith(TestData.Users.STANDARD_USER, TestData.Users.PASSWORD);
    }

    @Test(description = "Products page should load with correct title and all products")
    @Story("Products page load")
    @Severity(SeverityLevel.BLOCKER)
    public void productsPageShouldLoadCorrectly() {
        productsPage
                .verifyPageLoaded()
                .verifyProductCount(TestData.Products.TOTAL_PRODUCT_COUNT);
    }

    @Test(description = "Cart badge should not be visible before adding any product")
    @Story("Cart badge")
    @Severity(SeverityLevel.NORMAL)
    public void cartBadgeShouldNotBeVisibleInitially() {
        productsPage
                .verifyPageLoaded()
                .verifyCartBadgeNotVisible();
    }

    @Test(
            dataProvider = "productNames",
            dataProviderClass = TestDataProvider.class,
            description = "Each product should be visible on the products page"
    )
    @Story("Product listing")
    @Severity(SeverityLevel.CRITICAL)
    public void productShouldBeListedOnPage(String productName) {
        productsPage.verifyProductListed(productName);
    }

    @Test(
            dataProvider = "productDetails",
            dataProviderClass = TestDataProvider.class,
            description = "Product details page should show correct name and price"
    )
    @Story("Product details")
    @Severity(SeverityLevel.CRITICAL)
    public void productDetailsShouldShowCorrectInfo(String productName, String productPrice) {
        productsPage
                .openProductDetails(productName)
                .verifyPageLoaded()
                .verifyProductName(productName)
                .verifyProductPrice(productPrice);
    }

    @Test(
            dataProvider = "productNamesWithDescription",
            dataProviderClass = TestDataProvider.class,
            description = "Product details page should show correct description"
    )
    @Story("Product details")
    @Severity(SeverityLevel.NORMAL)
    public void productDetailsShouldShowCorrectDescription(String productName,
                                                           String productDescription) {
        productsPage
                .openProductDetails(productName)
                .verifyPageLoaded()
                .verifyProductDescription(productDescription);
    }

    @Test(
            dataProvider = "productNames",
            dataProviderClass = TestDataProvider.class,
            description = "Adding a product to cart should increment the cart badge"
    )
    @Story("Add to cart")
    @Severity(SeverityLevel.CRITICAL)
    public void addingProductShouldIncrementCartBadge(String productName) {
        productsPage
                .addProductToCartByName(productName)
                .verifyCartBadgeCount(1);
    }

    @Test(description = "Adding product from details page should update cart badge")
    @Story("Product details add to cart")
    @Severity(SeverityLevel.CRITICAL)
    public void addingFromDetailsShouldUpdateCartBadge() {
        productsPage
                .openProductDetails(TestData.Products.BACKPACK)
                .verifyPageLoaded()
                .addToCart()
                .verifyCartBadgeCount(1);
    }

    @Test(description = "Cart icon on details page should navigate to cart")
    @Story("Product details navigation")
    @Severity(SeverityLevel.NORMAL)
    public void cartIconOnDetailsShouldNavigateToCart() {
        productsPage
                .openProductDetails(TestData.Products.BACKPACK)
                .addToCart()
                .goToCart()
                .verifyPageLoaded()
                .verifyCartContains(TestData.Products.BACKPACK);
    }

    @Test(description = "Back button on details page should return to products page")
    @Story("Product details navigation")
    @Severity(SeverityLevel.NORMAL)
    public void backButtonShouldReturnToProductsPage() {
        productsPage
                .openProductDetails(TestData.Products.BACKPACK)
                .verifyPageLoaded()
                .goBackToProducts()
                .verifyPageLoaded();
    }
}