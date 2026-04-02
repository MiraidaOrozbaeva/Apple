package org.example.demoqa.drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeWebDriver {

    public static WebDriver driver;

    public static WebDriver loadChromeDriver(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("--headless=new");  // ← добавили
        options.addArguments("--no-sandbox");           // ← обязательно для Jenkins/Linux
        options.addArguments("--disable-dev-shm-usage"); // ← обязательно для Jenkins/Linux
        options.addArguments("--disable-gpu");           // ← для совместимости
        options.addArguments("--window-size=1920,1080"); // ← вместо maximize() в headless
        driver = new ChromeDriver(options);
//        driver.manage().window().maximize();
        return driver;
    }
}
