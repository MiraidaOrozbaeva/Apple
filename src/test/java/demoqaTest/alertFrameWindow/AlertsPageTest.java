package demoqaTest.alertFrameWindow;

import demoqaTest.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("UI")
public class AlertsPageTest extends BaseTest {

    @Test
    @Tag("SMOKE")
    @DisplayName("Click alert button -> accept; click confirm button -> dismiss; click prompt button -> send text")
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
