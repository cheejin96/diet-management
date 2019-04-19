package com.example.cheejin.fyp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cheejin.fyp.FoodInfo.Item;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class DisplayFoodDetail extends AppCompatActivity {

    TextView foodNameLabel, foodBrandLabel, foodCarbsLabel, foodFatsLabel, foodCaloriesLabel, foodProteinsLabel;
    Button addBtn;
    ImageButton backBtn;
    UserFoodRecord userFoodRecord;
    DatabaseReference databaseTracks;
    User user;
    String listToUpdate;
    Double tempCalories = 0.00;
    DecimalFormat df = new DecimalFormat("#.##");
    Spinner quantitySpinner;
    Integer tempQuantity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        final Item item = (Item) getIntent().getSerializableExtra("foodDetails");
        userFoodRecord = (UserFoodRecord) getIntent().getSerializableExtra("userFoodRecord");
        user = (User) getIntent().getSerializableExtra("user");
        listToUpdate = (String) getIntent().getSerializableExtra("listToUpdate");
        databaseTracks = FirebaseDatabase.getInstance().getReference("tracks").child(user.getUserId()).child(userFoodRecord.date);

        foodNameLabel = findViewById(R.id.exercName);
        foodBrandLabel = findViewById(R.id.foodBrandLabel);
        foodCarbsLabel = findViewById(R.id.foodCarbsLabel);
        foodFatsLabel = findViewById(R.id.foodFatsLabel);
        foodCaloriesLabel = findViewById(R.id.foodCaloriesLabel);
        foodProteinsLabel = findViewById(R.id.foodProteinsLabel);
        addBtn = findViewById(R.id.addBtn);
        backBtn = findViewById(R.id.backBtn);
        quantitySpinner = findViewById(R.id.quantitySpinner);

        if (item != null){
            foodNameLabel.setText(item.getFoodName());
            foodBrandLabel.setText(item.getFoodBrand());
            foodCarbsLabel.setText(item.getNutrient().getCarbs());
            foodFatsLabel.setText(item.getNutrient().getFat());
            foodCaloriesLabel.setText(item.getNutrient().getCalories());
            foodProteinsLabel.setText(item.getNutrient().getProtein());
        }

        quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tempQuantity = Integer.parseInt(quantitySpinner.getSelectedItem().toString());
                foodCarbsLabel.setText(String.valueOf(df.format(Double.valueOf(item.getNutrient().getCarbs())*tempQuantity)));
                foodFatsLabel.setText(String.valueOf(df.format(Double.valueOf(item.getNutrient().getFat())*tempQuantity)));
                foodCaloriesLabel.setText(String.valueOf(df.format(Double.valueOf(item.getNutrient().getCalories())*tempQuantity)));
                foodProteinsLabel.setText(String.valueOf(df.format(Double.valueOf(item.getNutrient().getProtein())*tempQuantity)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tempCalories = userFoodRecord.getFood();
                tempQuantity = Integer.parseInt(quantitySpinner.getSelectedItem().toString());
                item.setQuantity(tempQuantity);

                if (listToUpdate.equals("breakfast")){

                    if (userFoodRecord.breakfast == null){
                        List<Item> breakfast = new ArrayList<>();
                        breakfast.add(item);
                        userFoodRecord.setBreakfast(breakfast);
                        tempCalories += Double.parseDouble(item.getNutrient().getCalories())*tempQuantity;
                        userFoodRecord.setFood(Double.valueOf(df.format(tempCalories)));
                    }else{
                        List<Item> breakfast = userFoodRecord.getBreakfast();
                        breakfast.add(item);
                        tempCalories += Double.parseDouble(item.getNutrient().getCalories())*tempQuantity;
                        userFoodRecord.setFood(Double.valueOf(df.format(tempCalories)));
                    }
                }else if (listToUpdate.equals("lunch")){
                    if (userFoodRecord.lunch == null){
                        List<Item> lunch = new ArrayList<>();
                        lunch.add(item);
                        userFoodRecord.setLunch(lunch);
                        tempCalories += Double.parseDouble(item.getNutrient().getCalories())*tempQuantity;
                        userFoodRecord.setFood(Double.valueOf(df.format(tempCalories)));
                    }else{

                        List<Item> lunch = userFoodRecord.getLunch();
                        lunch.add(item);
                        tempCalories += Double.parseDouble(item.getNutrient().getCalories())*tempQuantity;
                        userFoodRecord.setFood(Double.valueOf(df.format(tempCalories)));
                    }
                }else if (listToUpdate.equals("dinner")){
                    if (userFoodRecord.dinner == null){
                        List<Item> dinner = new ArrayList<>();
                        dinner.add(item);
                        userFoodRecord.setDinner(dinner);
                        tempCalories += Double.parseDouble(item.getNutrient().getCalories())*tempQuantity;
                        userFoodRecord.setFood(Double.valueOf(df.format(tempCalories)));
                    }else{

                        List<Item> dinner = userFoodRecord.getDinner();
                        dinner.add(item);
                        tempCalories += Double.parseDouble(item.getNutrient().getCalories())*tempQuantity;
                        userFoodRecord.setFood(Double.valueOf(df.format(tempCalories)));
                    }
                }else if (listToUpdate.equals("other")){
                    if (userFoodRecord.other == null){
                        List<Item> other = new ArrayList<>();
                        other.add(item);
                        userFoodRecord.setOther(other);
                        tempCalories += Double.parseDouble(item.getNutrient().getCalories())*tempQuantity;
                        userFoodRecord.setFood(Double.valueOf(df.format(tempCalories)));
                    }else{
                        List<Item> other = userFoodRecord.getOther();
                        other.add(item);
                        tempCalories += Double.parseDouble(item.getNutrient().getCalories())*tempQuantity;
                        userFoodRecord.setFood(Double.valueOf(df.format(tempCalories)));
                    }
                }

                Double tempRemaining = userFoodRecord.goal - userFoodRecord.food + userFoodRecord.exerciseBurn;
                userFoodRecord.setRemaining(Double.valueOf(df.format(tempRemaining)));
                databaseTracks.setValue(userFoodRecord);
                Toast.makeText(DisplayFoodDetail.this, "Added Successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
