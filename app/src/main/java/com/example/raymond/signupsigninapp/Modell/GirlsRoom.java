package com.example.raymond.signupsigninapp.Modell;

public class GirlsRoom {
    private String BedNumber;
    private String RoomDescription;
    private String Image;
    private String Room;
    private String Status;

    public GirlsRoom() {
    }

    public GirlsRoom(String bedNumber, String roomDescription, String room,String status, String image) {
        BedNumber = bedNumber;
        RoomDescription = roomDescription;
        Image = image;
        Room = room;
        Status = status;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getBedNumber() {
        return BedNumber;
    }

    public void setBedNumber(String bedNumber) {
        BedNumber = bedNumber;
    }

    public String getRoomDescription() {
        return RoomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        RoomDescription = roomDescription;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

}
