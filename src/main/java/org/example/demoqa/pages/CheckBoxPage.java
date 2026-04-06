package org.example.demoqa.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.example.demoqa.drivers.ChromeWebDriver.driver;

public class CheckBoxPage extends BasePage{

    @FindBy(css = ".text-center")
    private WebElement textCenter;

    @FindBy(css = ".rct-collapse-btn")
    private WebElement collapseBtnArrow;

    @FindBy(xpath = ".//div[@class='rc-tree-list-holder']//span[starts-with(@class, 'rc-tree-switcher')]")
    private WebElement expandAllBtn;

    @FindBy(xpath = ".//div[@class='rc-tree-list-holder']//span[starts-with(@class, 'rc-tree-switcher')]")
    private WebElement collapseAllBtn;

    public CheckBoxPage clickCollapseAllBtn(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", collapseAllBtn);
//        elementActions.clickBtn(collapseAllBtn);
        return this;
    }

    public CheckBoxPage clickExpandAllBtn(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", expandAllBtn);
//        elementActions.clickBtn(expandAllBtn);
        return this;
    }

    public CheckBoxPage clickCollapseBtnArrow(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", collapseBtnArrow);
//        elementActions.clickBtn(collapseBtnArrow);
        return this;
    }
}
