package com.example.cheejin.fyp;

import com.example.cheejin.fyp.Exercise.Exercise;
import com.example.cheejin.fyp.FoodInfo.Item;

import java.io.Serializable;
import java.util.List;

public class UserFoodRecord implements Serializable {
    String date;
    List<Item> breakfast;
    List<Item> lunch;
    List<Item> dinner;
    List<Item> other;
    List<Exercise> exercise;
    Double goal = 0.00;
    Double food = 0.00;
    Double exerciseBurn = 0.00;
    Double remaining = 0.00;

    public UserFoodRecord() {
    }

    public UserFoodRecord(String date, List<Item> breakfast, List<Item> lunch, List<Item> dinner, List<Item> other, List<Exercise> exercise, Double goal, Double food, Double exerciseBurn) {
        this.date = date;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.other = other;
        this.exercise = exercise;
        this.goal = goal;
        this.food = food;
        this.exerciseBurn = exerciseBurn;

        Double tempRemain = goal - food + exerciseBurn;
        this.remaining = tempRemain;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Item> getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(List<Item> breakfast) {
        this.breakfast = breakfast;
    }

    public List<Item> getLunch() {
        return lunch;
    }

    public void setLunch(List<Item> lunch) {
        this.lunch = lunch;
    }

    public List<Item> getDinner() {
        return dinner;
    }

    public void setDinner(List<Item> dinner) {
        this.dinner = dinner;
    }

    public List<Item> getOther() {
        return other;
    }

    public void setOther(List<Item> other) {
        this.other = other;
    }

    public List<Exercise> getExercise() {
        return exercise;
    }

    public void setExercise(List<Exercise> exercise) {
        this.exercise = exercise;
    }

    public Double getGoal() {
        return goal;
    }

    public void setGoal(Double goal) {
        this.goal = goal;
    }

    public Double getFood() {
        return food;
    }

    public void setFood(Double food) {
        this.food = food;
    }

    public Double getExerciseBurn() {
        return exerciseBurn;
    }

    public void setExerciseBurn(Double exerciseBurn) {
        this.exerciseBurn = exerciseBurn;
    }

    public Double getRemaining() {
        return remaining;
    }

    public void setRemaining(Double remaining) {
        this.remaining = remaining;
    }
}
