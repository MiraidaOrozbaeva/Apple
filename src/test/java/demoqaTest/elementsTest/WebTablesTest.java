package demoqaTest.elementsTest;

import demoqaTest.BaseTest;
import org.example.demoqa.models.Employee;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
@Tag("UI")
public class WebTablesTest extends BaseTest {


    @Test
    void webTablesTest(){
        driver.get("https://demoqa.com/webtables");
//        webTablesPage.clickAddBtn();
//        UserWebTables userWebTables = RandomUtils.generateWebTablesAddNewRecordForm();
//        webTablesPage.fillAddNewRecords(userWebTables);
//        webTablesPage.clickSubmitBtn();

        List<Employee> employeeList = webTablesPage.getEmployeesFromTable();

        for (Employee employee : employeeList){
            System.out.println(employee);
        }
    }

    @Test
    void addNewEmployeeTest(){
        browserHelper.open("https://demoqa.com/webtables");

        Employee koshei = new Employee("Koshei", "Bessmertnyi", 29, "koshei@example.com",
                2000, "Legal");

        webTablesPage.addNewEmployee(koshei);
    }

    @Test
    void editTest(){
        browserHelper.open("https://demoqa.com/webtables");

        Employee cierraUpdate = new Employee("Mierra", "Vega", 39, "cierra@example.com",
                10000, "Accounting");

        webTablesPage.editEmployee("Cierra", cierraUpdate);
    }

    @Test
    void removeTest(){
        browserHelper.open("https://demoqa.com/webtables");

        webTablesPage.removeEmployee("Cierra");
    }
}
