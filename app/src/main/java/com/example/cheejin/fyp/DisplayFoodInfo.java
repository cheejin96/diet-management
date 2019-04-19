package com.example.cheejin.fyp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cheejin.fyp.FoodInfo.Item;

public class DisplayFoodInfo  extends AppCompatActivity {

    TextView foodNameLabel, foodBrandLabel, foodCarbsLabel, foodFatsLabel, foodCaloriesLabel, foodProteinsLabel;
    ImageButton backBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_food_info);

        Item item = (Item) getIntent().getSerializableExtra("foodDetails");

        foodNameLabel = findViewById(R.id.exercName);
        foodBrandLabel = findViewById(R.id.foodBrandLabel);
        foodCarbsLabel = findViewById(R.id.foodCarbsLabel);
        foodFatsLabel = findViewById(R.id.foodFatsLabel);
        foodCaloriesLabel = findViewById(R.id.foodCaloriesLabel);
        foodProteinsLabel = findViewById(R.id.foodProteinsLabel);
        backBtn = findViewById(R.id.backBtn);

        if (item != null){
            foodNameLabel.setText(item.getFoodName());
            foodBrandLabel.setText(item.getFoodBrand());
            foodCarbsLabel.setText(item.getNutrient().getCarbs());
            foodFatsLabel.setText(item.getNutrient().getFat());
            foodCaloriesLabel.setText(item.getNutrient().getCalories());
            foodProteinsLabel.setText(item.getNutrient().getProtein());
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
