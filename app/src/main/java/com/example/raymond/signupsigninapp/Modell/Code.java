package com.example.raymond.signupsigninapp.Modell;

public class Code {
    private String code, status;

    public Code() {
    }

    public Code(String code, String status) {
        this.code = code;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
