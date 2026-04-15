import demoqaTest.BaseTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AmazonTest extends BaseTest{

    @Test
    void amazonTest() throws InterruptedException {
//        WebDriverManager.chromedriver().setup();
//        WebDriver driver = new ChromeDriver();
        driver.get("https://www.amazon.com");
        // Использовать WebDriverWait вместо Thread.sleep
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchBox = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("twotabsearchtextbox")));
        searchBox.sendKeys("iphone", Keys.RETURN);

//        #brandsRefinements ul li a
//        #brandsRefinements ul li div.a-checkbox

        try {
            List<WebElement> brands = driver.findElements(By.cssSelector("#brandsRefinements ul li a")); // ul li a
            for (WebElement brand : brands) {
                brand.click();
            }
            // catch с реальной обработкой:
        } catch (StaleElementReferenceException e) {
            System.err.println("Stale element: " + e.getMessage()); // или log.warn
        }

//        By itemsLocator = By.cssSelector("#brandsRefinements");
//
//        for (int i = 0; i < driver.findElements(itemsLocator).size(); i++){
//            List<WebElement> elements = driver.findElements(itemsLocator);
//            elements.get(i).click();
//        }
    }

        @Test
        void clickAllBrandsTest() {
            driver.get("https://www.amazon.com");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement searchBox = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.id("twotabsearchtextbox")));

            searchBox.sendKeys("iphone", Keys.RETURN);

            By brandsLocator = By.cssSelector("#brandsRefinements ul li");

            int brandsCount = wait.until(ExpectedConditions
                    .presenceOfAllElementsLocatedBy(brandsLocator)).size();

            for (int i = 0; i < brandsCount; i++) {

                // каждый раз находим список заново
                List<WebElement> brands = wait.until(ExpectedConditions
                        .presenceOfAllElementsLocatedBy(brandsLocator));

                WebElement brand = brands.get(i);

                String oldUrl = driver.getCurrentUrl();


                // скролл чтобы было видно
                ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].scrollIntoView(true);", brand);

                wait.until(ExpectedConditions.elementToBeClickable(brand)).click();

                wait.until(ExpectedConditions.not(
                        ExpectedConditions.urlToBe(oldUrl)));            }
        }



}
