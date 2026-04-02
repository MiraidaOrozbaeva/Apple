package org.example.demoqa.models;

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

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    @Override
    public String toString() {
        return "UserTextBox{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", currentAddress='" + currentAddress + '\'' +
                ", permanentAddress='" + permanentAddress + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserTextBox that = (UserTextBox) o;
        return Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(currentAddress, that.currentAddress) && Objects.equals(permanentAddress, that.permanentAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, currentAddress, permanentAddress);
    }
}
