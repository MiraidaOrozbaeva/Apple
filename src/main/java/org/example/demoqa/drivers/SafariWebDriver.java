package org.example.demoqa.drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class SafariWebDriver {

    public static WebDriver loadSafariWebDriver() {
        WebDriverManager.safaridriver().setup();
        WebDriver driver = new EdgeDriver();
        driver.manage().window().maximize();
        return driver;
    }
}
