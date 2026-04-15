package org.example.demoqa.pages;

import io.qameta.allure.Step;
import org.example.demoqa.drivers.DriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RadioButtonPage extends BasePage{

    @FindBy(css = "#yesRadio + label") // находим по id и знак "+" берет следующего соседа после
    private WebElement yesCheckBox;

    @FindBy(xpath = "//label[@for='impressiveRadio']")
    private WebElement impressiveCheckBox;

    @Step("Click yes button")
    public RadioButtonPage clickYesBtn(){
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", yesCheckBox);
        return this;
    }

    @Step("Click impressive button")
    public RadioButtonPage clickImpressiveBtn(){
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", impressiveCheckBox);
        return this;
    }
}
