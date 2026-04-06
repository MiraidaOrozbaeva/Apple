package org.example.demoqa.pages;

import io.qameta.allure.Step;
import org.example.demoqa.drivers.DriverManager;
import org.example.demoqa.models.Employee;
import org.example.demoqa.models.UserWebTables;
import org.example.demoqa.utils.RandomUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.example.demoqa.drivers.ChromeWebDriver.driver;

public class WebTablesPage extends BasePage {

    @FindBy(id = "searchBox")
    private WebElement searchBoxInput;

    @FindBy(id = "addNewRecordButton")
    private WebElement addNewRecordBtn;

    @FindBy(id = "firstName")
    private WebElement firstNameInput;

    @FindBy(id = "lastName")
    private WebElement lastNameInput;

    @FindBy(id = "userEmail")
    private WebElement userEmailInput;

    @FindBy(id = "age")
    private WebElement ageInput;

    @FindBy(id = "salary")
    private WebElement salaryInput;

    @FindBy(id = "department")
    private WebElement departmentInput;

    @FindBy(id = "submit")
    private WebElement submitBtn;

    @FindBy(css = "tbody tr")
    private List<WebElement> rowsList;

    @Step("Click add button")
    public WebTablesPage clickAddBtn() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addNewRecordBtn);
        return this;
    }

    @Step("Fill first name field")
    public WebTablesPage fillFirstName(String firstName) {
        elementActions.inputText(firstNameInput, firstName);
        return this;
    }

    @Step("Fill last name field")
    public WebTablesPage fillLastName(String lastName) {
        elementActions.inputText(lastNameInput, lastName);
        return this;
    }

    @Step("Fill email field")
    public WebTablesPage fillEmail(String email) {
        elementActions.inputText(userEmailInput, email);
        return this;
    }

    @Step("Fill age field")
    public WebTablesPage fillAge(Integer age) {
        elementActions.inputText(ageInput, age.toString());
        return this;
    }

    @Step("Fill salary field")
    public WebTablesPage fillSalary(Integer salary) {
        elementActions.inputText(salaryInput, salary.toString());
        return this;
    }

    @Step("Fill department field")
    public WebTablesPage fillDepartment(String department) {
        elementActions.inputText(departmentInput, department);
        return this;
    }

    @Step("Click submit button")
    public WebTablesPage clickSubmitBtn() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
        return this;
    }

    @Step("Fill in Registration form")
    public WebTablesPage fillAddNewRecords(UserWebTables userWebTables) {
        fillFirstName(userWebTables.getFirstName())
                .fillLastName(userWebTables.getLastName())
                .fillEmail(userWebTables.getEmail())
                .fillAge(userWebTables.getAge())
                .fillSalary(userWebTables.getSalary())
                .fillDepartment(userWebTables.getDepartment());
        return this;
    }

    @Step("Add new employee")
    public WebTablesPage addNewEmployee(Employee employee) {
        boolean isExist = getEmployeesFromTable().contains(employee);

        if (!isExist){
            clickAddBtn();
            fillFirstName(employee.getFirstName())
                    .fillLastName(employee.getLastName())
                    .fillAge(employee.getAge())
                    .fillEmail(employee.getEmail())
                    .fillSalary(employee.getSalary())
                    .fillDepartment(employee.getDepartment());
            clickSubmitBtn();
        } else{
            throw new IllegalArgumentException("Employee is exist");
        }
        return this;
    }

    @Step("Clear and type")
    public void clearAndType(WebElement element, String value){
        element.clear();
        element.sendKeys(value);
    }

    @Step("Edit employee and update")
    public WebTablesPage editEmployee(String name, Employee updatedEmployeeInfo){
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5));
        for (Employee emp : getEmployeesFromTable()){
            boolean isExist = emp.getFirstName().equals(name);
            if (isExist){
                WebElement edit = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                        "//td[text()='" + name + "']/ancestor::tr//span[starts-with(@id,'edit')]")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", edit);
            }
        }
        return this;
    }

    @Step("Remove employee")
    public WebTablesPage removeEmployee(String name){
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5));
        WebElement delete = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                "//td[text()='" + name + "']/ancestor::tr//span[starts-with(@id,'delete')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", delete);
        return this;
    }

    @Step("Get list of employees")
    public ArrayList<Employee> getEmployeesFromTable() {
        ArrayList<Employee> employees = new ArrayList<>();
        for (WebElement row : rowsList) {
            List<WebElement> cells = row.findElements(By.cssSelector("td"));

            String firstName = cells.getFirst().getText();
            String lastName = cells.get(1).getText();
            String ageTxt = cells.get(2).getText().replaceAll("[^0-9]", ""); // regex 0-9 replaceAll except 0-9
            String email = cells.get(3).getText();
            String salaryTxt = cells.get(4).getText().replaceAll("[^0-9]", "");
            String department = cells.get(5).getText();

            if (firstName.isEmpty() || lastName.isEmpty() || ageTxt.isEmpty() || email.isEmpty() || salaryTxt.isEmpty() ||
                    department.isEmpty()) {
                continue;
            }

            int age = Integer.parseInt(ageTxt.trim());
            int salary = Integer.parseInt(salaryTxt.trim());

            employees.add(new Employee(firstName, lastName, age, email, salary, department));
        }
        return employees;
    }
}
