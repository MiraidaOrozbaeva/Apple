package org.example.demoqa.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RadioButtonPage extends BasePage{

    @FindBy(css = "#yesRadio + label") // находим по id и знак "+" берет следующего соседа после
    private WebElement yesCheckBox;

    @FindBy(xpath = "//label[@for='impressiveRadio']")
    private WebElement impressiveCheckBox;

    public RadioButtonPage clickYesBtn(){
        elementActions.clickBtn(yesCheckBox);
        return this;
    }

    public RadioButtonPage clickImpressiveBtn(){
        elementActions.clickBtn(impressiveCheckBox);
        return this;
    }
}
