package org.example.demoqa.drivers;

import io.qameta.allure.Step;
import org.example.utils.file.ConfigurationManager;
import org.openqa.selenium.WebDriver;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    @Step("Initialize WebDriver")
    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            switch (ConfigurationManager.getBaseConfig().browser().toLowerCase()) {
                case "chrome":
                    driverThreadLocal.set(ChromeWebDriver.loadChromeDriver());
                    break;
                case "edge":
                    driverThreadLocal.set(EdgeWebDriver.loadEdgeWebDriver());
                    break;
                case "safari":
                    driverThreadLocal.set(SafariWebDriver.loadSafariWebDriver());
                    break;
                case "firefox":
                    driverThreadLocal.set(FireFoxWebDriver.loadFirefoxWebDriver());
                    break;
                default:
                    throw new IllegalArgumentException("Wrong driver name");
            }
        }
        System.out.println("BROWSER: = " + ConfigurationManager.getBaseConfig().browser());
        return driverThreadLocal.get();
    }

    @Step("Close WebDriver")
    public static void closeDriver() {
        if (driverThreadLocal.get() != null) {
            driverThreadLocal.get().quit();
            driverThreadLocal.remove(); // обязательно! иначе ThreadLocal не очищается
        }
    }
}
// Почему так: ThreadLocal гарантирует, что у каждого потока (теста) есть своя независимая копия драйвера.
// Это стандартный паттерн для параллельного запуска тестов.
