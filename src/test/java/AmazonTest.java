import demoqaTest.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.security.Key;
import java.time.Duration;
import java.util.List;

public class AmazonTest extends BaseTest{

    @Test
    void amazonTest() throws InterruptedException {
//        WebDriverManager.chromedriver().setup();
//        WebDriver driver = new ChromeDriver();
        driver.get("https://www.amazon.com");
        Thread.sleep(5000);
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("iphone", Keys.RETURN);
        Thread.sleep(3000);

//        #brandsRefinements ul li a
//        #brandsRefinements ul li div.a-checkbox

        try {
            List<WebElement> brands = driver.findElements(By.cssSelector("#brandsRefinements ul li a")); // ul li a
            for (WebElement brand : brands) {
                brand.click();
            }
        } catch (StaleElementReferenceException e) {
            e.getStackTrace();
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
