package com.example.raymond.signupsigninapp.Modell;

public class Students {
    private String Surname, FirstName, LastName, BedNumber, ChaletName, Email, MatNo, Phone, ParentNo, Gender, ProfilePic;

    public Students() {
    }

    public Students(String surname, String firstName, String lastName, String bedNumber, String chaletName, String email, String matNo, String phone, String parentNo, String gender, String profilePic) {
        Surname = surname;
        FirstName = firstName;
        LastName = lastName;
        BedNumber = bedNumber;
        ChaletName = chaletName;
        Email = email;
        MatNo = matNo;
        Phone = phone;
        ParentNo = parentNo;
        Gender = gender;
        ProfilePic = profilePic;
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

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getBedNumber() {
        return BedNumber;
    }

    public void setBedNumber(String bedNumber) {
        BedNumber = bedNumber;
    }

    public String getChaletName() {
        return ChaletName;
    }

    public void setChaletName(String chaletName) {
        ChaletName = chaletName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMatNo() {
        return MatNo;
    }

    public void setMatNo(String matNo) {
        MatNo = matNo;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getParentNo() {
        return ParentNo;
    }

    public void setParentNo(String parentNo) {
        ParentNo = parentNo;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }
}
