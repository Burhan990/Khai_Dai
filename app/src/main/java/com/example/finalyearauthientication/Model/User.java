package com.example.finalyearauthientication.Model;

public class User {

    private String Email;
    private String FirstName;
    private String LastName;
    private String Phone;
    private String Password;
    private String address;
    private String userStatus;
    private String profile_pic;

    public User() {
    }


    public User(String email, String firstName, String lastName, String phone, String password, String address, String userStatus, String profile_pic) {
        Email = email;
        FirstName = firstName;
        LastName = lastName;
        Phone = phone;
        Password = password;
        this.address = address;
        this.userStatus = userStatus;
        this.profile_pic = profile_pic;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
