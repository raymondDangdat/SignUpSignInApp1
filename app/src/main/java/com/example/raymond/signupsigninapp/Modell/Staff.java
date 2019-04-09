package com.example.raymond.signupsigninapp.Modell;

public class Staff {
    private String fullName;
    private String email;
    private String image;

    public Staff() {
    }

    public Staff(String fullName, String email, String image) {
        this.fullName = fullName;
        this.email = email;
        this.image = image;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
