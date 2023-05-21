package com.example.smartparking;

public class ReadWriteUserDetails {
    public String fullname,email,dob,gender,mobile;
    //constructor
    public ReadWriteUserDetails(){};
    public ReadWriteUserDetails(String fullname,String email,String dob,String gender,String mobile){
        this.fullname=fullname;
        this.email=email;
        this.dob=dob;
        this.gender=gender;
        this.mobile=mobile;
    }
}
