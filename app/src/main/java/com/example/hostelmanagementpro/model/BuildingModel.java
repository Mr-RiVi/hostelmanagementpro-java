package com.example.hostelmanagementpro.model;

public class BuildingModel {
    private String number;
    private String name;
    private String gender;

    public BuildingModel() {}

    public BuildingModel(String number, String name, String gender) {
        this.number = number;
        this.name = name;
        this.gender = gender;
    }

    public void setNumber(String number) { this.number = number; }
    public void setName(String name) { this.name = name; }
    public void setGender(String gender) { this.gender = gender; }

    public String getNumber() { return number; }
    public String getName() { return name; }
    public String getGender() {
        return gender;
    }
}
