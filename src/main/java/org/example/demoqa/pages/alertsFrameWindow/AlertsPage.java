package org.example.demoqa.pages.alertsFrameWindow;

import io.qameta.allure.Step;
import org.example.demoqa.drivers.DriverManager;
import org.example.demoqa.pages.BasePage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", alertBtn);
        return this;
    }

    @Step("Click time alert button")
    public AlertsPage clickTimerAlertBtn(){
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", timerAlertBtn);
        return this;
    }

    @Step("Click confirm button")
    public AlertsPage clickConfirmBtn(){
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", confirmBtn);
        return this;
    }

    @Step("Click promt button")
    public AlertsPage clickPromptBtn(){
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", promtBtn);
        return this;
    }
}
