package org.example.demoqa.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ButtonsPage extends BasePage {

    @FindBy(id = "doubleClickBtn")
    private WebElement doubleClickBtn;

    @FindBy(id = "rightClickBtn")
    private WebElement rightClickBtn;

    @FindBy(xpath = "//button[text() = 'Click Me']")
    private WebElement clickMeBtn;


    public ButtonsPage doubleClick(){
        elementActions.doubleClick(doubleClickBtn);
        return this;
    }

    public ButtonsPage rightClick(){
        elementActions.rightClick(rightClickBtn);
        return this;
    }

}
