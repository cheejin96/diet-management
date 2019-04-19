package com.example.cheejin.fyp;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cheejin.fyp.Exercise.Exercise;
import com.example.cheejin.fyp.FoodInfo.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {

    DatabaseReference databaseTracks;
    Button breakfastBtn, lunchBtn, dinnerBtn, otherBtn, exerciseBtn, tipsBtn;
    TextView caloriesGoal, caloriesFood, caloriesExerc, caloriesRemain;
    Spinner dateSpinner;
    User currentUser, latestUserInfo;
    UserFoodRecord currentUserFoodRecord;
    List<User> latestUserList = new ArrayList<>();
    List<UserFoodRecord> trackList = new ArrayList<>();
    List<String> dateList = new ArrayList<>();
    List<String> dateListShowLatest = new ArrayList<>();
    String todayDate, tempDate, btnType;
    Boolean previous = false;
    Double tempCalories;
    Double goal = 0.00;
    Double food = 0.00;
    Double exerciseBurn = 0.00;
    DecimalFormat df = new DecimalFormat("#.##");
    DatabaseReference databaseUsers;

    boolean checkTodayRecord = false;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        currentUser = (User) getArguments().getSerializable("currentUserBundle");
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseTracks = FirebaseDatabase.getInstance().getReference("tracks").child(currentUser.getUserId());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
        Date date = new Date();
        todayDate = simpleDateFormat.format(date);

        final View v = inflater.inflate(R.layout.fragment_add, container, false);

        //Button
        breakfastBtn = v.findViewById(R.id.breakfastBtn);
        lunchBtn = v.findViewById(R.id.lunchBtn);
        dinnerBtn = v.findViewById(R.id.dinnerBtn);
        otherBtn = v.findViewById(R.id.otherBtn);
        exerciseBtn = v.findViewById(R.id.exerciseBtn);
        tipsBtn = v.findViewById(R.id.tipsBtn);

        //TextView
        caloriesGoal = v.findViewById(R.id.caloriesGoal);
        caloriesFood = v.findViewById(R.id.caloriesFood);
        caloriesExerc = v.findViewById(R.id.caloriesExerc);
        caloriesRemain = v.findViewById(R.id.caloriesRemain);

        dateSpinner = v.findViewById(R.id.dateSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, dateListShowLatest);
        dateSpinner.setAdapter(adapter);

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (latestUserInfo != null || latestUserList != null){
                    latestUserInfo = new User();
                    latestUserList.clear();
                }

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot c : children) {
                    latestUserList.add(c.getValue(User.class));
                }

               if (latestUserList != null){
                    for (User u: latestUserList){
                        if (u.getUserId().equals(currentUser.getUserId())){
                            latestUserInfo = u;
                        }
                    }
                }

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

                            if (dateList!= null || dateListShowLatest!= null){
                                dateList.clear();
                                dateListShowLatest.clear();
                            }

                            for (UserFoodRecord u : trackList) {
                                dateList.add(u.getDate());
                                if (u.date.equals(todayDate)) {
                                    //Check contain today record
                                    //Make it true - contain today record
                                    checkTodayRecord = true;

                                    //Check previous view
                                    //If dont have then show today record
                                    if (!previous) {
                                        UserFoodRecord tempRecord = new UserFoodRecord(u.getDate(), u.getBreakfast(), u.getLunch(), u.getDinner(), u.getOther()
                                                , u.getExercise(), u.getGoal(), u.getFood(), u.getExerciseBurn());
                                        currentUserFoodRecord = tempRecord;
                                        caloriesGoal.setText(df.format(currentUserFoodRecord.getGoal()));
                                        caloriesFood.setText(df.format(currentUserFoodRecord.getFood()));
                                        caloriesExerc.setText(df.format(currentUserFoodRecord.getExerciseBurn()));
                                        if (currentUserFoodRecord.getRemaining() < 0.00){
                                            caloriesRemain.setTextColor(Color.RED);
                                        }else if (currentUserFoodRecord.getRemaining() <= 500.00){
                                            caloriesRemain.setTextColor(getResources().getColor(R.color.colorOrange));
                                        }else{
                                            caloriesRemain.setTextColor(Color.BLACK);
                                        }
                                        caloriesRemain.setText(df.format(currentUserFoodRecord.getRemaining()));
                                    }
                                }

                                if (u.date.equals(tempDate)){
                                    UserFoodRecord tempRecord = new UserFoodRecord(u.getDate(), u.getBreakfast(), u.getLunch(), u.getDinner(), u.getOther()
                                            , u.getExercise(), u.getGoal(), u.getFood(), u.getExerciseBurn());
                                    currentUserFoodRecord = tempRecord;
                                    caloriesGoal.setText(df.format(currentUserFoodRecord.getGoal()));
                                    caloriesFood.setText(df.format(currentUserFoodRecord.getFood()));
                                    caloriesExerc.setText(df.format(currentUserFoodRecord.getExerciseBurn()));
                                    if (currentUserFoodRecord.getRemaining() < 0.00){
                                        caloriesRemain.setTextColor(Color.RED);
                                    }else if (currentUserFoodRecord.getRemaining() <= 500.00){
                                        caloriesRemain.setTextColor(getResources().getColor(R.color.colorOrange));
                                    }else{
                                        caloriesRemain.setTextColor(Color.BLACK);
                                    }
                                    caloriesRemain.setText(df.format(currentUserFoodRecord.getRemaining()));
                                }

                                //Make sure record get latest user info
                                //Check goal is same with latest
                                if (latestUserInfo.gender.equals("Male")) {
                                    tempCalories = (10 * latestUserInfo.weight) + (6.25 * latestUserInfo.height) - (5 * latestUserInfo.age) + 5;

                                    if (latestUserInfo.goal.equals("Gain weight")) {
                                        goal = tempCalories + 500;
                                    } else if (latestUserInfo.goal.equals("Maintain weight")) {
                                        goal = tempCalories;
                                    } else if (latestUserInfo.goal.equals("Loss weight")) {
                                        goal = tempCalories - 500;
                                    }
                                } else {
                                    tempCalories = (10 * latestUserInfo.weight) + (6.25 * latestUserInfo.height) - (5 * latestUserInfo.age) - 161;

                                    if (latestUserInfo.goal.equals("Gain weight")) {
                                        goal = tempCalories + 500;
                                    } else if (latestUserInfo.goal.equals("Maintain weight")) {
                                        goal = tempCalories;
                                    } else if (latestUserInfo.goal.equals("Loss weight")) {
                                        goal = tempCalories - 500;
                                    }
                                }

                                //If different then set goal and remaining to latest
                                if (u.getGoal() != goal) {
                                    u.setGoal(goal);
                                    Double tempRemaining = goal - u.getFood() + u.getExerciseBurn();
                                    u.setRemaining(tempRemaining);
                                    databaseTracks.child(u.date).setValue(u);
                                }
                            }

                            //To display from latest date in spinner
                            for(int i = dateList.size() -1; i >=0 ;i--){
                                dateListShowLatest.add(dateList.get(i));
                            }


                            //Create new record for today
                            if (checkTodayRecord == false) {
                                dateList.add(todayDate);
                                if (latestUserInfo.gender.equals("Male")){
                                    tempCalories = (10*latestUserInfo.weight) + (6.25*latestUserInfo.height) - (5*latestUserInfo.age) +5;

                                    if (latestUserInfo.goal.equals("Gain weight")){
                                        goal = tempCalories + 500;
                                    }else if (latestUserInfo.goal.equals("Maintain weight")){
                                        goal = tempCalories;
                                    }else if (latestUserInfo.goal.equals("Loss weight")){
                                        goal = tempCalories - 500;
                                    }
                                }else{
                                    tempCalories = (10*latestUserInfo.weight) + (6.25*latestUserInfo.height) - (5*latestUserInfo.age) - 161;

                                    if (latestUserInfo.goal.equals("Gain weight")){
                                        goal = tempCalories + 500;
                                    }else if (latestUserInfo.goal.equals("Maintain weight")){
                                        goal = tempCalories;
                                    }else if (latestUserInfo.goal.equals("Loss weight")){
                                        goal = tempCalories - 500;
                                    }
                                }

                                //Create new record for today
                                List<Item> breakfast = new ArrayList<>();
                                List<Item> lunch = new ArrayList<>();
                                List<Item> dinner = new ArrayList<>();
                                List<Item> other = new ArrayList<>();
                                List<Exercise> exercise = new ArrayList<>();
                                UserFoodRecord u = new UserFoodRecord(todayDate, breakfast, lunch, dinner, other, exercise, goal, food, exerciseBurn);
                                databaseTracks.child(todayDate).setValue(u);
                                caloriesGoal.setText(df.format(u.goal));
                                caloriesFood.setText(df.format(u.food));
                                caloriesExerc.setText(df.format(u.exerciseBurn));
                                if (u.remaining < 0.00){
                                    caloriesRemain.setTextColor(Color.RED);
                                }else if (u.remaining <= 500.00){
                                    caloriesRemain.setTextColor(getResources().getColor(R.color.colorOrange));
                                }else{
                                    caloriesRemain.setTextColor(Color.BLACK);
                                }
                                caloriesRemain.setText(df.format(u.remaining));
                                currentUserFoodRecord = u;
                            }
                        }

                        if (!previous) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, dateListShowLatest);
                            dateSpinner.setAdapter(adapter);
                        }

                        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                tempDate = dateSpinner.getSelectedItem().toString();
                                for (UserFoodRecord u: trackList){
                                    if (u.date.equals(tempDate)){
                                        caloriesGoal.setText(df.format(u.goal));
                                        caloriesFood.setText(df.format(u.food));
                                        caloriesExerc.setText(df.format(u.exerciseBurn));
                                        if (u.remaining < 0.00){
                                            caloriesRemain.setTextColor(Color.RED);
                                        }else if (u.remaining <= 500.00){
                                            caloriesRemain.setTextColor(getResources().getColor(R.color.colorOrange));
                                        }else{
                                            caloriesRemain.setTextColor(Color.BLACK);
                                        }
                                        caloriesRemain.setText(df.format(u.remaining));
                                        currentUserFoodRecord = u;
                                    }
                                }
                                previous = true;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                previous = false;
                            }
                        });

                        breakfastBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btnType = "breakfast";
                                Intent i = new Intent(getContext(), DisplayFoodList.class);
                                i.putExtra("dateSelected", tempDate);
                                i.putExtra("user",latestUserInfo);
                                i.putExtra("breakfast",btnType);
                                startActivity(i);
                            }
                        });

                        lunchBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btnType = "lunch";
                                Intent i = new Intent(getContext(), DisplayFoodList.class);
                                i.putExtra("dateSelected", tempDate);
                                i.putExtra("user",latestUserInfo);
                                i.putExtra("lunch",btnType);
                                startActivity(i);
                            }
                        });

                        dinnerBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btnType = "dinner";
                                Intent i = new Intent(getContext(), DisplayFoodList.class);
                                i.putExtra("dateSelected", tempDate);
                                i.putExtra("user",latestUserInfo);
                                i.putExtra("dinner",btnType);
                                startActivity(i);
                            }
                        });

                        otherBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btnType = "other";
                                Intent i = new Intent(getContext(), DisplayFoodList.class);
                                i.putExtra("dateSelected", tempDate);
                                i.putExtra("user",latestUserInfo);
                                i.putExtra("other",btnType);
                                startActivity(i);
                            }
                        });

                        exerciseBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getContext(), DisplayExercList.class);
                                i.putExtra("dateSelected", tempDate);
                                i.putExtra("user",latestUserInfo);
                                startActivity(i);
                            }
                        });

                        tipsBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getContext(), TipsForHelp.class);
                                startActivity(i);
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

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
