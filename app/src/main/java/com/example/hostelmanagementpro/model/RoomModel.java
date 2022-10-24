package com.example.hostelmanagementpro.model;

public class RoomModel {
    private String number;
    private String roomType;
    private String roomStatus;
    private String stuCount;
    private String bedCount;
    private String floorNo;
    private String buildingNo;



    public void setNumber(String number) { this.number = number; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public void setRoomStatus(String roomStatus) { this.roomStatus = roomStatus; }
    public void setStuCount(String stuCount) { this.stuCount = stuCount; }
    public void setBedCount(String bedCount) { this.bedCount = bedCount; }
    public void setFloorNo(String floorNo) { this.floorNo = floorNo; }
    public void setBuildingNo(String buildingNo) { this.buildingNo = buildingNo; }

    public String getNumber() { return number; }
    public String getRoomType() { return roomType; }
    public String getRoomStatus() { return roomStatus; }
    public String getStuCount() { return stuCount; }
    public String getBedCount() { return bedCount; }
    public String getFloorNo() { return floorNo; }
    public String getBuildingNo() { return buildingNo; }

}
