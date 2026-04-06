package demoqaTest.elementsTest;

import demoqaTest.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
@Tag("UI")
public class ButtonPageTest extends BaseTest {

    @Test
    @Tag("SMOKE")
    @DisplayName("Double click button; right click button")
    void buttonPageTest(){
        driver.get("https://demoqa.com/buttons");
        buttonsPage.doubleClick();
        buttonsPage.rightClick();

    }
}
