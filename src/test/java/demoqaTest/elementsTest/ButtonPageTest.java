package demoqaTest.elementsTest;

import demoqaTest.BaseTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
@Tag("UI")
public class ButtonPageTest extends BaseTest {

    @Test
    void buttonPageTest(){
        driver.get("https://demoqa.com/buttons");
        buttonsPage.doubleClick();
        buttonsPage.rightClick();

    }
}
