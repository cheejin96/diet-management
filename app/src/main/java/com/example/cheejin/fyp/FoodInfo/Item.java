package com.example.cheejin.fyp.FoodInfo;

import java.io.Serializable;

public class Item implements Serializable{

    private String foodID;
    private String foodName;
    private Nutrient nutrient;
    private String foodBrand;
    private Integer quantity;

    public Item(){

    }

    public Item(String foodID, String foodName, String foodBrand, Nutrient nutrient, Integer quantity){
        this.foodID = foodID;
        this.foodName = foodName;
        this.foodBrand = foodBrand;
        this.nutrient = nutrient;
        this.quantity = quantity;
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Nutrient getNutrient() {
        return nutrient;
    }

    public void setNutrient(Nutrient nutrient) {
        this.nutrient = nutrient;
    }
    public String getFoodBrand() {
        return foodBrand;
    }

    public void setFoodBrand(String foodBrand) {
        this.foodBrand = foodBrand;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
