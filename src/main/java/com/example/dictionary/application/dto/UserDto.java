package com.example.dictionary.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class UserDto {

    private Integer id;

    @NotEmpty(message = "First Name is required")
    private String firstName;

    @NotEmpty(message = "Last Name is required")
    private String lastName;

    @Email(message = "Incorrect email format")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "Key is required")
    private String key;

    private UserInfoDto userInfo;

    @JsonFormat(shape = STRING, pattern = "dd-MM-yyyy")
    private LocalDate registeredAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserInfoDto getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoDto userInfo) {
        this.userInfo = userInfo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LocalDate getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDate registeredAt) {
        this.registeredAt = registeredAt;
    }
}
