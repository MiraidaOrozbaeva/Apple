package org.example.demoqa.helper;

import io.qameta.allure.Step;
import org.example.demoqa.drivers.DriverManager;
import org.openqa.selenium.WebDriver;

import java.util.LinkedList;
import java.util.Set;

public class BrowserHelper {

    private WebDriver driver = DriverManager.getDriver();

    public BrowserHelper(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Open url")
    public void open(String url){
        driver.navigate().to(url); // отличается от driver.get() тем что запоминает историю и может двигаться назад/вперед
    }

    @Step("Go back")
    public void goBack(){
        driver.navigate().back();
    }

    @Step("Go forward")
    public void goForward(){
        driver.navigate().forward();
    }

    @Step("Refresh page")
    public void refreshPage(){
        driver.navigate().refresh();
    }

    @Step("Get all open windows")
    public Set<String> getWindows(){
        return driver.getWindowHandles();
    }

    @Step("Switch to window")
    public void switchToWindow(int index){
        LinkedList<String> windowsId = new LinkedList<>(getWindows());
        if (index < 0 || index >= windowsId.size()){
            throw new IllegalArgumentException("Invalid index " + index);
        }
        driver.switchTo().window(windowsId.get(index));
    }

    @Step("Switch to parent window")
    public void switchToParentWindow(){
        LinkedList<String> windowsId = new LinkedList<>(getWindows());
        driver.switchTo().window(windowsId.getFirst());
    }
}
