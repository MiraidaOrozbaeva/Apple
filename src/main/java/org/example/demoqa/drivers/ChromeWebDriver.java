package org.example.demoqa.drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Step;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeWebDriver {

    // поле driver здесь вообще не нужно — фабричный метод должен просто создавать и возвращать

    @Step("Chrome Driver Set Up")
    public static WebDriver loadChromeDriver(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("--headless");  // ← добавили
        WebDriver driver = new ChromeDriver(options); // локальная переменная!
        driver.manage().window().maximize();
        return driver; // возвращаем, но не храним
    }
}
// Проблема: Поле driver объявлено как public static.
// Это означает, что оно одно на весь класс (не на поток).
// При параллельном запуске тестов два потока будут писать в одно поле и читать из него — тест A получит браузер теста B.
// Кроме того, в TextBoxPage.java есть прямой импорт
// import static org.example.demoqa.drivers.ChromeWebDriver.driver — Page-объект напрямую тащит статический драйвер, минуя DriverManager.
