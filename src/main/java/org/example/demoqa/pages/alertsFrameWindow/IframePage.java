package org.example.demoqa.pages.alertsFrameWindow;

import io.qameta.allure.Step;
import org.example.demoqa.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class IframePage extends BasePage {

    @FindBy(id = "sampleHeading")
    private WebElement sampleHeading;

    @Step("Get sample heading text")
    public String getSampleHeadingText(){
        return sampleHeading.getText();
    }
}
