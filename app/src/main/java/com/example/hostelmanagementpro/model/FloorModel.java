package com.example.hostelmanagementpro.model;

public class FloorModel {
    private String number;
    private String roomCount;

    public FloorModel() {}

    public FloorModel(String number, String roomCount){
        this.number = number;
        this.roomCount = roomCount;
    }

    public void setNumber(String number) { this.number = number; }

    public void setRoomCount(String roomCount) { this.roomCount = roomCount; }


    public String getNumber() { return number; }

    public String getRoomCount() { return roomCount; }
}
