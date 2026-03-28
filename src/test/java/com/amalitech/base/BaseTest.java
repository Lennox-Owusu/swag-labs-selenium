package com.amalitech.base;

import com.amalitech.config.AppConfig;
import com.amalitech.utils.LoggerUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected final AppConfig config = AppConfig.get();
    private static final Logger log = LoggerUtil.getLogger(BaseTest.class);

    @BeforeMethod
    public void setUp(Method method) {
        log.info("Starting test: {}", method.getName());
        log.info("Browser: Chrome | Headless: {}", config.headless());

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        if (config.headless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--window-size=1920,1080",
                "--remote-allow-origins=*"
        );

        driver = new ChromeDriver(options);

        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(config.implicitWait()));
        driver.manage().timeouts()
                .pageLoadTimeout(Duration.ofSeconds(config.pageLoadTimeout()));
        driver.manage().window().maximize();

        driver.get(config.baseUrl());
        log.info("Browser opened at: {}", config.baseUrl());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("Test FAILED: {}", result.getName());
            log.error("Failure reason: {}", result.getThrowable().getMessage());
            takeScreenshot(result.getName());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            log.info("Test PASSED: {}", result.getName());
        } else {
            log.warn("Test SKIPPED: {}", result.getName());
        }

        if (driver != null) {
            driver.quit();
            log.info("Browser closed after test: {}", result.getName());
        }
    }

    @Attachment(value = "Screenshot on failure: {testName}", type = "image/png")
    private void takeScreenshot(String testName) {
        log.info("Taking screenshot for failed test: {}", testName);
        ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}