package org.example.demoqa.pages;

import io.qameta.allure.Step;
import org.example.demoqa.drivers.DriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckBoxPage extends BasePage{

    @FindBy(css = ".text-center")
    private WebElement textCenter;

    @FindBy(css = ".rct-collapse-btn")
    private WebElement collapseBtnArrow;

    @FindBy(xpath = ".//div[@class='rc-tree-list-holder']//span[starts-with(@class, 'rc-tree-switcher')]")
    private WebElement expandAllBtn;

    @FindBy(xpath = ".//div[@class='rc-tree-list-holder']//span[starts-with(@class, 'rc-tree-switcher')]")
    private WebElement collapseAllBtn;

    @Step("Click collapse all button")
    public CheckBoxPage clickCollapseAllBtn(){
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", collapseAllBtn);
        return this;
    }

    @Step("Click expand all button")
    public CheckBoxPage clickExpandAllBtn(){
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", expandAllBtn);
        return this;
    }

    @Step("Click collapse button arrow")
    public CheckBoxPage clickCollapseBtnArrow(){
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", collapseBtnArrow);
        return this;
    }
}
