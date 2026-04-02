package org.example.demoqa.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckBoxPage extends BasePage{

    @FindBy(css = ".text-center")
    private WebElement textCenter;

    @FindBy(css = ".rct-collapse-btn")
    private WebElement collapseBtnArrow;

    @FindBy(css = ".rct-option-expand-all")
    private WebElement expandAllBtn;

    @FindBy(css = ".rct-option-collapse-all")
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
