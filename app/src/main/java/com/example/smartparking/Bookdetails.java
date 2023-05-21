package com.example.smartparking;
public class Bookdetails {
private String phone,current,destination,vechicletype,vechileno,date,duration;
public Bookdetails(){}

    public Bookdetails(String phone, String current, String destination, String vechicletype, String vechileno, String date, String duration) {
        this.phone = phone;
        this.current = current;
        this.destination = destination;
        this.vechicletype = vechicletype;
        this.vechileno = vechileno;
        this.date = date;
        this.duration = duration;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getVechicletype() {
        return vechicletype;
    }

    public void setVechicletype(String vechicletype) {
        this.vechicletype = vechicletype;
    }

    public String getVechileno() {
        return vechileno;
    }

    public void setVechileno(String vechileno) {
        this.vechileno = vechileno;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}





