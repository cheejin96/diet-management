package com.example.cheejin.fyp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.cheejin.fyp.Exercise.Exercise;
import com.example.cheejin.fyp.FoodInfo.ItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DisplayExercList extends AppCompatActivity {

    Button addBtn;
    ImageButton backBtn;
    RecyclerView recyclerView;
    UserFoodRecord currentUserFoodRecord;
    List<Exercise> exerciseList;
    ItemAdapter ia;
    User currentUser;
    String dateSelected;
    DatabaseReference databaseTracks;
    List<UserFoodRecord> trackList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_record_list);

        dateSelected = (String) getIntent().getSerializableExtra("dateSelected");
        currentUser = (User) getIntent().getSerializableExtra("user");

        databaseTracks = FirebaseDatabase.getInstance().getReference("tracks").child(currentUser.getUserId());

        addBtn = findViewById(R.id.addBtn);
        backBtn = (ImageButton) findViewById(R.id.backBtn);
        recyclerView = (RecyclerView) findViewById(R.id.exercList);

        final Context currentContext = DisplayExercList.this;

        databaseTracks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (exerciseList != null){
                    exerciseList.clear();
                }

                if (trackList != null) {
                    trackList.clear();
                }

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot c : children) {
                    trackList.add(c.getValue(UserFoodRecord.class));
                }

                if (trackList != null ) {
                    for (UserFoodRecord u : trackList) {
                        if (u.date.equals(dateSelected)){
                            UserFoodRecord tempRecord = new UserFoodRecord(u.getDate(), u.getBreakfast(), u.getLunch(), u.getDinner(), u.getOther()
                                    , u.getExercise(), u.getGoal(), u.getFood(), u.getExerciseBurn());
                            currentUserFoodRecord = tempRecord;
                        }
                    }
                }

                if (currentUserFoodRecord!=null){


                    if (currentUserFoodRecord.getExercise() != null){
                        exerciseList = currentUserFoodRecord.getExercise();
                    }else{
                        //To update list when no item inside if not last time removed will be show in the list
                        exerciseList = new ArrayList<>();
                    }

                    LinearLayoutManager lim = new LinearLayoutManager(currentContext);
                    recyclerView.setLayoutManager(lim);
                    ia = new ItemAdapter(exerciseList, (DisplayExercList) currentContext, currentUserFoodRecord, currentUser);
                    recyclerView.setAdapter(ia);
                }

                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getFragmentManager().getBackStackEntryCount() != 0){
                            getFragmentManager().popBackStack();
                        }else{
                            DisplayExercList.super.onBackPressed();
                        }
                    }
                });

                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(DisplayExercList.this, ExerciseListToAdd.class);
                        i.putExtra("userFoodRecord", (Serializable) currentUserFoodRecord);
                        i.putExtra("dateSelected", dateSelected);
                        i.putExtra("user", currentUser);
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
