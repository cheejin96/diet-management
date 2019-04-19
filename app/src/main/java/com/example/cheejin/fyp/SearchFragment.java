package com.example.cheejin.fyp;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cheejin.fyp.FoodInfo.Item;
import com.example.cheejin.fyp.FoodInfo.ItemAdapter;
import com.example.cheejin.fyp.FoodInfo.Nutrient;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

//import com.fasterxml.jackson.databind.JsonNode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    String foodToSearch = "";
    String urlNutritionixSearch = "https://api.nutritionix.com/v1_1/search/";
    String urlContainAppKeyNID = "?results=0%3A20&cal_min=0&cal_max=50000&fields=*&appId=c552faed&appKey=6ce4b01edd81a90a941f8a4b29f219d7";
    EditText searchText;
    Button searchBtn;
    RecyclerView recyclerView;
    private View view;
    String brand;
    ItemAdapter ia;
    List<Item> foodList;


    private RequestQueue requestQueue;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search, container, false);
        searchText = (EditText) view.findViewById(R.id.searchText);
        searchBtn = (Button) view.findViewById(R.id.searchBtn);
        recyclerView = (RecyclerView) view.findViewById(R.id.foodListView);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFoodParse();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void searchFoodParse(){

        foodList = new ArrayList<>();
        foodToSearch = searchText.getText().toString();
        if(foodToSearch.contains(" ")){
            foodToSearch = foodToSearch.replace(" ","%20");
        }

        String finalUrl = urlNutritionixSearch + foodToSearch + urlContainAppKeyNID;

        requestQueue = Volley.newRequestQueue(getContext());
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
                                Item foodTemp = new Item(foodID,foodName,brandName,nutrientTemp, 1);
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

        LinearLayoutManager lim = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lim);

        ia = new ItemAdapter(foodList, this);
        recyclerView.setAdapter(ia);
    }

}
