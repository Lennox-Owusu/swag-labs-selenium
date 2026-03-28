package com.amalitech.regression;

import com.amalitech.base.BaseTest;
import com.amalitech.dataprovider.TestDataProvider;
import com.amalitech.pages.CartPage;
import com.amalitech.pages.LoginPage;
import com.amalitech.pages.ProductsPage;
import com.amalitech.testdata.TestData;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Feature("Shopping Cart")
public class CartTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod
    public void loginAndGoToProducts() {
        productsPage = new LoginPage(driver)
                .loginWith(TestData.Users.STANDARD_USER, TestData.Users.PASSWORD);
    }

    @Test(
            dataProvider = "cartProducts",
            dataProviderClass = TestDataProvider.class,
            description = "Adding products to cart should reflect correct item count"
    )
    @Story("Add products to cart")
    @Severity(SeverityLevel.CRITICAL)
    public void addingProductsShouldUpdateCart(String[] productNames, int expectedCount) {
        for (String productName : productNames) {
            productsPage.addProductToCartByName(productName);
        }

        CartPage cartPage = productsPage
                .verifyCartBadgeCount(expectedCount)
                .goToCart();

        cartPage.verifyPageLoaded()
                .verifyCartItemCount(expectedCount);

        for (String productName : productNames) {
            cartPage.verifyCartContains(productName);
        }
    }

    @Test(description = "Removing a product from cart should update cart item count")
    @Story("Remove product from cart")
    @Severity(SeverityLevel.CRITICAL)
    public void removingProductShouldUpdateCartCount() {
        productsPage
                .addProductToCartByName(TestData.Products.BACKPACK)
                .addProductToCartByName(TestData.Products.BIKE_LIGHT)
                .goToCart()
                .verifyPageLoaded()
                .verifyCartItemCount(2)
                .removeProductByName(TestData.Products.BIKE_LIGHT)
                .verifyCartItemCount(1)
                .verifyCartContains(TestData.Products.BACKPACK);
    }

    @Test(description = "Removing all products should leave cart empty")
    @Story("Remove all products")
    @Severity(SeverityLevel.NORMAL)
    public void removingAllProductsShouldEmptyCart() {
        productsPage
                .addProductToCartByName(TestData.Products.BACKPACK)
                .goToCart()
                .verifyPageLoaded()
                .verifyCartItemCount(1)
                .removeProductByName(TestData.Products.BACKPACK)
                .verifyCartIsEmpty();
    }

    @Test(description = "Continue shopping button should return to products page")
    @Story("Continue shopping")
    @Severity(SeverityLevel.NORMAL)
    public void continueShoppingButtonShouldReturnToProductsPage() {
        productsPage
                .addProductToCartByName(TestData.Products.BACKPACK)
                .goToCart()
                .verifyPageLoaded()
                .continueShopping()
                .verifyPageLoaded();
    }
}