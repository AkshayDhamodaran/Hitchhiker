package com.akshay.otptest;

public class HelperClass {

    String firstname, lastname, DoB, College, PhoneNumber, Gender,about;
    double Lattitude,Longitude;

    public HelperClass(String first_name, String last_name, String doB, String college, String phoneNumber, String gender, double lattitude, double longitude, String About) {
        firstname = first_name;
        lastname = last_name;
        DoB = doB;
        College = college;
        PhoneNumber = phoneNumber;
        Gender = gender;
        Lattitude = lattitude;
        Longitude = longitude;
        about = About;
    }

    public String getFirst_name() {
        return firstname;
    }

    public void setFirst_name(String first_name) {
        firstname = first_name;
    }

    public String getLast_name() {
        return lastname;
    }

    public void setLast_name(String last_name) {
        lastname = last_name;
    }

    public String getDoB() {
        return DoB;
    }

    public void setDoB(String doB) {
        DoB = doB;
    }

    public String getCollege() {
        return College;
    }

    public void setCollege(String college) {
        College = college;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String About) {
        about = About;
    }

    public double getLattitude() {
        return Lattitude;
    }

    public void setLattitude(double lattitude) {
        Lattitude = lattitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}