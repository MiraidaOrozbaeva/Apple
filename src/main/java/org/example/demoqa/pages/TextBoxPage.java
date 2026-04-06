package org.example.demoqa.pages;

import org.example.demoqa.drivers.DriverManager;
import org.example.demoqa.helper.ElementActions;
import org.example.demoqa.models.UserTextBox;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

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

    public String getSubmittedName(){
        elementActions.waitElementToBeVisible(displayedName).scrollToElement(displayedName);
        return displayedName.getText().replace("Name:", "").trim();
    }

    public String getSubmittedEmail(){
        elementActions.waitElementToBeVisible(displayedEmail).scrollToElement(displayedEmail);
        return displayedEmail.getText().replace("Email:", "").trim();
    }

    public String getSubmittedCurrentAddress(){
        elementActions.waitElementToBeVisible(displayedCurrentAddress).scrollToElement(displayedCurrentAddress);
        return displayedCurrentAddress.getText().replace("Current Address :", "").trim();
    }

    public String getSubmittedPermanentAddress(){
        elementActions.waitElementToBeVisible(displayedPermanentAddress).scrollToElement(displayedPermanentAddress);
        return displayedPermanentAddress.getText().replace("Permananet Address :", "").trim();
    }


    public TextBoxPage fillUserName(String userName){
        elementActions.inputText(userNameInput, userName);
        return this;
    }

    public TextBoxPage fillUserEmail(String userEmail){
        elementActions.inputText(userEmailInput, userEmail);
        return this;
    }

    public TextBoxPage fillCurrentAddress(String currentAddress){
        elementActions.inputText(currentAddressInput, currentAddress);
        return this;
    }

    public TextBoxPage fillPermanentAddress(String permanentAddress){
        elementActions.inputText(permanentAddressInput, permanentAddress);
        return this;
    }

    public TextBoxPage clickSubmit(){
        elementActions.clickBtn(submitBtn);
        return this;
    }

    public TextBoxPage fiilUpTextBoxForm(UserTextBox userTextBox){
        fillUserName(userTextBox.getName()).fillUserEmail(userTextBox.getEmail())
                .fillCurrentAddress(userTextBox.getCurrentAddress())
                .fillPermanentAddress(userTextBox.getPermanentAddress()).clickSubmit();

        return this;
    }
}
