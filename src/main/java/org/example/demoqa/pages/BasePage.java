package org.example.demoqa.pages;

import org.example.demoqa.drivers.DriverManager;
import org.example.demoqa.helper.ElementActions;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {

    public ElementActions elementActions = new ElementActions();

    public BasePage () {
        PageFactory.initElements(DriverManager.getDriver(), this);
    }



}
