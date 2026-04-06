package org.example.demoqa.models;

import io.qameta.allure.Step;

import java.util.Objects;

public class UserTextBox {

    private String name;
    private String email;
    private String currentAddress;
    private String permanentAddress;

    public UserTextBox() {
    }

    public UserTextBox(String name, String email, String currentAddress, String permanentAddress) {
        this.name = name;
        this.email = email;
        this.currentAddress = currentAddress;
        this.permanentAddress = permanentAddress;
    }

    @Step("Get name")
    public String getName() {
        return name;
    }

    @Step("Get email")
    public String getEmail() {
        return email;
    }

    @Step("Get current address")
    public String getCurrentAddress() {
        return currentAddress;
    }

    @Step("Get permanent address")
    public String getPermanentAddress() {
        return permanentAddress;
    }

    @Step("Set name")
    public void setName(String name) {
        this.name = name;
    }

    @Step("Set email")
    public void setEmail(String email) {
        this.email = email;
    }

    @Step("Set current address")
    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    @Step("Set permanent address")
    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    @Step("To String")
    @Override
    public String toString() {
        return "UserTextBox{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", currentAddress='" + currentAddress + '\'' +
                ", permanentAddress='" + permanentAddress + '\'' +
                '}';
    }

    @Step("Equals")
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserTextBox that = (UserTextBox) o;
        return Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(currentAddress, that.currentAddress) && Objects.equals(permanentAddress, that.permanentAddress);
    }

    @Step("HashCode")
    @Override
    public int hashCode() {
        return Objects.hash(name, email, currentAddress, permanentAddress);
    }
}
