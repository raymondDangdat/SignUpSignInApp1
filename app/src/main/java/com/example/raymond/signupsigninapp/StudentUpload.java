package com.example.raymond.signupsigninapp;

public class StudentUpload {

    private String surname;
    private String firstName;
    private String username;
    private String email;
    private String gender;
    private String department;
    private String image;
    private String faculty;
    private String emergencyNo;
    private String phone;
    private String lastName;
    private String matNo;


    //empty constructor

    public StudentUpload() {
    }

    public StudentUpload(String surname, String firstName, String username, String email, String gender, String department, String image, String faculty, String emergencyNo, String phone, String lastName, String matNo) {
        this.surname = surname;
        this.firstName = firstName;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.department = department;
        this.image = image;
        this.faculty = faculty;
        this.emergencyNo = emergencyNo;
        this.phone = phone;
        this.lastName = lastName;
        this.matNo = matNo;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getEmergencyNo() {
        return emergencyNo;
    }

    public void setEmergencyNo(String emergencyNo) {
        this.emergencyNo = emergencyNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMatNo() {
        return matNo;
    }

    public void setMatNo(String matNo) {
        this.matNo = matNo;
    }
}
