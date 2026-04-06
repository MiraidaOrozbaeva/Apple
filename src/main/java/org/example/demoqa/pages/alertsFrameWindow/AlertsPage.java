package org.example.demoqa.pages.alertsFrameWindow;

import io.qameta.allure.Step;
import org.example.demoqa.pages.BasePage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.example.demoqa.drivers.ChromeWebDriver.driver;

public class AlertsPage extends BasePage {

    @FindBy(id = "alertButton")
    private WebElement alertBtn;

    @FindBy(id = "timerAlertButton")
    private WebElement timerAlertBtn;

    @FindBy(id = "confirmButton")
    private WebElement confirmBtn;

    @FindBy(id = "promtButton")
    private WebElement promtBtn;

    @Step("Click alert button")
    public AlertsPage clickAlertBtn(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", alertBtn);
        return this;
    }

    @Step("Click time alert button")
    public AlertsPage clickTimerAlertBtn(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", timerAlertBtn);
        return this;
    }

    @Step("Click confirm button")
    public AlertsPage clickConfirmBtn(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmBtn);
        return this;
    }

    @Step("Click promt button")
    public AlertsPage clickPromptBtn(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", promtBtn);
        return this;
    }
}
