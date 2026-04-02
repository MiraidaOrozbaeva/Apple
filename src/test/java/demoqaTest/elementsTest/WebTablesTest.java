package demoqaTest.elementsTest;

import demoqaTest.BaseTest;
import org.example.demoqa.models.Employee;
import org.example.demoqa.models.UserWebTables;
import org.example.demoqa.utils.RandomUtils;
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
        Employee cierra = new Employee("Cierra", "Vega", 39, "cierra@example.com",
                10000, "Accounting");

        Employee kierra = new Employee("Kierra", "Gentry", 29, "kierra@example.com",
                2000, "Legal");

        webTablesPage.addNewEmployee(cierra);

    }

    @Test
    void editTest(){
        browserHelper.open("https://demoqa.com/webtables");

//        webTablesPage.editEmployee("Cierra");
    }

    @Test
    void removeTest(){
        browserHelper.open("https://demoqa.com/webtables");

        webTablesPage.removeEmployee("Cierra");
    }


}
