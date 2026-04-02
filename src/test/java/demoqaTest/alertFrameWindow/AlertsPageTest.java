package demoqaTest.alertFrameWindow;

import demoqaTest.BaseTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
@Tag("UI")
public class AlertsPageTest extends BaseTest {

    @Test
    void alertsTest() {
        driver.get("https://demoqa.com/alerts");
        alertsPage.clickAlertBtn();
        alertHelper.accept();

        alertsPage.clickConfirmBtn();
        alertHelper.dismiss();

        alertsPage.clickPromptBtn();
        alertHelper.sendKeys("Miraida").accept();
    }
}
