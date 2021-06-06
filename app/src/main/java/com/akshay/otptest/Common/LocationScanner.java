package com.akshay.otptest.Common;

import android.util.Log;

public class LocationScanner {
    private  String phoneNumber = null;
    private String first_name = null;
    private String last_name = null;
    private String doB = null;

    public LocationScanner(String phoneNumber, String first_name, String last_name, String doB, String gender, String college, String about, double lattitude, double longitude) {
        this.phoneNumber = phoneNumber;
        this.first_name = first_name;
        this.last_name = last_name;
        this.doB = doB;
        this.gender = gender;
        this.college = college;
        this.about = about;
        this.lattitude = lattitude;
        this.longitude = longitude;
    }

    private String gender = null;
    private String college = null;
    private String about = null;
    private double lattitude;
    private double longitude;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirst_name() {
        Log.d("KeyAks",first_name);
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDoB() {
        return doB;
    }

    public void setDoB(String doB) {
        this.doB = doB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
