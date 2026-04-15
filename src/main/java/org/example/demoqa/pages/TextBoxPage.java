package org.example.demoqa.pages;

import io.qameta.allure.Step;
import org.example.demoqa.drivers.DriverManager;
import org.example.demoqa.models.UserTextBox;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TextBoxPage extends BasePage {

    @FindBy(className = "text-center")
    private WebElement textCenter;

    @FindBy(id = "userName")
    private WebElement userNameInput;

    @FindBy(id = "userEmail")
    private WebElement userEmailInput;

    @FindBy(id = "currentAddress")
    private WebElement currentAddressInput;

    @FindBy(id = "permanentAddress")
    private WebElement permanentAddressInput;

    @FindBy(id = "submit")
    private WebElement submitBtn;

    @FindBy(id = "name")
    private WebElement displayedName;

    @FindBy(id = "email")
    private WebElement displayedEmail;

    @FindBy(css = "p#currentAddress")
    private WebElement displayedCurrentAddress;

    @FindBy(css = "p#permanentAddress")
    private WebElement displayedPermanentAddress;

    @Step("Get submitted name")
    public String getSubmittedName(){
        elementActions.waitElementToBeVisible(displayedName).scrollToElement(displayedName);
        return displayedName.getText().replace("Name:", "").trim();
    }

    @Step("Get submitted email")
    public String getSubmittedEmail(){
        elementActions.waitElementToBeVisible(displayedEmail).scrollToElement(displayedEmail);
        return displayedEmail.getText().replace("Email:", "").trim();
    }

    @Step("Get submitted current address")
    public String getSubmittedCurrentAddress(){
        elementActions.waitElementToBeVisible(displayedCurrentAddress).scrollToElement(displayedCurrentAddress);
        return displayedCurrentAddress.getText().replace("Current Address :", "").trim();
    }

    @Step("Get submitted permanent address")
    public String getSubmittedPermanentAddress(){
        elementActions.waitElementToBeVisible(displayedPermanentAddress).scrollToElement(displayedPermanentAddress);
        return displayedPermanentAddress.getText().replace("Permanent Address :", "").trim();
    }

    @Step("Fill user name")
    public TextBoxPage fillUserName(String userName){
        elementActions.inputText(userNameInput, userName);
        return this;
    }

    @Step("Fill user email")
    public TextBoxPage fillUserEmail(String userEmail){
        elementActions.inputText(userEmailInput, userEmail);
        return this;
    }

    @Step("Fill current address")
    public TextBoxPage fillCurrentAddress(String currentAddress){
        elementActions.inputText(currentAddressInput, currentAddress);
        return this;
    }

    @Step("Fill permanent address")
    public TextBoxPage fillPermanentAddress(String permanentAddress){
        elementActions.inputText(permanentAddressInput, permanentAddress);
        return this;
    }

    @Step("Click submit button")
    public TextBoxPage clickSubmit(){
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", submitBtn);
        return this;
    }

    @Step("Fill out complete text box form")
    public TextBoxPage fillUpTextBoxForm(UserTextBox userTextBox){
        fillUserName(userTextBox.getName()).fillUserEmail(userTextBox.getEmail())
                .fillCurrentAddress(userTextBox.getCurrentAddress())
                .fillPermanentAddress(userTextBox.getPermanentAddress()).clickSubmit();
        return this;
    }
}
// Почему так: Статические мутируемые поля — источник race condition.
// DriverManager уже выполняет роль держателя драйвера;
// дублировать эту логику в каждом driver-классе — нарушение принципа Single Responsibility.
