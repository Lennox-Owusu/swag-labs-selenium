package com.amalitech.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

import java.time.Duration;

public final class WaitUtil {

    private static final Logger log = LoggerUtil.getLogger(WaitUtil.class);

    private WaitUtil() {}

    // Wait for element to be visible
    public static void waitForVisible(WebDriver driver, WebElement element, int timeoutSecs) {
        log.info("Waiting for element to be visible");
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSecs))
                .until(ExpectedConditions.visibilityOf(element));
    }

    // Wait for element to be clickable
    public static void waitForClickable(WebDriver driver, WebElement element, int timeoutSecs) {
        log.info("Waiting for element to be clickable");
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSecs))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    // Wait for element to contain specific text
    public static void waitForText(WebDriver driver, WebElement element, String text, int timeoutSecs) {
        log.info("Waiting for element to contain text: {}", text);
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSecs))
                .until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    // Wait for element to disappear
    public static void waitForInvisible(WebDriver driver, WebElement element, int timeoutSecs) {
        log.info("Waiting for element to disappear");
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSecs))
                .until(ExpectedConditions.invisibilityOf(element));
    }

}