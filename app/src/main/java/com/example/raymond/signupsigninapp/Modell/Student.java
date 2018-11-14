package com.example.raymond.signupsigninapp.Modell;

public class Student {
    private String Surname;
    private String FirstName;
    private String ProfilePic;
    private String LastName;


    public Student() {
    }

    public Student(String surname, String firstName, String profilePic, String lastName) {
        Surname = surname;
        FirstName = firstName;
        ProfilePic = profilePic;
        LastName = lastName;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
