package com.example.hostelmanagementpro.model;

public class Organization {
    private String OrgName;
    private String OrgUsername;
    private String OrgPassword;
    private String OrgPhone;
    private String email;
    private String address;

    public Organization(){}

    public String getOrgName() {
        return OrgName;
    }

    public void setOrgName(String orgName) {
        OrgName = orgName;
    }

    public String getOrgUsername() {
        return OrgUsername;
    }

    public void setOrgUsername(String orgUsername) {
        OrgUsername = orgUsername;
    }

    public String getOrgPassword() {
        return OrgPassword;
    }

    public void setOrgPassword(String orgPassword) {
        OrgPassword = orgPassword;
    }

    public String getOrgPhone() {
        return OrgPhone;
    }

    public void setOrgPhone(String orgPhone) {
        OrgPhone = orgPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
