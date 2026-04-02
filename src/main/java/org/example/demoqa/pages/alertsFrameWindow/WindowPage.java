package org.example.demoqa.pages.alertsFrameWindow;

import org.example.demoqa.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class WindowPage extends BasePage {

    @FindBy(id = "tabButton")
    private WebElement tabBtn;

    @FindBy(id = "windowButton")
    private WebElement windowBtn;

    @FindBy(id = "messageWindowButton")
    private WebElement messageWindowBtn;

    public WindowPage clickTabBtn(){
        elementActions.clickBtn(tabBtn);
        return this;
    }

    public WindowPage clickWindowBtn(){
        elementActions.clickBtn(windowBtn);
        return this;
    }

    public WindowPage clickMessageWindowBtn(){
        elementActions.clickBtn(messageWindowBtn);
        return this;
    }
}
