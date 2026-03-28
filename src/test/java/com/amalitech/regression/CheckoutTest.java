package com.amalitech.regression;

import com.amalitech.base.BaseTest;
import com.amalitech.dataprovider.TestDataProvider;
import com.amalitech.pages.CartPage;
import com.amalitech.pages.LoginPage;
import com.amalitech.testdata.TestData;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Feature("Checkout")
public class CheckoutTest extends BaseTest {

    private CartPage cartPage;

    @BeforeMethod
    public void loginAndAddProductToCart() {
        cartPage = new LoginPage(driver)
                .loginWith(TestData.Users.STANDARD_USER, TestData.Users.PASSWORD)
                .addProductToCartByName(TestData.Products.BACKPACK)
                .goToCart();
    }

    @Test(
            dataProvider = "validCheckoutInfo",
            dataProviderClass = TestDataProvider.class,
            description = "Valid checkout info should complete order successfully"
    )
    @Story("Successful checkout")
    @Severity(SeverityLevel.BLOCKER)
    public void validCheckoutShouldCompleteOrder(String firstName,
                                                 String lastName,
                                                 String postalCode) {
        cartPage.verifyPageLoaded()
                .verifyCartContains(TestData.Products.BACKPACK)
                .proceedToCheckout()
                .verifyStepOneLoaded()
                .fillCustomerInfo(firstName, lastName, postalCode)
                .clickContinue()
                .verifySummaryLoaded()
                .clickFinish()
                .verifyOrderConfirmed();
    }

    @Test(description = "Order summary should show correct product and price")
    @Story("Order summary")
    @Severity(SeverityLevel.CRITICAL)
    public void orderSummaryShouldShowCorrectProductAndPrice() {
        cartPage.proceedToCheckout()
                .fillCustomerInfo(
                        TestData.Checkout.FIRST_NAME,
                        TestData.Checkout.LAST_NAME,
                        TestData.Checkout.POSTAL_CODE)
                .clickContinue()
                .verifySummaryLoaded()
                .verifySubtotal(TestData.Products.BACKPACK_PRICE);
    }

    @Test(
            dataProvider = "invalidCheckoutInfo",
            dataProviderClass = TestDataProvider.class,
            description = "Missing checkout fields should show correct error message"
    )
    @Story("Checkout validation")
    @Severity(SeverityLevel.CRITICAL)
    public void missingCheckoutFieldShouldShowError(String firstName,
                                                    String lastName,
                                                    String postalCode,
                                                    String expectedError) {
        cartPage.proceedToCheckout()
                .verifyStepOneLoaded()
                .fillCustomerInfo(firstName, lastName, postalCode)
                .clickContinue()
                .verifyErrorText(expectedError);
    }

    @Test(description = "Cancelling checkout should return to cart")
    @Story("Cancel checkout")
    @Severity(SeverityLevel.NORMAL)
    public void cancelCheckoutShouldReturnToCart() {
        cartPage.proceedToCheckout()
                .verifyStepOneLoaded()
                .cancelCheckout()
                .verifyPageLoaded()
                .verifyCartContains(TestData.Products.BACKPACK);
    }

    @Test(description = "Back to products after order confirmation should work")
    @Story("Post order navigation")
    @Severity(SeverityLevel.NORMAL)
    public void backToProductsAfterOrderShouldWork() {
        cartPage.proceedToCheckout()
                .fillCustomerInfo(
                        TestData.Checkout.FIRST_NAME,
                        TestData.Checkout.LAST_NAME,
                        TestData.Checkout.POSTAL_CODE)
                .clickContinue()
                .clickFinish()
                .verifyOrderConfirmed()
                .backToProducts()
                .verifyPageLoaded();
    }
}