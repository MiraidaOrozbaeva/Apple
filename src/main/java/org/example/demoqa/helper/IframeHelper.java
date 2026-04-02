package org.example.demoqa.helper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class IframeHelper {

    private  WebDriver driver;

    public  IframeHelper(WebDriver driver) {
        this.driver = driver;
    }

    public void switchToFrame(String nameOrId){
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(nameOrId));
    }

    public void switchToParentFrame(){
        driver.switchTo().parentFrame();
    }

}
