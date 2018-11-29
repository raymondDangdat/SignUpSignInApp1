package com.example.raymond.signupsigninapp.Modell;

public class JambConfirmationModel {
    private String jambNo, status;


    public JambConfirmationModel() {
    }

    public JambConfirmationModel(String jambNo, String status) {
        this.jambNo = jambNo;
        this.status = status;
    }

    public String getJambNo() {
        return jambNo;
    }

    public void setJambNo(String jambNo) {
        this.jambNo = jambNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
