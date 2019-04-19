package com.example.cheejin.fyp;

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

public class ManageExercList extends AppCompatActivity{

    DatabaseReference databaseTracks;
    User user;
    ImageButton backBtn;
    UserFoodRecord userFoodRecord;
    TextView name, caloriesBurn, duration;
    Button removeBtn;
    Double tempExerciseBurn;
    Integer tempDuration;
    DecimalFormat df = new DecimalFormat("#.##");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_exercise_list);

        final Exercise exercise = (Exercise) getIntent().getSerializableExtra("exercDetails");
        user = (User) getIntent().getSerializableExtra("user");
        userFoodRecord = (UserFoodRecord) getIntent().getSerializableExtra("userFoodRecord");
        databaseTracks = FirebaseDatabase.getInstance().getReference("tracks").child(user.getUserId()).child(userFoodRecord.date);

        name = findViewById(R.id.exercName);
        caloriesBurn = findViewById(R.id.caloriesBurn);
        duration = findViewById(R.id.duration);
        removeBtn = findViewById(R.id.removeBtn);
        backBtn = findViewById(R.id.backBtn);

        if (exercise != null){
            name.setText(exercise.getName());
            caloriesBurn.setText(exercise.getCalories().toString());
            duration.setText(exercise.getDuration().toString());
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
                tempExerciseBurn = userFoodRecord.getExerciseBurn();
                tempDuration = exercise.getDuration();

                List<Exercise> exercises = userFoodRecord.getExercise();
                List<Exercise> tempExercise = new ArrayList<>();

                for (Exercise i : exercises){
                    if (!i.getName().equals(exercise.getName())){
                        tempExercise.add(i);
                    }
                }
                userFoodRecord.setExercise(tempExercise);

                tempExerciseBurn -= exercise.getCalories()* exercise.getDuration()/30;
                userFoodRecord.setExerciseBurn(Double.valueOf(df.format(tempExerciseBurn)));

                Double tempRemaining = userFoodRecord.goal - userFoodRecord.food + userFoodRecord.exerciseBurn;
                userFoodRecord.setRemaining(Double.valueOf(df.format(tempRemaining)));
                databaseTracks.setValue(userFoodRecord);
                Toast.makeText(ManageExercList.this, "Remove Successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
