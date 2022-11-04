package org.netcompany;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Main {

    private static final String LINK = SettingsService.getVariable(Setting.LINK);
    private static final String SIGN_UP_BUTTON_CLASS_NAME = SettingsService.getVariable(Setting.SIGN_UP_BUTTON_CLASS_NAME);
    private static final String CONFIRM_BUTTON_CLASS_NAME = SettingsService.getVariable(Setting.CONFIRM_BUTTON_CLASS_NAME);
    private static final String SIGNED_UP_DIV_CLASS = SettingsService.getVariable(Setting.SIGNED_UP_DIV_CLASS);
    private static final int TIMEOUT_SECONDS = Integer.parseInt(SettingsService.getVariable(Setting.TIMEOUT_SECONDS));

    private static final int IMPLICITLY_TIMEOUT_SECONDS = 20;

    public static void main(String...args) throws InterruptedException {
        WebDriverManager.chromedriver().setup();

        WebDriver driver  = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_TIMEOUT_SECONDS));
        driver.manage().window().maximize();

        boolean signedUpForEvent = false;
        while (!signedUpForEvent) {
            driver.navigate().to(LINK);

            try {
                WebElement signUpButton = new WebDriverWait(driver, Duration.ofMillis(IMPLICITLY_TIMEOUT_SECONDS))
                        .until(ExpectedConditions.elementToBeClickable(By.className(SIGN_UP_BUTTON_CLASS_NAME)));
                signUpButton.click();
                Thread.sleep(1000L);
                WebElement confirmButton = new WebDriverWait(driver, Duration.ofMillis(IMPLICITLY_TIMEOUT_SECONDS))
                        .until(ExpectedConditions.elementToBeClickable(By.className(CONFIRM_BUTTON_CLASS_NAME)));
                confirmButton.click();
                new WebDriverWait(driver, Duration.ofMillis(IMPLICITLY_TIMEOUT_SECONDS))
                        .until(ExpectedConditions.elementToBeClickable(By.className(SIGNED_UP_DIV_CLASS)));
                signedUpForEvent = true;
            } catch (TimeoutException e) {
                System.out.printf("Timeout exception occurred. Try again in %s seconds.", TIMEOUT_SECONDS);
                Thread.sleep(TIMEOUT_SECONDS * 1000L);
            }
        }

        driver.quit();

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("-------------------------- Successfully signed up for event! ----------------------------");
        System.out.println("-----------------------------------------------------------------------------------------");
    }

}
