package demoqaTest.elementsTest;

import demoqaTest.BaseTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
@Tag("UI")
public class RadioButtonTest extends BaseTest {

    @Test
    void radioButtonTest(){
        driver.get("https://demoqa.com/radio-button");
        radioButtonPage.clickYesBtn();
        radioButtonPage.clickImpressiveBtn();

    }
}
