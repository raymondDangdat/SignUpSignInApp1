package com.example.raymond.signupsigninapp.Modell;

public class Occupants {

    private String fullName;
    private String gender;
    private String department;
    private String profilePic;
    private String parentNo;
    private String phone;
    private String matNo;
    private String JAMB;
    private String faculty;
    private String bedNumber;
    private String chaletName;

    public Occupants() {
    }

    public Occupants(String fullName, String gender, String department, String profilePic, String parentNo, String phone, String matNo, String JAMB,
                     String faculty, String bedNumber, String chaletName) {
        this.fullName = fullName;
        this.gender = gender;
        this.department = department;
        this.profilePic = profilePic;
        this.parentNo = parentNo;
        this.phone = phone;
        this.matNo = matNo;
        this.JAMB = JAMB;
        this.faculty = faculty;
        this.bedNumber = bedNumber;
        this.chaletName = chaletName;
    }

    public String getJAMB() {
        return JAMB;
    }

    public void setJAMB(String JAMB) {
        this.JAMB = JAMB;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public String getChaletName() {
        return chaletName;
    }

    public void setChaletName(String chaletName) {
        this.chaletName = chaletName;
    }

    public void setParentNo(String parentNo) {
        this.parentNo = parentNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getParentNo() {
        return parentNo;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMatNo() {
        return matNo;
    }

    public void setMatNo(String matNo) {
        this.matNo = matNo;
    }
}
