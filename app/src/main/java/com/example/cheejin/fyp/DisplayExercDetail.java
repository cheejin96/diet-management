package com.example.cheejin.fyp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cheejin.fyp.Exercise.Exercise;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DisplayExercDetail extends AppCompatActivity {

    DatabaseReference databaseTracks;
    User user;
    ImageButton backBtn;
    UserFoodRecord userFoodRecord;
    TextView name, caloriesBurn, duration;
    Spinner durationSpinner;
    Button addBtn;
    Double tempExerciseBurn = 0.00;
    Integer tempDuration = 0;
    List<Exercise> exercList;
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        userFoodRecord = (UserFoodRecord) getIntent().getSerializableExtra("userFoodRecord");
        final Exercise exercise = (Exercise) getIntent().getSerializableExtra("exercDetails");
        user = (User) getIntent().getSerializableExtra("user");

        databaseTracks = FirebaseDatabase.getInstance().getReference("tracks").child(user.getUserId()).child(userFoodRecord.date);

        name = findViewById(R.id.exercName);
        caloriesBurn = findViewById(R.id.caloriesBurn);
        durationSpinner = findViewById(R.id.durationSpinner);
        addBtn = findViewById(R.id.addBtn);
        backBtn = findViewById(R.id.backBtn);

        if (exercise != null){
            name.setText(exercise.getName());
            caloriesBurn.setText(exercise.getCalories().toString());
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get current calories burn
                tempExerciseBurn = userFoodRecord.getExerciseBurn();
                //Get duration
                tempDuration = Integer.parseInt(durationSpinner.getSelectedItem().toString());
                exercise.setDuration(tempDuration);

                //Check contain exercise list
                if (userFoodRecord.exercise == null){
                    //create a new list
                    exercList = new ArrayList<>();
                    exercList.add(exercise);
                    userFoodRecord.setExercise(exercList);
                    tempExerciseBurn += exercise.getCalories()*tempDuration/30;
                    userFoodRecord.setExerciseBurn(Double.valueOf(df.format(tempExerciseBurn)));
                }else{
                    List<Exercise> exercList = userFoodRecord.getExercise();
                    exercList.add(exercise);
                    tempExerciseBurn += exercise.getCalories()*tempDuration/30;
                    userFoodRecord.setExerciseBurn(Double.valueOf(df.format(tempExerciseBurn)));
                }

                Double tempRemaining = userFoodRecord.goal - userFoodRecord.food + userFoodRecord.exerciseBurn;
                userFoodRecord.setRemaining(Double.valueOf(df.format(tempRemaining)));
                databaseTracks.setValue(userFoodRecord);
                Toast.makeText(DisplayExercDetail.this, "Added Successful", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }
}
