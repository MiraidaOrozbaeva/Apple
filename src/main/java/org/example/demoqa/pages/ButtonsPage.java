package org.example.demoqa.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ButtonsPage extends BasePage {

    @FindBy(id = "doubleClickBtn")
    private WebElement doubleClickBtn;

    @FindBy(id = "rightClickBtn")
    private WebElement rightClickBtn;

    @FindBy(xpath = "//button[text() = 'Click Me']")
    private WebElement clickMeBtn;


    @Step("Double click")
    public ButtonsPage doubleClick(){
        elementActions.doubleClick(doubleClickBtn);
        return this;
    }

    @Step("Right click")
    public ButtonsPage rightClick(){
        elementActions.rightClick(rightClickBtn);
        return this;
    }
}
