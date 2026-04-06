package demoqaTest.elementsTest;

import demoqaTest.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
@Tag("UI")
public class RadioButtonTest extends BaseTest {

    @Test
    @Tag("SMOKE")
    @DisplayName("Click yes button; click impressive button")
    void radioButtonTest(){
        driver.get("https://demoqa.com/radio-button");
        radioButtonPage.clickYesBtn();
        radioButtonPage.clickImpressiveBtn();

    }
}
