package com.example.cheejin.fyp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cheejin.fyp.FoodInfo.Item;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ManageFoodList extends AppCompatActivity {

    TextView foodNameLabel, foodBrandLabel, foodCarbsLabel, foodFatsLabel, foodCaloriesLabel, foodProteinsLabel, quantityText;
    Button removeBtn;
    ImageButton backBtn;
    UserFoodRecord userFoodRecord;
    DatabaseReference databaseTracks;
    User user;
    String listToUpdate;
    Double tempCalories = 0.00;
    DecimalFormat df = new DecimalFormat("#.##");
    Integer tempQuantity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_food_list);

        final Item item = (Item) getIntent().getSerializableExtra("foodDetails");
        userFoodRecord = (UserFoodRecord) getIntent().getSerializableExtra("userFoodRecord");
        user = (User) getIntent().getSerializableExtra("user");
        listToUpdate = (String) getIntent().getSerializableExtra("listToUpdate");
        databaseTracks = FirebaseDatabase.getInstance().getReference("tracks").child(user.getUserId()).child(userFoodRecord.date);

        quantityText = findViewById(R.id.quantityText);
        foodNameLabel = findViewById(R.id.exercName);
        foodBrandLabel = findViewById(R.id.foodBrandLabel);
        foodCarbsLabel = findViewById(R.id.foodCarbsLabel);
        foodFatsLabel = findViewById(R.id.foodFatsLabel);
        foodCaloriesLabel = findViewById(R.id.foodCaloriesLabel);
        foodProteinsLabel = findViewById(R.id.foodProteinsLabel);
        removeBtn = findViewById(R.id.removeBtn);
        backBtn = findViewById(R.id.backBtn);

        if (item != null){
            foodNameLabel.setText(item.getFoodName());
            foodBrandLabel.setText(item.getFoodBrand());
            foodCarbsLabel.setText(String.valueOf(df.format(Double.valueOf(item.getNutrient().getCarbs())*item.getQuantity())));
            foodFatsLabel.setText(String.valueOf(df.format(Double.valueOf(item.getNutrient().getFat())*item.getQuantity())));
            foodCaloriesLabel.setText(String.valueOf(df.format(Double.valueOf(item.getNutrient().getCalories())*item.getQuantity())));
            foodProteinsLabel.setText(String.valueOf(df.format(Double.valueOf(item.getNutrient().getProtein())*item.getQuantity())));
            quantityText.setText(item.getQuantity().toString());
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempCalories = userFoodRecord.getFood();
                tempQuantity = item.getQuantity();

                if (listToUpdate.equals("breakfast")){

                    List<Item> breakfast = userFoodRecord.getBreakfast();
                    List<Item> tempBreakfast = new ArrayList<>();

                    for (Item i : breakfast){
                        if (!i.getFoodName().equals(item.getFoodName())){
                            tempBreakfast.add(i);
                        }
                    }
                    userFoodRecord.setBreakfast(tempBreakfast);
                    tempCalories -= Double.parseDouble(item.getNutrient().getCalories())*tempQuantity;
                    userFoodRecord.setFood(Double.valueOf(df.format(tempCalories)));

                }else if (listToUpdate.equals("lunch")){

                    List<Item> lunch = userFoodRecord.getLunch();
                    List<Item> tempLunch = new ArrayList<>();

                    for (Item i : lunch){
                        if (!i.getFoodName().equals(item.getFoodName())){
                            tempLunch.add(i);
                        }
                    }
                    userFoodRecord.setLunch(tempLunch);
                    tempCalories -= Double.parseDouble(item.getNutrient().getCalories())*tempQuantity;
                    userFoodRecord.setFood(Double.valueOf(df.format(tempCalories)));

                }else if (listToUpdate.equals("dinner")){

                    List<Item> dinner = userFoodRecord.getDinner();
                    List<Item> tempDinner = new ArrayList<>();

                    for (Item i : dinner){
                        if (!i.getFoodName().equals(item.getFoodName())){
                            tempDinner.add(i);
                        }
                    }
                    userFoodRecord.setBreakfast(tempDinner);
                    tempCalories -= Double.parseDouble(item.getNutrient().getCalories())*tempQuantity;
                    userFoodRecord.setFood(Double.valueOf(df.format(tempCalories)));
                }else if (listToUpdate.equals("other")){

                    List<Item> other = userFoodRecord.getOther();
                    List<Item> tempOther = new ArrayList<>();

                    for (Item i : other){
                        if (!i.getFoodName().equals(item.getFoodName())){
                            tempOther.add(i);
                        }
                    }
                    userFoodRecord.setBreakfast(tempOther);
                    tempCalories -= Double.parseDouble(item.getNutrient().getCalories())*tempQuantity;
                    userFoodRecord.setFood(Double.valueOf(df.format(tempCalories)));
                }

                Double tempRemaining = userFoodRecord.goal - userFoodRecord.food + userFoodRecord.exerciseBurn;
                userFoodRecord.setRemaining(Double.valueOf(df.format(tempRemaining)));
                databaseTracks.setValue(userFoodRecord);
                Toast.makeText(ManageFoodList.this, "Remove Successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
