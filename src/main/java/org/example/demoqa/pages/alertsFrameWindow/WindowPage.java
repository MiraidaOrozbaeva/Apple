package org.example.demoqa.pages.alertsFrameWindow;

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

    public WindowPage clickTabBtn(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tabBtn);
//        elementActions.clickBtn(tabBtn);
        return this;
    }

    public WindowPage clickWindowBtn(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", windowBtn);
//        elementActions.clickBtn(windowBtn);
        return this;
    }

    public WindowPage clickMessageWindowBtn(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", messageWindowBtn);
//        elementActions.clickBtn(messageWindowBtn);
        return this;
    }
}
