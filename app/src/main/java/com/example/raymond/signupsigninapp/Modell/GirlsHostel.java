package com.example.raymond.signupsigninapp.Modell;

public class GirlsHostel {
    private String Room;
    private String Image;

    public GirlsHostel() {
    }

    public GirlsHostel(String room, String image) {
        Room = room;
        Image = image;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
