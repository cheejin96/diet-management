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
import android.widget.TextView;

import com.example.cheejin.fyp.FoodInfo.Item;
import com.example.cheejin.fyp.FoodInfo.ItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DisplayFoodList  extends AppCompatActivity {

    TextView foodLabel;
    Button addBtn;
    ImageButton backBtn;
    RecyclerView recyclerView;
    UserFoodRecord currentUserFoodRecord;
    List<Item> itemList;
    ItemAdapter ia;
    User currentUser;
    String dateSelected, breakfast, lunch, dinner, other, listToUpdate;
    DatabaseReference databaseTracks;
    List<UserFoodRecord> trackList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_record_list);

        dateSelected = (String) getIntent().getSerializableExtra("dateSelected");
        breakfast = (String) getIntent().getSerializableExtra("breakfast");
        lunch = (String) getIntent().getSerializableExtra("lunch");
        dinner = (String) getIntent().getSerializableExtra("dinner");
        other = (String) getIntent().getSerializableExtra("other");
        currentUser = (User) getIntent().getSerializableExtra("user");
        databaseTracks = FirebaseDatabase.getInstance().getReference("tracks").child(currentUser.getUserId());

        foodLabel = findViewById(R.id.foodLabel);
        addBtn = findViewById(R.id.addBtn);
        backBtn = (ImageButton) findViewById(R.id.backBtn);
        recyclerView = (RecyclerView) findViewById(R.id.foodList);

        final Context currentContext = DisplayFoodList.this;

        databaseTracks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (itemList != null){
                    itemList.clear();
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

                    if (breakfast!= null) {
                        foodLabel.setText("Breakfast");
                        itemList = currentUserFoodRecord.getBreakfast();
                        listToUpdate = "breakfast";
                    }else if (lunch != null){
                        foodLabel.setText("Lunch");
                        itemList = currentUserFoodRecord.getLunch();
                        listToUpdate = "lunch";
                    }else if (dinner != null){
                        foodLabel.setText("Dinner");
                        itemList = currentUserFoodRecord.getDinner();
                        listToUpdate = "dinner";
                    }else if (other != null){
                        foodLabel.setText("Other");
                        itemList = currentUserFoodRecord.getOther();
                        listToUpdate = "other";
                    }

                    if (itemList != null){
                        LinearLayoutManager lim = new LinearLayoutManager(currentContext);
                        recyclerView.setLayoutManager(lim);
                        ia = new ItemAdapter(itemList, (DisplayFoodList) currentContext, currentUserFoodRecord, currentUser, listToUpdate);
                        recyclerView.setAdapter(ia);
                    }
                }

                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getFragmentManager().getBackStackEntryCount() != 0){
                            getFragmentManager().popBackStack();
                        }else{
                            DisplayFoodList.super.onBackPressed();
                        }
                    }
                });

                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(DisplayFoodList.this, SearchFoodToAdd.class);
                        i.putExtra("userFoodRecord", (Serializable) currentUserFoodRecord);
                        i.putExtra("dateSelected", dateSelected);
                        i.putExtra("listToUpdate", listToUpdate);
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
