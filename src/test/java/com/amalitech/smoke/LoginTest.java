package com.amalitech.smoke;

import com.amalitech.base.BaseTest;
import com.amalitech.dataprovider.TestDataProvider;
import com.amalitech.pages.LoginPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

@Feature("Authentication")
public class LoginTest extends BaseTest {

    @Test(
            dataProvider = "validLoginCredentials",
            dataProviderClass = TestDataProvider.class,
            description = "Valid credentials should land on the products page"
    )
    @Story("Valid login")
    @Severity(SeverityLevel.BLOCKER)
    public void validLoginShouldReachProductsPage(String username, String password) {
        new LoginPage(driver)
                .loginWith(username, password)
                .verifyPageLoaded();
    }

    @Test(
            dataProvider = "invalidLoginCredentials",
            dataProviderClass = TestDataProvider.class,
            description = "Invalid credentials should show an error message"
    )
    @Story("Invalid login")
    @Severity(SeverityLevel.CRITICAL)
    public void invalidLoginShouldShowError(String username, String password, String expectedError) {
        new LoginPage(driver)
                .loginWithInvalidCredentials(username, password)
                .verifyErrorVisible()
                .verifyErrorText(expectedError);
    }

    @Test(description = "Login page should load all required elements")
    @Story("Login page load")
    @Severity(SeverityLevel.NORMAL)
    public void loginPageShouldLoadCorrectly() {
        new LoginPage(driver).verifyPageLoaded();
    }
}