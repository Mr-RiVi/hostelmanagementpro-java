package com.example.hostelmanagementpro.model;

public class RoomModel {
    private String roomNo;
    private String roomType;
    private String roomStatus;
    private String stuCount;
    private String bedCount;
//    private String floorNo;
//    private String buildingNo;

    public RoomModel(){}

    public RoomModel(String roomNo, String roomType,String roomStatus, String stuCount, String bedCount){
        this.roomNo = roomNo;
        this.roomType = roomType;
        this.roomStatus = roomStatus;
        this.stuCount = stuCount;
        this.bedCount = bedCount;
    }

    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public void setRoomStatus(String roomStatus) { this.roomStatus = roomStatus; }
    public void setStuCount(String stuCount) { this.stuCount = stuCount; }
    public void setBedCount(String bedCount) { this.bedCount = bedCount; }
//    public void setFloorNo(String floorNo) { this.floorNo = floorNo; }
//    public void setBuildingNo(String buildingNo) { this.buildingNo = buildingNo; }

    public String getRoomNo() { return roomNo; }
    public String getRoomType() { return roomType; }
    public String getRoomStatus() { return roomStatus; }
    public String getStuCount() { return stuCount; }
    public String getBedCount() { return bedCount; }
//    public String getFloorNo() { return floorNo; }
//    public String getBuildingNo() { return buildingNo; }

}
