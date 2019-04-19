package com.example.cheejin.fyp.Exercise;

import java.io.Serializable;

public class Exercise implements Serializable {
    String name;
    Double calories;
    Integer duration;
    Double totalCaloriesBurn;

    public Exercise(){
    }

    public Exercise(String name, Double calories, Integer duration){
        this.name = name;
        this.calories = calories;
        this.duration = duration;
        Double temp = calories * duration/30;
        this.totalCaloriesBurn = temp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Double getTotalCaloriesBurn() {
        return totalCaloriesBurn;
    }

    public void setTotalCaloriesBurn(Double totalCaloriesBurn) {
        this.totalCaloriesBurn = totalCaloriesBurn;
    }
}
