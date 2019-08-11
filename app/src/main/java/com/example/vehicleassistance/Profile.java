package com.example.vehicleassistance;

import java.util.HashMap;
import java.util.Map;

public class Profile {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean emailVerified;


    public Boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }


    public void setEmail(String firstName) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getEmail() {
        return email;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public String getUserId() {
        return userId;
    }

    public void getMap() {
        Map<String, Object> profile = new HashMap<>();
        profile.put("userId", userId);
        profile.put("firstName", firstName);
        profile.put("lastName", lastName);
    }

}
