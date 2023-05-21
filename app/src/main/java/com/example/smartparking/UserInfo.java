package com.example.smartparking;

public class UserInfo {
    private String fullname,email,dob,gender,aadhar,mobile;
    public UserInfo(String fullname,String email,String dob,String gender,String aadhar,String mobile)
    {
        this.fullname=fullname;
        this.email=email;
        this.dob=dob;
        this.gender=gender;
        this.aadhar=aadhar;
        this.mobile=mobile;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }
    public String getMobile(){return mobile;}
    public void setMobile(String mobile){this.mobile=mobile;}



}
