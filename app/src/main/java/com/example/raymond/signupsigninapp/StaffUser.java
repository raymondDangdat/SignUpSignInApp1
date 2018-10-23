package com.example.raymond.signupsigninapp;

public class StaffUser {
    private String Name, Email, Password;

    public StaffUser() {
    }

    public StaffUser(String name, String email, String password) {
        Name = name;
        Email = email;
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
