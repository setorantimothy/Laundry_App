package com.erastimothy.laundry_app.model;



public class User {
    public String name,email,password,phoneNumber,uid;
    public boolean is_owner;

    public User(String uid,String name, String email, String password, String phoneNumber,boolean is_owner) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.is_owner = is_owner;
    }

    public boolean is_owner() {
        return is_owner;
    }

    public void setIs_owner(boolean is_owner){
        this.is_owner = is_owner;
    }

    public String getUid() {return uid;}

    public void setUid(String uid){this.uid = uid;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }   

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
