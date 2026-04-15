package demoqaTest.alertFrameWindow;

import demoqaTest.BaseTest;
import org.example.demoqa.pages.BasePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Set;

@Tag("UI")
public class WindowTest extends BaseTest {

    @Test
    @Tag("SMOKE")
    @DisplayName("Click TAB button; switch to second window; switch back to previous window")
    void windowTest(){
        driver.get("https://demoqa.com/browser-windows");
        // Получить хэндл до клика, потом найти новый
        Set<String> before = driver.getWindowHandles();
        windowPage.clickTabBtn();
        Set<String> after = driver.getWindowHandles();
        after.removeAll(before); // оставляем только новый
        driver.switchTo().window(after.iterator().next());
        browserHelper.switchToParentWindow();
    }
}
