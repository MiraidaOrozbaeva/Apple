package org.example.demoqa.pages;

import org.example.demoqa.drivers.DriverManager;
import org.example.demoqa.models.UserPracticeForm;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

public class PracticeFormPage extends BasePage {

    @FindBy(id = "firstName")
    private WebElement firstNameInput;

    @FindBy(id = "lastName")
    private WebElement lastNameInput;

    @FindBy(id = "userEmail")
    private WebElement userEmailInput;

    @FindBy(css = "input[value='Male']+label")
    private WebElement genderSelectMale;

    @FindBy(css = "input[value='Female']+label")
    private WebElement genderSelectFemale;

    @FindBy(css = "input[value='Other']+label")
    private WebElement genderSelectOther;

    @FindBy(id = "userNumber")
    private WebElement userNumberInput;

    @FindBy(css = ".react-datepicker__input-container")
    private WebElement datePickerInput;

    @FindBy(id = "subjectsInput")
    private WebElement subjectsInput;

    @FindBy(xpath = "//label[text()='Sports']")
    private WebElement hobbiesSelectSports;

    @FindBy(xpath = "//label[text()='Reading']")
    private WebElement hobbiesSelectReading;

    @FindBy(xpath = "//label[text()='Music']")
    private WebElement hobbiesSelectMusic;

    @FindBy(id = "uploadPicture")
    private WebElement uploadPicture;

    @FindBy(id = "currentAddress")
    private WebElement currentAddressInput;

    @FindBy(css = "#state input")
    private WebElement stateInput;

    @FindBy(css = "#city input")
    private WebElement cityInput;

    @FindBy(id = "submit")
    private WebElement submitBtn;

//    div[class*='react-datepicker__day'][aria-label*='February']
//    div.react-datepicker__day:not(.react-datepicker__day--outside-month)
//    //div[contains(@class, 'react-datepicker__day')
//    and not(contains(@class, 'react-datepicker__day--outside-month')) and text() = '"+day+"']
//    //div[contains(@class, 'multi-value__label') and text()='" + subject3 + "']
//    //div[contains(@class, '-option') and text()='Agra']


    public PracticeFormPage fillUserFirstName(String firstName) {
        elementActions.inputText(firstNameInput, firstName);
        return this;
    }

    public PracticeFormPage fillUserLastName(String lastName) {
        elementActions.inputText(lastNameInput, lastName);
        return this;
    }

    public PracticeFormPage fillUserEmail(String email) {
        elementActions.inputText(userEmailInput, email);
        return this;
    }

    public PracticeFormPage selectGender(String gender) {
        switch (gender.toLowerCase().trim()) {
            case "female":
                elementActions.clickBtn(genderSelectFemale);
                break;
            case "male":
                elementActions.clickBtn(genderSelectMale);
                break;
            case "other":
                elementActions.clickBtn(genderSelectOther);
                break;
            default:
                throw new IllegalArgumentException("INVALID GENDER SELECTION");
        }
        return this;
    }

    public PracticeFormPage fillUserNumber(String number) { // 10 digits
        elementActions.inputText(userNumberInput, number);
        return this;
    }

    public PracticeFormPage fillDateOfBirth(String dateMonthYear) {
        String[] dateMonthYearParts = dateMonthYear.split(" ");

        String date = dateMonthYearParts[0];
        String month = dateMonthYearParts[1];
        String year = dateMonthYearParts[2];

        elementActions.clickBtn(datePickerInput);

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(15));

        WebElement monthDropDown = wait.until(ExpectedConditions.visibilityOfElementLocated
                (By.className("react-datepicker__month-select")));

        WebElement yearDropDown = wait.until(ExpectedConditions.visibilityOfElementLocated
                (By.className("react-datepicker__year-select")));

        elementActions.selectByVisibleText(monthDropDown, month);
        elementActions.selectByVisibleText(yearDropDown, year);

        WebElement dayElement = wait.until(ExpectedConditions.elementToBeClickable
                (By.xpath("//div[contains(@class, 'react-datepicker__day') " +
                        "and not(contains(@class, 'react-datepicker__day--outside-month')) " +
                        "and text() = '" + date + "']")));

        elementActions.clickBtn(dayElement);

        return this;
    }

    public PracticeFormPage fillUserSubjects(String subject) {
        elementActions.inputText(subjectsInput, subject);
        subjectsInput.sendKeys(Keys.TAB); // TAB лучше чем RETURN потому что таб перейдет на следующий а return может дать команду submit
        return this;
    }

    public PracticeFormPage fillSubject(String subject1, String subject2, String subject3) {

        elementActions.inputText(subjectsInput, subject1);
        subjectsInput.sendKeys(Keys.TAB);

        elementActions.inputText(subjectsInput, subject2);
        subjectsInput.sendKeys(Keys.TAB);

        elementActions.inputText(subjectsInput, subject3);
        subjectsInput.sendKeys(Keys.TAB);

        return this;
    }
//    через рандом выбрать нужное количество предметов из рандомного числа которе даст число в промежутке от 1 до allSubjects.size()
    public PracticeFormPage fillOutSubjects() {
        List<String> allSubjectsList = List.of("physics", "chemistry", "biology", "history", "civics",
                "computer science", "accounting", "social studies", "maths", "arts", "english", "economics",
                "commerce", "hindi"); // 14 subjects

        Random random = new Random();
        int n = random.nextInt(1, allSubjectsList.size());

        for (int i = 0; i < n; i++) {
            String subjectChoice = allSubjectsList.get(i);
            elementActions.inputText(subjectsInput,subjectChoice);
            subjectsInput.sendKeys(Keys.TAB);
        }
        return this;
    }

    public PracticeFormPage selectSportsAsHobbies() {
        elementActions.clickBtn(hobbiesSelectSports);
        return this;
    }

    public PracticeFormPage selectReadingAsHobbies() {
        elementActions.clickBtn(hobbiesSelectReading);
        return this;
    }

    public PracticeFormPage selectMusicAsHobbies() {
        elementActions.clickBtn(hobbiesSelectMusic);
        return this;
    }

    public PracticeFormPage uploadPicture(String pictureLink) {
        elementActions.inputText(uploadPicture, pictureLink);
        return this;
    }


    public PracticeFormPage fillUserCurrentAddress(String address) {
        elementActions.inputText(currentAddressInput, address);
        return this;
    }

    public PracticeFormPage selectStateAndCity(String state) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10));

        String stateToLowerCase = state.toLowerCase().trim();

        Map<String, List<String>> statesAndCity = new HashMap<>();
        statesAndCity.put("ncr", List.of("delhi", "gurgaon", "noida"));
        statesAndCity.put("uttar pradesh", List.of("agra", "lucknow", "merrut"));
        statesAndCity.put("haryana", List.of("karnal", "panipat"));
        statesAndCity.put("rajasthan", List.of("jaipur", "jaiselmer"));

        if (!statesAndCity.containsKey(stateToLowerCase)) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }

        elementActions.inputText(stateInput, state);
        stateInput.sendKeys(Keys.TAB);

        List<String> cities = statesAndCity.get(stateToLowerCase);
        String city = cities.get(new Random().nextInt(cities.size()));

        elementActions.inputText(cityInput, city);
        cityInput.sendKeys(Keys.TAB);

        return this;
    }

    public PracticeFormPage submit(){
        elementActions.clickBtn(submitBtn);
        return this;
    }


    public PracticeFormPage fillPracticeForm(UserPracticeForm userPracticeForm) {
        fillUserFirstName(userPracticeForm.getFirstName())
                .fillUserLastName(userPracticeForm.getLastName())
                .fillUserEmail(userPracticeForm.getEmail())
                .selectGender(userPracticeForm.getGender())
                .fillUserNumber(userPracticeForm.getMobileNumber())
                .fillOutSubjects()
                .selectSportsAsHobbies()
//                .selectReadingAsHobbies()
//                .selectMusicAsHobbies()
                .fillUserCurrentAddress(userPracticeForm.getCurrentAddress());
        return this;
    }


}
