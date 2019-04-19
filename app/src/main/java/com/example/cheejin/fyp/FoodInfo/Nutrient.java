package com.example.cheejin.fyp.FoodInfo;

import java.io.Serializable;

public class Nutrient implements Serializable {
    private String calories;
    private String fat;
    private String protein;
    private String carbs;

    public Nutrient(){

    }

    public Nutrient(String calories, String fat , String protein, String carbs){
        if (calories.equals("null")){
            calories = String.valueOf(0);
        }else if (fat.equals("null")){
            fat = String.valueOf(0);
        }else if (protein.equals("null")){
            protein = String.valueOf(0);
        }else if (carbs.equals("null")){
            carbs = String.valueOf(0);
        }
        this.calories = calories;
        this.fat = fat;
        this.protein = protein;
        this.carbs = carbs;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }
}
