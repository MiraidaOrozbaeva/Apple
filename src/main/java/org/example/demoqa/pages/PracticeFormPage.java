package org.example.demoqa.pages;

import io.qameta.allure.Step;
import org.example.demoqa.drivers.DriverManager;
import org.example.demoqa.models.UserPracticeForm;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

    @Step("Fill user first name")
    public PracticeFormPage fillUserFirstName(String firstName) {
        elementActions.inputText(firstNameInput, firstName);
        return this;
    }

    @Step("Fill user last name")
    public PracticeFormPage fillUserLastName(String lastName) {
        elementActions.inputText(lastNameInput, lastName);
        return this;
    }

    @Step("Fill user email")
    public PracticeFormPage fillUserEmail(String email) {
        elementActions.inputText(userEmailInput, email);
        return this;
    }

    @Step("Select gender")
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

    @Step("Fill user mobile number")
    public PracticeFormPage fillUserNumber(String number) { // 10 digits
        elementActions.inputText(userNumberInput, number);
        return this;
    }

    @Step("Fill user date of birth")
    public PracticeFormPage fillDateOfBirth(String dateMonthYear) {
        String[] dateMonthYearParts = dateMonthYear.split(" ");

        String date = dateMonthYearParts[0];
        String month = dateMonthYearParts[1];
        String year = dateMonthYearParts[2];

        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", datePickerInput);

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

        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", dayElement);

        return this;
    }

    @Step("Fill user subjects filed")
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

    @Step("Select sports as hobbies")
    public PracticeFormPage selectSportsAsHobbies() {
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", hobbiesSelectSports);
        return this;
    }

    @Step("Select reading as hobbies")
    public PracticeFormPage selectReadingAsHobbies() {
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", hobbiesSelectReading);
        return this;
    }

    @Step("Select music as hobbies")
    public PracticeFormPage selectMusicAsHobbies() {
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", hobbiesSelectMusic);
        return this;
    }

    @Step("Upload picture")
    public PracticeFormPage uploadPicture(String pictureLink) {
        elementActions.inputText(uploadPicture, pictureLink);
        return this;
    }

    @Step("Fill user current address field")
    public PracticeFormPage fillUserCurrentAddress(String address) {
        elementActions.inputText(currentAddressInput, address);
        return this;
    }

    @Step("Select state and city")
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

    @Step("Click submit button")
    public PracticeFormPage submit(){
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", submitBtn);
        return this;
    }

    @Step("Fill out complete practice form")
    public PracticeFormPage fillPracticeForm(UserPracticeForm userPracticeForm) {
        fillUserFirstName(userPracticeForm.getFirstName())
                .fillUserLastName(userPracticeForm.getLastName())
                .fillUserEmail(userPracticeForm.getEmail())
                .selectGender(userPracticeForm.getGender())
                .fillUserNumber(userPracticeForm.getMobileNumber())
                .fillOutSubjects()
                .selectSportsAsHobbies()
                .selectReadingAsHobbies()
                .selectMusicAsHobbies()
                .fillUserCurrentAddress(userPracticeForm.getCurrentAddress());
        return this;
    }
}
