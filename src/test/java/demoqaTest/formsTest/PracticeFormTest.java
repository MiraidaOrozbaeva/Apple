package demoqaTest.formsTest;

import demoqaTest.BaseTest;
import org.example.demoqa.models.UserPracticeForm;
import org.example.demoqa.utils.RandomUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
@Tag("UI")
public class PracticeFormTest extends BaseTest {

    @Test
    void practiceFormTest1() {
        UserPracticeForm userPracticeForm = RandomUtils.generatePracticeFormUser();
        driver.get("https://demoqa.com/automation-practice-form");
        practiceFormPage.fillPracticeForm(userPracticeForm);
    }

    @Test
    void practiceFormTest2() {
        UserPracticeForm userPracticeForm = new UserPracticeForm("Miraida", "Orozbaeva",
                "orozbaeva@gmail.com", "Female", "0552233988", "16 Apr 1994",
                "English", "Bishkek city");
        driver.get("https://demoqa.com/automation-practice-form");

        practiceFormPage.fillPracticeForm(userPracticeForm);
    }

    @Test
    void practiceFormTest3(){
        driver.get("https://demoqa.com/automation-practice-form");
        practiceFormPage.fillUserFirstName("Miraida").fillUserLastName("Orozbaeva").fillUserEmail("miraida@gmail.com")
                .selectGender("Female").fillUserNumber("0666777888").fillDateOfBirth("12 December 2002")
                .fillOutSubjects().selectMusicAsHobbies().fillUserCurrentAddress("Bishkek")
                .uploadPicture("/Users/miraida/IdeaProjects/Apple/src/test/resources/cat.png")
                .selectStateAndCity("haryana").submit();
//        Assertions compare all the displayed info with submitted one
    }

}
