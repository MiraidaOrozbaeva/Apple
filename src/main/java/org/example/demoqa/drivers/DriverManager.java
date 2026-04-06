package org.example.demoqa.drivers;

import io.qameta.allure.Step;
import org.example.demoqa.utils.FileReaderUtil;
import org.openqa.selenium.WebDriver;

public class DriverManager {

    private static WebDriver driver;

    @Step("File Reader Util Get Value")
    public static WebDriver getDriver() {
        if (driver == null) {
            switch (FileReaderUtil.getValue("browser").toLowerCase()) {
                case "chrome":
                    driver = ChromeWebDriver.loadChromeDriver();
                    break;
                case "edge":
                    driver = EdgeWebDriver.loadEdgeWebDriver();
                    break;
                case "safari":
                    driver = SafariWebDriver.loadSafariWebDriver();
                    break;
                case "firefox":
                    driver = FireFoxWebDriver.loadFirefoxWebDriver();
                    break;
                default:
                    throw new IllegalArgumentException("Wrong driver name");
            }
        }
        System.out.println("BROWSER: = " + System.getenv("BROWSER IS: "));
        return driver;
    }

    @Step("Close Driver")
    public static void closeDriver(){
        try {
            if (driver != null){
                driver.quit();
                driver = null;
            }
        } catch (Exception e){
            System.err.println("Error while closing driver");
        }
    }
}
