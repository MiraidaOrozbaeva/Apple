package org.example.demoqa.pages.alertsFrameWindow;

import io.qameta.allure.Step;
import org.example.demoqa.pages.BasePage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.example.demoqa.drivers.ChromeWebDriver.driver;

public class WindowPage extends BasePage {

    @FindBy(id = "tabButton")
    private WebElement tabBtn;

    @FindBy(id = "windowButton")
    private WebElement windowBtn;

    @FindBy(id = "messageWindowButton")
    private WebElement messageWindowBtn;

    @Step("Click TAB button")
    public WindowPage clickTabBtn(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tabBtn);
        return this;
    }

    @Step("Click window button")
    public WindowPage clickWindowBtn(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", windowBtn);
        return this;
    }

    @Step("Click message window button")
    public WindowPage clickMessageWindowBtn(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", messageWindowBtn);
        return this;
    }
}
