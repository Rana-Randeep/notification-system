package com.rana.notification_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserRequestDTO {

    @NotBlank(message="Name is Required")
    private String name;

    @NotBlank(message="Email is Required")
    @Email(message="Invalid email format")
    private String email;

    private String phoneNumber;
    private String deviceToken;

    public UserRequestDTO() {};

    public UserRequestDTO(String name, String email, String phoneNumber, String deviceToken) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.deviceToken = deviceToken;
    }

    //Getters

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    //Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
