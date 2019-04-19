package com.example.cheejin.fyp.FoodInfo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cheejin.fyp.DisplayExercDetail;
import com.example.cheejin.fyp.DisplayExercList;
import com.example.cheejin.fyp.ExerciseListToAdd;
import com.example.cheejin.fyp.ManageExercList;
import com.example.cheejin.fyp.DisplayFoodDetail;
import com.example.cheejin.fyp.DisplayFoodInfo;
import com.example.cheejin.fyp.DisplayFoodList;
import com.example.cheejin.fyp.Exercise.Exercise;
import com.example.cheejin.fyp.ManageFoodList;
import com.example.cheejin.fyp.SearchFoodToAdd;
import com.example.cheejin.fyp.R;
import com.example.cheejin.fyp.SearchFragment;
import com.example.cheejin.fyp.User;
import com.example.cheejin.fyp.UserFoodRecord;
import com.squareup.picasso.Picasso;
import java.io.Serializable;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private SearchFragment searchFragment;
    private SearchFoodToAdd searchFoodToAdd;
    private DisplayFoodList displayFoodList;
    private UserFoodRecord userFoodRecord;
    private DisplayExercList displayExercList;
    private ExerciseListToAdd exerciseListToAdd;
    private List<Item> items;
    private List<Exercise> exercises;
    private String listToUpdate;
    private User user;

    public ItemAdapter(List<Item> items, SearchFragment searchFragment){
        this.items = items;
        this.searchFragment = searchFragment;
    }

    //To add food
    public ItemAdapter(List<Item> items, SearchFoodToAdd searchFoodToAdd, UserFoodRecord userFoodRecord, User user, String listToUpdate){
        this.items = items;
        this.searchFoodToAdd = searchFoodToAdd;
        this.userFoodRecord = userFoodRecord;
        this.user = user;
        this.listToUpdate = listToUpdate;
    }

    //To manipulate food
    public ItemAdapter(List<Item> items, DisplayFoodList displayFoodList, UserFoodRecord userFoodRecord, User user, String listToUpdate){
        this.items = items;
        this.displayFoodList = displayFoodList;
        this.userFoodRecord = userFoodRecord;
        this.user = user;
        this.listToUpdate = listToUpdate;
    }

    //To add exercise
    public ItemAdapter(List<Exercise> exercises, ExerciseListToAdd exerciseListToAdd, UserFoodRecord userFoodRecord, User user){
        this.exercises = exercises;
        this.exerciseListToAdd = exerciseListToAdd;
        this.userFoodRecord = userFoodRecord;
        this.user = user;
    }

    //To manipulate exercise
    public ItemAdapter(List<Exercise> exercises, DisplayExercList displayExercList, UserFoodRecord userFoodRecord, User user){
        this.exercises = exercises;
        this.displayExercList = displayExercList;
        this.userFoodRecord = userFoodRecord;
        this.user = user;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_food_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, final int position) {
        if (items != null){
            holder.foodname.setText(items.get(position).getFoodName());
            holder.foodbrand.setText(items.get(position).getFoodBrand());
            final Nutrient nutrient = items.get(position).getNutrient();
            if (nutrient.getCarbs().toString() == null ) {
                holder.fooddescrip.setText("Calories:" + nutrient.getCalories() + "  |  " +
                        "Fat:" +  nutrient.getFat() + "  |  " +
                        "Protein:" + nutrient.getProtein());
            }else if (nutrient.getCalories().toString()==null){
                holder.fooddescrip.setText("Carbs:" + (Double.valueOf(nutrient.getCarbs()) * items.get(position).getQuantity()) + "  |  " +
                        "Fat:" + nutrient.getFat() + "  |  " +
                        "Protein:" + nutrient.getProtein());
            }else if (nutrient.getProtein().toString()==null){
                holder.fooddescrip.setText("Carbs:" + (Double.valueOf(nutrient.getCarbs()) * items.get(position).getQuantity()) + "  |  " +
                        "Calories:" + nutrient.getCalories() + "  |  " +
                        "Fat:" + nutrient.getFat());
            }else if (nutrient.getFat().toString()==null){
                holder.fooddescrip.setText("Carbs:" + nutrient.getCarbs() + "  |  " +
                        "Calories:" + nutrient.getCalories() + "  |  " +
                        "Protein:" + nutrient.getProtein());
            }else{
                holder.fooddescrip.setText("Carbs:" + nutrient.getCarbs() + "  |  " +
                        "Calories:" + nutrient.getCalories() + "  |  " +
                        "Fat:" + nutrient.getFat() + "  |  " +
                        "Protein:" + nutrient.getProtein());
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (searchFoodToAdd != null) {
                        //Display food list with add button
                        Nutrient tempNutrient = new Nutrient(nutrient.getCalories(), nutrient.getFat(), nutrient.getProtein(), nutrient.getCarbs());
                        Item itemsPosition = items.get(position);
                        Item tempItem = new Item(itemsPosition.getFoodID(), itemsPosition.getFoodName(), itemsPosition.getFoodBrand(), tempNutrient, itemsPosition.getQuantity());

                        Intent i = new Intent(searchFoodToAdd, DisplayFoodDetail.class);
                        i.putExtra("userFoodRecord", (Serializable) userFoodRecord);
                        i.putExtra("listToUpdate", listToUpdate);
                        i.putExtra("foodDetails", (Serializable) tempItem);
                        i.putExtra("user",user);
                        searchFoodToAdd.startActivity(i);
                    }else if (displayFoodList != null){
                        //Display food list for meal
                        Nutrient tempNutrient = new Nutrient(nutrient.getCalories(), nutrient.getFat(), nutrient.getProtein(), nutrient.getCarbs());
                        Item itemsPosition = items.get(position);
                        Item tempItem = new Item(itemsPosition.getFoodID(), itemsPosition.getFoodName(), itemsPosition.getFoodBrand(), tempNutrient, itemsPosition.getQuantity());

                        //To manage food - remove from list
                        Intent i = new Intent(displayFoodList, ManageFoodList.class);
                        i.putExtra("userFoodRecord", (Serializable) userFoodRecord);
                        i.putExtra("listToUpdate", listToUpdate);
                        i.putExtra("foodDetails", (Serializable) tempItem);
                        i.putExtra("user",user);
                        displayFoodList.startActivity(i);
                    }else{
                        //For search fragment
                        Nutrient tempNutrient = new Nutrient(nutrient.getCalories(), nutrient.getFat(), nutrient.getProtein(), nutrient.getCarbs());
                        Item itemsPosition = items.get(position);
                        Item tempItem = new Item(itemsPosition.getFoodID(), itemsPosition.getFoodName(), itemsPosition.getFoodBrand(), tempNutrient, itemsPosition.getQuantity());

                        Intent i = new Intent(searchFragment.getContext(), DisplayFoodInfo.class);
                        i.putExtra("foodDetails", (Serializable) tempItem);
                        searchFragment.startActivity(i);
                    }
                }
            });
        }else if(exercises != null){
            if (displayExercList != null){
                holder.foodname.setText(exercises.get(position).getName());
                holder.foodbrand.setText(String.valueOf(exercises.get(position).getCalories()* exercises.get(position).getDuration()/30));
                holder.fooddescrip.setVisibility(View.GONE);

                holder.itemView.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        Exercise tempPosition = exercises.get(position);
                        Exercise tempExercise = new Exercise(tempPosition.getName(),tempPosition.getCalories(), tempPosition.getDuration());

                        Intent i = new Intent(displayExercList, ManageExercList.class);
                        i.putExtra("userFoodRecord", (Serializable) userFoodRecord);
                        i.putExtra("exercDetails", (Serializable) tempExercise);
                        i.putExtra("user",user);
                        displayExercList.startActivity(i);
                    }
                });
            }else{
                holder.foodname.setText(exercises.get(position).getName());
                holder.foodbrand.setVisibility(View.GONE);
                holder.fooddescrip.setVisibility(View.GONE);

                holder.itemView.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        Exercise tempPosition = exercises.get(position);
                        Exercise tempExercise = new Exercise(tempPosition.getName(),tempPosition.getCalories(), tempPosition.getDuration());

                        Intent i = new Intent(exerciseListToAdd, DisplayExercDetail.class);
                        i.putExtra("userFoodRecord", (Serializable) userFoodRecord);
                        i.putExtra("exercDetails", (Serializable) tempExercise);
                        i.putExtra("user",user);
                        exerciseListToAdd.startActivity(i);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }else{
            return exercises.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView foodname;
        protected TextView foodbrand;
        protected TextView fooddescrip;
        public ViewHolder(View itemView) {
            super(itemView);
            foodname = (TextView) itemView.findViewById(R.id.exerciseNameLabel);
            foodbrand = (TextView) itemView.findViewById(R.id.caloriesBurnLabel);
            fooddescrip = (TextView) itemView.findViewById(R.id.foodDescrip);
        }
    }
}
