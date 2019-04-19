package com.example.cheejin.fyp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.example.cheejin.fyp.Exercise.Exercise;
import com.example.cheejin.fyp.FoodInfo.Item;
import com.example.cheejin.fyp.FoodInfo.ItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExerciseListToAdd extends AppCompatActivity {

    DatabaseReference databaseTracks, databaseExercises;
    RecyclerView recyclerView;
    List<Exercise> list = new ArrayList<>();
    User user;
    UserFoodRecord userFoodRecord;
    ItemAdapter ia;
    ImageButton backBtn;
    String dateSelected;
    List<UserFoodRecord> trackList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        dateSelected = (String) getIntent().getSerializableExtra("dateSelected");
        user = (User) getIntent().getSerializableExtra("user");
        userFoodRecord = (UserFoodRecord) getIntent().getSerializableExtra("userFoodRecord");

        recyclerView = findViewById(R.id.exerciseList);
        backBtn = findViewById(R.id.backBtn);

        final Context context = ExerciseListToAdd.this;

        databaseTracks = FirebaseDatabase.getInstance().getReference("tracks").child(user.getUserId());
        databaseExercises = FirebaseDatabase.getInstance().getReference("exercise");

        databaseTracks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                            userFoodRecord = tempRecord;
                        }
                    }
                }

                databaseExercises.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (list != null){
                            list.clear();
                        }

                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot c : children) {
                            list.add(c.getValue(Exercise.class));
                        }

                        if (list != null){
                            LinearLayoutManager lim = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager(lim);
                            ia = new ItemAdapter(list, (ExerciseListToAdd) context, userFoodRecord ,user);
                            recyclerView.setAdapter(ia);
                        }

                        backBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (getFragmentManager().getBackStackEntryCount() != 0){
                                    getFragmentManager().popBackStack();
                                }else{
                                    ExerciseListToAdd.super.onBackPressed();
                                }
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
