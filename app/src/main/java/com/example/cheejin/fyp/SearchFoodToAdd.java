package com.example.cheejin.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cheejin.fyp.FoodInfo.Item;
import com.example.cheejin.fyp.FoodInfo.ItemAdapter;
import com.example.cheejin.fyp.FoodInfo.Nutrient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchFoodToAdd extends AppCompatActivity {

    ImageButton backBtn;
    EditText searchText;
    Button searchBtn;
    String foodToSearch = "";
    String urlNutritionixSearch = "https://api.nutritionix.com/v1_1/search/";
    String urlContainAppKeyNID = "?results=0%3A20&cal_min=0&cal_max=50000&fields=*&appId=c552faed&appKey=6ce4b01edd81a90a941f8a4b29f219d7";
    List<Item> foodList;
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    ItemAdapter ia;
    UserFoodRecord currentUserFoodRecord;
    User currentUser;
    String listToUpdate, dateSelected;
    DatabaseReference databaseTracks;
    List<UserFoodRecord> trackList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        //foodToSearch
        currentUserFoodRecord = (UserFoodRecord) getIntent().getSerializableExtra("userFoodRecord");
        currentUser = (User) getIntent().getSerializableExtra("user");
        listToUpdate = (String) getIntent().getSerializableExtra("listToUpdate");
        dateSelected = (String) getIntent().getSerializableExtra("dateSelected");
        databaseTracks = FirebaseDatabase.getInstance().getReference("tracks").child(currentUser.getUserId());

        searchText = (EditText) findViewById(R.id.searchText);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        backBtn = (ImageButton) findViewById(R.id.backBtn);
        recyclerView = (RecyclerView) findViewById(R.id.foodList);

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
                            currentUserFoodRecord = tempRecord;
                        }
                    }
                }

                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        backFunction();
                    }
                });

                searchBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchFoodParse();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void backFunction() {
        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void searchFoodParse() {
        foodList = new ArrayList<>();
        foodToSearch = searchText.getText().toString();
        if(foodToSearch.contains(" ")){
            foodToSearch = foodToSearch.replace(" ","%20");
        }

        String finalUrl = urlNutritionixSearch + foodToSearch + urlContainAppKeyNID;

        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, finalUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("hits");

                            for (int i =0; i<jsonArray .length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                JSONObject food = jsonObject.getJSONObject("fields");
                                String foodID = food.getString("item_id");
                                String foodName = food.getString("item_name");
                                String brandName = food.getString("brand_name");
                                String calories = food.getString("nf_calories");
                                String fat = food.getString("nf_total_fat");
                                String protein = food.getString("nf_protein");
                                String carbs = food.getString("nf_total_carbohydrate");
                                Nutrient nutrientTemp = new Nutrient(calories,fat,protein,carbs);
                                Item foodTemp = new Item(foodID,foodName,brandName,nutrientTemp,1);
                                foodList.add(foodTemp);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);

        LinearLayoutManager lim = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lim);

        ia = new ItemAdapter(foodList, this, currentUserFoodRecord, currentUser, listToUpdate);
        recyclerView.setAdapter(ia);
    }
}
