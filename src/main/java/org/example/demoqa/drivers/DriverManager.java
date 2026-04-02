package org.example.demoqa.drivers;

import org.example.demoqa.utils.FileReaderUtil;
import org.openqa.selenium.WebDriver;


public class DriverManager {
    private static WebDriver driver;

    public static WebDriver getDriver(){
        if (driver == null){
            switch (FileReaderUtil.getValue("browser").toLowerCase()){
                case "chrome" : driver = ChromeWebDriver.loadChromeDriver();
                break;
                case "edge" : driver = EdgeWebDriver.loadEdgeWebDriver();
                break;
                case "safari" : driver = SafariWebDriver.loadSafariWebDriver();
                break;
                case "firefox" : driver = FireFoxWebDriver.loadFirefoxWebDriver();
                break;
                default: throw new IllegalArgumentException("Wrong driver name");
            }
        }
        return driver;
    }

    public static void closeDriver(){
        try {
            if (driver != null){
                driver.close();
                driver.quit();
                driver = null;
            }
        } catch (Exception e){
            System.err.println("Error while closing driver");
        }
    }
}
