package org.example.demoqa.helper;

import io.qameta.allure.Step;
import org.example.demoqa.drivers.DriverManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AlertHelper {

    private WebDriver driver = DriverManager.getDriver();

    public AlertHelper(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Switch to alert")
    private Alert getAlert(){
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.alertIsPresent());
        return driver.switchTo().alert(); // c html переключается на Alert чтобы решить нажимать ок либо отменить
    }

    @Step("Accept alert")
    public void accept(){
        getAlert().accept();
    }

    @Step("Dismiss alert")
    public void dismiss(){
        getAlert().dismiss();
    }

    @Step("Input text in alert window")
    public AlertHelper sendKeys(String message){
        getAlert().sendKeys(message);
        return this;
    }
}
