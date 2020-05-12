package com.cs.mapstutorial;

public class UserInfo {

    String id, name, phnumber, aadhar, address;

    public UserInfo(){

    }

    public UserInfo(String id, String name, String phnumber, String aadhar, String address) {
        this.id = id;
        this.name = name;
        this.phnumber = phnumber;
        this.aadhar = aadhar;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhnumber() {
        return phnumber;
    }

    public String getAadhar() {
        return aadhar;
    }

    public String getAddress() {
        return address;
    }
}