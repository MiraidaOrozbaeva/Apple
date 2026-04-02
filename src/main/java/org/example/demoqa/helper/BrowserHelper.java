package org.example.demoqa.helper;

import org.example.demoqa.drivers.DriverManager;
import org.openqa.selenium.WebDriver;

import java.util.LinkedList;
import java.util.Set;

public class BrowserHelper {

    private WebDriver driver = DriverManager.getDriver();

    public BrowserHelper(WebDriver driver) {
        this.driver = driver;
    }

    public void open(String url){
        driver.navigate().to(url); // отличается от driver.get() тем что запоминает историю и может двигаться назад/вперед
    }

    public void goBack(){
        driver.navigate().back();
    }

    public void goForward(){
        driver.navigate().forward();
    }

    public void refreshPage(){
        driver.navigate().refresh();
    }

    public Set<String> getWindows(){
        return driver.getWindowHandles();
    }

    public void switchToWindow(int index){
        LinkedList<String> windowsId = new LinkedList<>(getWindows());
        if (index < 0 || index >= windowsId.size()){
            throw new IllegalArgumentException("Invalid index " + index);
        }
        driver.switchTo().window(windowsId.get(index));
    }

    public void switchToParentWindow(){
        LinkedList<String> windowsId = new LinkedList<>(getWindows());
        driver.switchTo().window(windowsId.getFirst());
    }




}
