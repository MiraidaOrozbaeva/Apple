package demoqaTest.elementsTest;

import demoqaTest.BaseTest;
import io.qameta.allure.AllureId;
import org.example.demoqa.models.UserTextBox;
import org.example.demoqa.utils.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
@Tag("UI")
public class TextBoxTest extends BaseTest {


    @Test
    @Tag("TextBoxTest")
    @DisplayName("Fill up text box form with created user details")
    void textBoxTest(){
        UserTextBox userTextBox = new UserTextBox("Miraida", "miraida@gmail.com",
                "Bishkek city", "Kyrgyzstan");
        driver.get("https://demoqa.com/text-box");

//        textBoxPage.fillUserName("Miraida").fillUserEmail("miraida@gmail.com").fillCurrentAddress("Bishkek city Mira 333")
//                .fillPermanentAddress("Kyrgyzstan Bishkek").clickSubmit();

        textBoxPage.fiilUpTextBoxForm(userTextBox);
    }

    @Test
    @Tag("REGRESSION")
    @DisplayName("Fill up text box form with user's random generated details; assert submitted with displayed data")
    void textBoxCompareDisplayedInfoTest(){
        UserTextBox userTextBox = RandomUtils.generateTextBoxForm();
        driver.get("https://demoqa.com/text-box");
        textBoxPage.fiilUpTextBoxForm(userTextBox);

        Assertions.assertEquals(textBoxPage.getSubmittedName(), userTextBox.getName()
                , "Name does not match");
        Assertions.assertEquals(textBoxPage.getSubmittedEmail(), userTextBox.getEmail()
                , "Email does not match");
        Assertions.assertEquals(textBoxPage.getSubmittedCurrentAddress(), userTextBox.getCurrentAddress()
                , "Current Address does not match");
        Assertions.assertEquals(textBoxPage.getSubmittedPermanentAddress(), userTextBox.getPermanentAddress()
                , "Permanent Address does not match");
    }
}
