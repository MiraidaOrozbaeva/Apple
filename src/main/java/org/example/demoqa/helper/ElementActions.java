package org.example.demoqa.helper;

import org.example.demoqa.drivers.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import javax.swing.*;
import java.time.Duration;
import java.util.function.Function;

public class ElementActions {
    Actions action = new Actions(DriverManager.getDriver());

    public ElementActions waitElementToBeClickable(WebElement element) {
        new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions.elementToBeClickable(element));
        return this;
    }

    public ElementActions waitElementToBeVisible(WebElement element) {
        new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOf(element));
        return this;
    }

    public ElementActions clickBtn(WebElement element) {
        waitElementToBeVisible(element);
        scrollToElement(element);
        waitElementToBeClickable(element);
        element.click();
        return this;
    }

    public ElementActions inputText(WebElement element, String text) {
        waitElementToBeVisible(element);
        scrollToElement(element);
        element.sendKeys(text);
        return this;
    }

    public ElementActions inputNumber(WebElement element, Integer number){
        waitElementToBeVisible(element);
        scrollToElement(element);
        element.sendKeys(number.toString());
        return this;
    }

    public ElementActions scrollToElement(WebElement element){
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", element);
        return this;
    }

    public ElementActions selectByVisibleText(WebElement element, String value){
        Select select = new Select(element);
        select.selectByVisibleText(value);
        return this;
    }

    public ElementActions selectByIndex(WebElement element, int index){
        Select select = new Select(element);
        select.selectByIndex(index);
        return this;
    }

    public ElementActions doubleClick(WebElement element){
        waitElementToBeVisible(element);
        waitElementToBeClickable(element);
        action.doubleClick(element).perform();
        return this;
    }

    public ElementActions rightClick(WebElement element){
        waitElementToBeVisible(element);
        waitElementToBeClickable(element);
        action.contextClick(element).perform();
        return this;
    }
}


