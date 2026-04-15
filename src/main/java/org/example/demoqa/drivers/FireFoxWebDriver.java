package org.example.demoqa.drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class FireFoxWebDriver {

    public static WebDriver loadFirefoxWebDriver(){
        WebDriverManager.firefoxdriver().setup();
        WebDriver driver = new EdgeDriver();
        driver.manage().window().maximize();
        return driver;
    }
}
