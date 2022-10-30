package com.example.hostelmanagementpro.model;

import android.widget.TextView;

public class StuProfiles {

    String stuID;
    String stuName;
    String stuMobile;
    String stuEmgMobile;
    String credID;

    public StuProfiles(String stuID, String stuName, String stuMobile, String stuEmgMobile,String credID) {
        this.stuID = stuID;
        this.stuName = stuName;
        this.stuMobile = stuMobile;
        this.stuEmgMobile = stuEmgMobile;
        this.credID = credID;
    }

    public String getStuID() {
        return stuID;
    }

    public String getStuName() {
        return stuName;
    }

    public String getStuMobile() {
        return stuMobile;
    }

    public String getStuEmgMobile() {
        return stuEmgMobile;
    }

    public String getCredID() {
        return credID;
    }
}
