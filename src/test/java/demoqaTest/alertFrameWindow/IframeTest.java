package demoqaTest.alertFrameWindow;

import demoqaTest.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
@Tag("UI")
public class IframeTest extends BaseTest {

    @Test
    @Tag("SMOKE")
    @DisplayName("Switch to frame1 -> get text; switch to frame2 -> get text")
    void iframeTest(){
        browserHelper.open("https://demoqa.com/frames");
        iframeHelper.switchToFrame("frame1");
        String txt = iframePage.getSampleHeadingText();
        System.out.println(txt);

        iframeHelper.switchToParentFrame(); // без перехода на родителя ищет frame2 в frame1
        iframeHelper.switchToFrame("frame2");
        String txt2 = iframePage.getSampleHeadingText();
        System.out.println(txt2);

    }
}
