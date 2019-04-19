package com.example.cheejin.fyp;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class User implements Serializable{
    String id;
    String username;
    String userpwd;
    String gender;
    Double height;
    Double weight;
    String goal;
    Double userbmi;
    Integer age;

    public User(){

    }

    public User(String id, String username, String userpwd, String gender,Integer age, Double height, Double weight, String goal){
        this.id = id;
        this.username = username;
        this.userpwd = userpwd;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.goal = goal;

        Double tempBmi = (weight/(height/100))/(height/100);
        this.userbmi = tempBmi;
    }

    public String getUserId() {
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

    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }

    public String getUserGender() {
        return gender;
    }

    public void setUserGender(String gender) {
        this.gender = gender;
    }

    public Double getUserHeight() {
        return height;
    }

    public void setUserHeight(Double height) {
        this.height = height;
    }

    public Double getUserWeight() {
        return weight;
    }

    public void setUserWeight(Double weight) {
        this.weight = weight;
    }

    public String getUserGoal() {
        return goal;
    }

    public void setUserGoal(String goal) {
        this.goal = goal;
    }

    public Double getUserbmi() {
        return userbmi;
    }

    public void setUserbmi(Double userbmi) {
        this.userbmi = userbmi;
    }

    public Integer getUserAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void updateBMI(){
        Double tempBmi = (weight/(height/100))/(height/100);
        setUserbmi(tempBmi);
    }
}
