package org.example.demoqa.pages;

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

    public CheckBoxPage clickCollapseAllBtn(){
        elementActions.clickBtn(collapseAllBtn);
        return this;
    }

    public CheckBoxPage clickExpandAllBtn(){
        elementActions.clickBtn(expandAllBtn);
        return this;
    }

    public CheckBoxPage clickCollapseBtnArrow(){
        elementActions.clickBtn(collapseBtnArrow);
        return this;
    }



}
