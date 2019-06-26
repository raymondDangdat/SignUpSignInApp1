package com.example.raymond.signupsigninapp.Modell;

public class ClearanceModel {
    private String fullname, profilePic, materials, status;

    public ClearanceModel() {
    }

    public ClearanceModel(String fullname, String profilePic, String materials, String status) {
        this.fullname = fullname;
        this.profilePic = profilePic;
        this.materials = materials;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }
}
