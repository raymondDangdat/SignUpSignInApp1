package com.example.raymond.signupsigninapp.Modell;

import java.util.Date;

public class Pin {
    private int Pin;
    private long PinTime;
    private String status;

    public Pin() {
    }

    public Pin(int pin, String status) {
        Pin = pin;

        PinTime = new Date().getTime();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPin() {
        return Pin;
    }

    public void setPin(int pin) {
        Pin = pin;
    }

    public long getPinTime() {
        return PinTime;
    }

    public void setPinTime(long pinTime) {
        PinTime = pinTime;
    }
}
