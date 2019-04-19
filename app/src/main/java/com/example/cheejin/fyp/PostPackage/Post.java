package com.example.cheejin.fyp.PostPackage;

public class Post {
    String username;
    String status;
    String photo;

    public Post(){

    }
    public Post(String username, String status){
        this.username = username;
        this.status = status;
        this.photo = "";
    }

    public Post(String username, String status, String photo){
        this.username = username;
        this.status = status;
        this.photo = photo;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
