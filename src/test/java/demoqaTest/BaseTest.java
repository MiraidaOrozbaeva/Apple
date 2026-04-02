package demoqaTest;

import org.example.demoqa.drivers.DriverManager;
import org.example.demoqa.helper.AlertHelper;
import org.example.demoqa.helper.BrowserHelper;
import org.example.demoqa.helper.IframeHelper;
import org.example.demoqa.pages.*;
import org.example.demoqa.pages.alertsFrameWindow.AlertsPage;
import org.example.demoqa.pages.alertsFrameWindow.IframePage;
import org.example.demoqa.pages.alertsFrameWindow.WindowPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;

public class BaseTest {

    protected static WebDriver driver;
    protected static TextBoxPage textBoxPage;
    protected static CheckBoxPage checkBoxPage;
    protected static RadioButtonPage radioButtonPage;
    protected static WebTablesPage webTablesPage;
    protected static ButtonsPage buttonsPage;
    protected static LinksPage linksPage;
    protected static BrokenLinksPage brokenLinksPage;
    protected static UploadAndDownloadPage uploadAndDownloadPage;
    protected static DynamicPropertiesPage dynamicPropertiesPage;
    protected static PracticeFormPage practiceFormPage;
    protected static AlertsPage alertsPage;
    protected static AlertHelper alertHelper;
    protected static WindowPage windowPage;
    protected static BrowserHelper browserHelper;
    protected static IframeHelper iframeHelper;
    protected static IframePage iframePage;


    @BeforeAll
    public static void setUpBrowser(){
        driver = DriverManager.getDriver();
        textBoxPage = new TextBoxPage();
        checkBoxPage = new CheckBoxPage();
        radioButtonPage = new RadioButtonPage();
        webTablesPage = new WebTablesPage();
        buttonsPage = new ButtonsPage();
        linksPage = new LinksPage();
        brokenLinksPage = new BrokenLinksPage();
        uploadAndDownloadPage = new UploadAndDownloadPage();
        dynamicPropertiesPage = new DynamicPropertiesPage();
        practiceFormPage = new PracticeFormPage();
        alertsPage = new AlertsPage();
        alertHelper = new AlertHelper(driver);
        windowPage = new WindowPage();
        browserHelper = new BrowserHelper(driver);
        iframeHelper = new IframeHelper(driver);
        iframePage = new IframePage();

    }

    @AfterAll
    public static void tearDown(){
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        DriverManager.closeDriver();
    }
}
