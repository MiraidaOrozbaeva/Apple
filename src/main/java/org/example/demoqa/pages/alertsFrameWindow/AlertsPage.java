package org.example.demoqa.pages.alertsFrameWindow;

import org.example.demoqa.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AlertsPage extends BasePage {

    @FindBy(id = "alertButton")
    private WebElement alertBtn;

    @FindBy(id = "timerAlertButton")
    private WebElement timerAlertBtn;

    @FindBy(id = "confirmButton")
    private WebElement confirmBtn;

    @FindBy(id = "promtButton")
    private WebElement promtBtn;


    public AlertsPage clickAlertBtn(){
        elementActions.clickBtn(alertBtn);
        return this;
    }

    public AlertsPage clickTimerAlertBtn(){
        elementActions.clickBtn(timerAlertBtn);
        return this;
    }

    public AlertsPage clickConfirmBtn(){
        elementActions.clickBtn(confirmBtn);
        return this;
    }

    public AlertsPage clickPromptBtn(){
        elementActions.clickBtn(promtBtn);
        return this;
    }
//    DEMOQA -> Elements -> Links
//    найти ссылки которые НЕ открывают новые вкладки
//    в один список WebElement положить все ссылки
//    найти те ссылки что НЕ перенаправляют в новое окно
//    и второй список которые выводят текст

//    BrokenLinks Images
//    тоже проверка

//    Upload and Download
//
// enum GroupHeader внутри константы всех pages внутри Elements .group-header 6шт
//    ELEMENTS("Elements"); -> constructor getter
//    enum Menu -> TEXT_BOX("Text Box"); -> constructor
//    method public void expandMenu(GroupHeader gh, Menu menu){
//
//    List<WebElement> groupHeader = driver.findElements(className = "group-header")
// через метод goBack вернуться потом вперед потом обновить страницу
}
