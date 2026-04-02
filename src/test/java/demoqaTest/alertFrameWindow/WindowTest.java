package demoqaTest.alertFrameWindow;

import demoqaTest.BaseTest;
import org.example.demoqa.pages.BasePage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
@Tag("UI")
public class WindowTest extends BaseTest {

    @Test
    void windowTest(){
        driver.get("https://demoqa.com/browser-windows");
        windowPage.clickTabBtn();
        windowPage.clickTabBtn();
        browserHelper.switchToWindow(2);
        browserHelper.switchToParentWindow();
    }
}
