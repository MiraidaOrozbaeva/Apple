package org.example.demoqa.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.example.demoqa.drivers.ChromeWebDriver.driver;

public class RadioButtonPage extends BasePage{

    @FindBy(css = "#yesRadio + label") // находим по id и знак "+" берет следующего соседа после
    private WebElement yesCheckBox;

    @FindBy(xpath = "//label[@for='impressiveRadio']")
    private WebElement impressiveCheckBox;

    public RadioButtonPage clickYesBtn(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", yesCheckBox);
//        elementActions.clickBtn(yesCheckBox);
        return this;
    }

    public RadioButtonPage clickImpressiveBtn(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", impressiveCheckBox);
//        elementActions.clickBtn(impressiveCheckBox);
        return this;
    }
}
