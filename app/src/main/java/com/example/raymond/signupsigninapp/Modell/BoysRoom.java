package com.example.raymond.signupsigninapp.Modell;

public class BoysRoom {
    private String BedNumber;
    private String RoomDescription;
    private String Image;
    private String Status;
    private String Room;

    public BoysRoom() {
    }

    public BoysRoom(String bedNumber, String roomDescription, String status, String room, String image) {
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
