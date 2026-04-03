package demoqaTest.elementsTest;

import demoqaTest.BaseTest;
import org.example.demoqa.pages.CheckBoxPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
@Tag("UI")
public class CheckBoxTest extends BaseTest {

    @Test
    void checkBoxTest(){
        driver.get("https://demoqa.com/checkbox");
        checkBoxPage.clickExpandAllBtn();
//        checkBoxPage.clickCollapseAllBtn();
//        checkBoxPage.clickCollapseBtnArrow();
    }
}
