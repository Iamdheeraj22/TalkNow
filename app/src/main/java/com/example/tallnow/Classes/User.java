package com.example.tallnow.Classes;

public class User
{
    private String id;
    private String username;
    private String imageurl;
    private String status;
    private String search;
    private String about;
    private String user;
    private String mood;
    private String fullname;
    private String gender;
    private String email;

    public User (String id, String username, String imageurl, String status, String search, String about, String user, String mood, String fullname, String gender, String email) {
        this.id = id;
        this.username = username;
        this.imageurl = imageurl;
        this.status = status;
        this.search = search;
        this.about = about;
        this.user = user;
        this.mood = mood;
        this.fullname = fullname;
        this.gender = gender;
        this.email = email;
    }

    public String getUser () {
        return user;
    }

    public void setUser (String user) {
        this.user = user;
    }

    public String getMood () {
        return mood;
    }

    public void setMood (String mood) {
        this.mood = mood;
    }

    public String getFullname () {
        return fullname;
    }

    public void setFullname (String fullname) {
        this.fullname = fullname;
    }

    public String getGender () {
        return gender;
    }

    public void setGender (String gender) {
        this.gender = gender;
    }

    public User(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
