package com.example.hostelmanagementpro.model;

public class BedModel {
    private String bedNo;
    private String stuId;
    private String roomNo;
//    private String floorNo;
//    private String buildingNo;
//    private String orgId;

    public BedModel(){}

    public BedModel(String bedNo, String stuId, String roomNo){
        this.bedNo = bedNo;
        this.stuId = stuId;
        this.roomNo = roomNo;
    }

    public void setBedNo(String bedNo) { this.bedNo = bedNo; }
    public void setStuId(String stuId) { this.stuId = stuId; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
//    public void setFloorNo(String floorNo) { this.floorNo = floorNo; }
//    public void setBuildingNo(String buildingNo) { this.buildingNo = buildingNo; }
//    public void setOrgId(String orgId) { this.orgId = orgId; }

    public String getBedNo() { return bedNo; }
    public String getStuId() { return stuId; }
    public String getRoomNo() { return roomNo; }
//    public String getFloorNo() { return floorNo; }
//    public String getBuildingNo() { return buildingNo; }
//    public String getOrgId() { return orgId; }

}
