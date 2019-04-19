package com.example.cheejin.fyp;


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
import android.widget.Spinner;

/*import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.Pie;
import com.anychart.anychart.ValueDataEntry;*/
import com.example.cheejin.fyp.FoodInfo.Item;
/*import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;*/
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//Sharky
/*import com.intrusoft.scatter.ChartData;
import com.intrusoft.scatter.PieChart;
import com.intrusoft.scatter.SimpleChart;*/

/*import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;*/

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    User currentUser;
    DatabaseReference databaseTracks;
    Spinner dateSpinner;
    List<String> dateList = new ArrayList<>();
    List<String> dateListShowLatest = new ArrayList<>();
    List<UserFoodRecord> trackList = new ArrayList<>();
    List<UserFoodRecord> descendingTrackList = new ArrayList<>();
    List<UserFoodRecord> weekList = new ArrayList<>();
    List<Item> breakfast = new ArrayList<>();
    List<Item> lunch = new ArrayList<>();
    List<Item> dinner = new ArrayList<>();
    List<Item> other = new ArrayList<>();
    String todayDate;
    Double calories, carbs, protein, fats;
    Integer quantity;
    Boolean previous = false;
    String tempDate;
    PieChartView pieChart;
    PieChartData pieChartData;

    public ChartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chart, container, false);

        currentUser = (User) getArguments().getSerializable("currentUserBundle");
        databaseTracks = FirebaseDatabase.getInstance().getReference("tracks").child(currentUser.getUserId());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
        Date date = new Date();
        todayDate = simpleDateFormat.format(date);

        dateSpinner = v.findViewById(R.id.dateSpinner);
        pieChart = v.findViewById(R.id.pieChart);

        final DecimalFormat df = new DecimalFormat("#.##");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, dateListShowLatest);
        dateSpinner.setAdapter(adapter);

        databaseTracks.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dateList != null || dateListShowLatest != null || trackList!= null || weekList!= null || descendingTrackList!= null){
                    dateList.clear();
                    dateListShowLatest.clear();
                    trackList.clear();
                    weekList.clear();
                    descendingTrackList.clear();
                }

                //Collect all record
                 Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                 for (DataSnapshot c : children) {
                     trackList.add(c.getValue(UserFoodRecord.class));
                 }

                 //To collect latest 7 days record for weekly chart view
                 Collections.reverse(trackList);
                 int dayCount =0;
                 for (UserFoodRecord u: trackList){
                     if (dayCount<7){
                         weekList.add(u);
                         dayCount++;
                     }
                 }
                 // Change back to ascending order
                 Collections.reverse(trackList);

                 if (trackList != null){
                     //int dayCount =0;
                     for (UserFoodRecord u: trackList){
                         dateList.add(u.date);

                         if (u.date.equals(todayDate)) {
                             //set first view to today record
                            if(!previous){
                                calories = 0.00;
                                carbs = 0.00;
                                protein = 0.00;
                                fats = 0.00;
                                breakfast = u.getBreakfast();
                                lunch = u.getLunch();
                                dinner = u.getDinner();
                                other = u.getOther();

                                if (breakfast != null){
                                    for (Item item : breakfast){
                                        quantity = item.getQuantity();
                                        calories += Double.parseDouble(item.getNutrient().getCalories())*quantity;
                                        carbs += Double.parseDouble(item.getNutrient().getCarbs())*quantity;
                                        protein += Double.parseDouble(item.getNutrient().getProtein())*quantity;
                                        fats += Double.parseDouble(item.getNutrient().getFat())*quantity;
                                    }
                                }

                                if (lunch != null){
                                    for (Item item : lunch){
                                        quantity = item.getQuantity();
                                        calories += Double.parseDouble(item.getNutrient().getCalories())*quantity;
                                        carbs += Double.parseDouble(item.getNutrient().getCarbs())*quantity;
                                        protein += Double.parseDouble(item.getNutrient().getProtein())*quantity;
                                        fats += Double.parseDouble(item.getNutrient().getFat())*quantity;
                                    }
                                }

                                if (dinner != null){
                                    for (Item item : dinner){
                                        quantity = item.getQuantity();
                                        calories += Double.parseDouble(item.getNutrient().getCalories())*quantity;
                                        carbs += Double.parseDouble(item.getNutrient().getCarbs())*quantity;
                                        protein += Double.parseDouble(item.getNutrient().getProtein())*quantity;
                                        fats += Double.parseDouble(item.getNutrient().getFat())*quantity;
                                    }
                                }

                                if (other != null){
                                    for (Item item : other){
                                        quantity = item.getQuantity();
                                        calories += Double.parseDouble(item.getNutrient().getCalories())*quantity;
                                        carbs += Double.parseDouble(item.getNutrient().getCarbs())*quantity;
                                        protein += Double.parseDouble(item.getNutrient().getProtein())*quantity;
                                        fats += Double.parseDouble(item.getNutrient().getFat())*quantity;
                                    }
                                }

                                List<SliceValue> values = new ArrayList<>();
                                if (calories != 0.00 || carbs != 0.00 || protein != 0.00 || fats != 0.00 ) {
                                    values.add(new SliceValue(Float.valueOf(String.valueOf(calories)),R.color.colorOrange /*getResources().getColor(R.color.colorOrange)*/).setLabel("Calories:" + df.format(calories)));
                                    values.add(new SliceValue(Float.valueOf(String.valueOf(carbs)), Color.BLUE).setLabel("Carbs:" + df.format(carbs)));
                                    values.add(new SliceValue(Float.valueOf(String.valueOf(protein)), Color.GREEN).setLabel("Proteins:" + df.format(protein)));
                                    values.add(new SliceValue(Float.valueOf(String.valueOf(fats)), Color.RED).setLabel("Fats:" + df.format(fats)));
                                }else{
                                    values.add(new SliceValue(1, Color.GRAY).setLabel("Empty"));
                                }
                                pieChartData = new PieChartData(values);
                                pieChartData.setHasLabels(true).setValueLabelTextSize(20);
                                pieChart.setPieChartData(pieChartData);
                                pieChart.invalidate();
                            }
                         }

                         if (tempDate != "Weekly") {
                             if (u.date.equals(tempDate)) {
                                 calories = 0.00;
                                 carbs = 0.00;
                                 protein = 0.00;
                                 fats = 0.00;
                                 breakfast = u.getBreakfast();
                                 lunch = u.getLunch();
                                 dinner = u.getDinner();
                                 other = u.getOther();

                                 if (breakfast != null) {
                                     for (Item item : breakfast) {
                                         quantity = item.getQuantity();
                                         calories += Double.parseDouble(item.getNutrient().getCalories()) * quantity;
                                         carbs += Double.parseDouble(item.getNutrient().getCarbs()) * quantity;
                                         protein += Double.parseDouble(item.getNutrient().getProtein()) * quantity;
                                         fats += Double.parseDouble(item.getNutrient().getFat()) * quantity;
                                     }
                                 }

                                 if (lunch != null) {
                                     for (Item item : lunch) {
                                         quantity = item.getQuantity();
                                         calories += Double.parseDouble(item.getNutrient().getCalories()) * quantity;
                                         carbs += Double.parseDouble(item.getNutrient().getCarbs()) * quantity;
                                         protein += Double.parseDouble(item.getNutrient().getProtein()) * quantity;
                                         fats += Double.parseDouble(item.getNutrient().getFat()) * quantity;
                                     }
                                 }

                                 if (dinner != null) {
                                     for (Item item : dinner) {
                                         quantity = item.getQuantity();
                                         calories += Double.parseDouble(item.getNutrient().getCalories()) * quantity;
                                         carbs += Double.parseDouble(item.getNutrient().getCarbs()) * quantity;
                                         protein += Double.parseDouble(item.getNutrient().getProtein()) * quantity;
                                         fats += Double.parseDouble(item.getNutrient().getFat()) * quantity;
                                     }
                                 }

                                 if (other != null) {
                                     for (Item item : other) {
                                         quantity = item.getQuantity();
                                         calories += Double.parseDouble(item.getNutrient().getCalories()) * quantity;
                                         carbs += Double.parseDouble(item.getNutrient().getCarbs()) * quantity;
                                         protein += Double.parseDouble(item.getNutrient().getProtein()) * quantity;
                                         fats += Double.parseDouble(item.getNutrient().getFat()) * quantity;
                                     }
                                 }

                                 List<SliceValue> values = new ArrayList<>();
                                 if (calories != 0.00 || carbs != 0.00 || protein != 0.00 || fats != 0.00 ) {/*getResources().getColor(R.color.colorOrange))*/
                                     values.add(new SliceValue(Float.valueOf(String.valueOf(calories)), R.color.colorOrange).setLabel("Calories:" + df.format(calories)));
                                     values.add(new SliceValue(Float.valueOf(String.valueOf(carbs)), Color.BLUE).setLabel("Carbs:" + df.format(carbs)));
                                     values.add(new SliceValue(Float.valueOf(String.valueOf(protein)), Color.GREEN).setLabel("Proteins:" + df.format(protein)));
                                     values.add(new SliceValue(Float.valueOf(String.valueOf(fats)), Color.RED).setLabel("Fats:" + df.format(fats)));
                                 }else{
                                     values.add(new SliceValue(1, Color.GRAY).setLabel("Empty"));
                                 }
                                 pieChartData = new PieChartData(values);
                                 pieChartData.setHasLabels(true).setValueLabelTextSize(20);
                                 pieChart.setPieChartData(pieChartData);
                                 pieChart.invalidate();
                             }
                         }else{
                             calories = 0.00;
                             carbs = 0.00;
                             protein = 0.00;
                             fats = 0.00;
                             for (UserFoodRecord userFoodRecord: weekList){
                                 breakfast = userFoodRecord.getBreakfast();
                                 lunch = userFoodRecord.getLunch();
                                 dinner = userFoodRecord.getDinner();
                                 other = userFoodRecord.getOther();

                                 if (breakfast != null) {
                                     for (Item item : breakfast) {
                                         quantity = item.getQuantity();
                                         calories += Double.parseDouble(item.getNutrient().getCalories()) * quantity;
                                         carbs += Double.parseDouble(item.getNutrient().getCarbs()) * quantity;
                                         protein += Double.parseDouble(item.getNutrient().getProtein()) * quantity;
                                         fats += Double.parseDouble(item.getNutrient().getFat()) * quantity;
                                     }
                                 }

                                 if (lunch != null) {
                                     for (Item item : lunch) {
                                         quantity = item.getQuantity();
                                         calories += Double.parseDouble(item.getNutrient().getCalories()) * quantity;
                                         carbs += Double.parseDouble(item.getNutrient().getCarbs()) * quantity;
                                         protein += Double.parseDouble(item.getNutrient().getProtein()) * quantity;
                                         fats += Double.parseDouble(item.getNutrient().getFat()) * quantity;
                                     }
                                 }

                                 if (dinner != null) {
                                     for (Item item : dinner) {
                                         quantity = item.getQuantity();
                                         calories += Double.parseDouble(item.getNutrient().getCalories()) * quantity;
                                         carbs += Double.parseDouble(item.getNutrient().getCarbs()) * quantity;
                                         protein += Double.parseDouble(item.getNutrient().getProtein()) * quantity;
                                         fats += Double.parseDouble(item.getNutrient().getFat()) * quantity;
                                     }
                                 }

                                 if (other != null) {
                                     for (Item item : other) {
                                         quantity = item.getQuantity();
                                         calories += Double.parseDouble(item.getNutrient().getCalories()) * quantity;
                                         carbs += Double.parseDouble(item.getNutrient().getCarbs()) * quantity;
                                         protein += Double.parseDouble(item.getNutrient().getProtein()) * quantity;
                                         fats += Double.parseDouble(item.getNutrient().getFat()) * quantity;
                                     }
                                 }
                             }

                             List<SliceValue> values = new ArrayList<>();
                             if (calories != 0.00 || carbs != 0.00 || protein != 0.00 || fats != 0.00 ) {
                                 values.add(new SliceValue(Float.valueOf(String.valueOf(calories)), R.color.colorOrange/*getResources().getColor(R.color.colorOrange)*/).setLabel("Calories:" + df.format(calories)));
                                 values.add(new SliceValue(Float.valueOf(String.valueOf(carbs)), Color.BLUE).setLabel("Carbs:" + df.format(carbs)));
                                 values.add(new SliceValue(Float.valueOf(String.valueOf(protein)), Color.GREEN).setLabel("Proteins:" + df.format(protein)));
                                 values.add(new SliceValue(Float.valueOf(String.valueOf(fats)), Color.RED).setLabel("Fats:" + df.format(fats)));
                             }else{
                                 values.add(new SliceValue(1, Color.GRAY).setLabel("Empty"));
                             }
                             pieChartData = new PieChartData(values);
                             pieChartData.setHasLabels(true).setValueLabelTextSize(20);
                             pieChart.setPieChartData(pieChartData);
                             pieChart.invalidate();
                             calories = 0.00;
                             carbs = 0.00;
                             protein = 0.00;
                             fats = 0.00;
                         }
                     }

                     //Set date display from latest
                     for (int i=dateList.size()-1; i>=0;i--){
                         dateListShowLatest.add(dateList.get(i));
                     }
                     dateListShowLatest.add("Weekly");

                 }

                 if (!previous) {
                     ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, dateListShowLatest);
                     dateSpinner.setAdapter(adapter);
                 }

                 dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                     @Override
                     public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                         calories = 0.00;
                         carbs = 0.00;
                         protein = 0.00;
                         fats = 0.00;
                         tempDate = dateSpinner.getSelectedItem().toString();
                         UserFoodRecord tempRecord = null;

                         if (trackList!= null) {

                             if (weekList!=null){
                                 weekList.clear();
                             }

                             //To collect latest 7 days record for weekly chart view
                             Collections.reverse(trackList);
                             int dayCount =0;
                             for (UserFoodRecord u: trackList){
                                 if (dayCount<7){
                                     weekList.add(u);
                                     dayCount++;
                                 }
                             }
                             // Change back to ascending order
                             Collections.reverse(trackList);

                             if(tempDate == "Weekly"){

                                 for (UserFoodRecord userFoodRecord : weekList) {
                                     breakfast = userFoodRecord.getBreakfast();
                                     lunch = userFoodRecord.getLunch();
                                     dinner = userFoodRecord.getDinner();
                                     other = userFoodRecord.getOther();

                                     if (breakfast != null) {
                                         for (Item item : breakfast) {
                                             quantity = item.getQuantity();

                                             calories += Double.parseDouble(item.getNutrient().getCalories()) * quantity;
                                             carbs += Double.parseDouble(item.getNutrient().getCarbs()) * quantity;
                                             protein += Double.parseDouble(item.getNutrient().getProtein()) * quantity;
                                             fats += Double.parseDouble(item.getNutrient().getFat()) * quantity;
                                         }
                                     }

                                     if (lunch != null) {
                                         for (Item item : lunch) {
                                             quantity = item.getQuantity();
                                             calories += Double.parseDouble(item.getNutrient().getCalories()) * quantity;
                                             carbs += Double.parseDouble(item.getNutrient().getCarbs()) * quantity;
                                             protein += Double.parseDouble(item.getNutrient().getProtein()) * quantity;
                                             fats += Double.parseDouble(item.getNutrient().getFat()) * quantity;
                                         }
                                     }

                                     if (dinner != null) {
                                         for (Item item : dinner) {
                                             quantity = item.getQuantity();
                                             calories += Double.parseDouble(item.getNutrient().getCalories()) * quantity;
                                             carbs += Double.parseDouble(item.getNutrient().getCarbs()) * quantity;
                                             protein += Double.parseDouble(item.getNutrient().getProtein()) * quantity;
                                             fats += Double.parseDouble(item.getNutrient().getFat()) * quantity;
                                         }
                                     }

                                     if (other != null) {
                                         for (Item item : other) {
                                             quantity = item.getQuantity();
                                             calories += Double.parseDouble(item.getNutrient().getCalories()) * quantity;
                                             carbs += Double.parseDouble(item.getNutrient().getCarbs()) * quantity;
                                             protein += Double.parseDouble(item.getNutrient().getProtein()) * quantity;
                                             fats += Double.parseDouble(item.getNutrient().getFat()) * quantity;
                                         }
                                     }
                                 }

                                 List<SliceValue> values = new ArrayList<>();
                                 if (calories != 0.00 || carbs != 0.00 || protein != 0.00 || fats != 0.00 ) {
                                     values.add(new SliceValue(Float.valueOf(String.valueOf(calories)),R.color.colorOrange/*getResources().getColor(R.color.colorOrange)*/).setLabel("Calories:" + df.format(calories)));
                                     values.add(new SliceValue(Float.valueOf(String.valueOf(carbs)), Color.BLUE).setLabel("Carbs:" + df.format(carbs)));
                                     values.add(new SliceValue(Float.valueOf(String.valueOf(protein)), Color.GREEN).setLabel("Proteins:" + df.format(protein)));
                                     values.add(new SliceValue(Float.valueOf(String.valueOf(fats)), Color.RED).setLabel("Fats:" + df.format(fats)));
                                 }else{
                                     values.add(new SliceValue(1, Color.GRAY).setLabel("Empty"));
                                 }
                                 pieChartData = new PieChartData(values);
                                 pieChartData.setHasLabels(true).setValueLabelTextSize(20);
                                 pieChart.setPieChartData(pieChartData);
                                 pieChart.invalidate();
                             }else {
                                 calories = 0.00;
                                 carbs = 0.00;
                                 protein = 0.00;
                                 fats = 0.00;
                                 for (UserFoodRecord u : trackList) {
                                     if (u.date.equals(tempDate)) {
                                         tempRecord = u;
                                     }
                                 }

                                 breakfast = tempRecord.getBreakfast();
                                 lunch = tempRecord.getLunch();
                                 dinner = tempRecord.getDinner();
                                 other = tempRecord.getOther();

                                 if (breakfast != null){
                                     for (Item item : breakfast){
                                         quantity = item.getQuantity();

                                         calories += Double.parseDouble(item.getNutrient().getCalories())*quantity;
                                         carbs += Double.parseDouble(item.getNutrient().getCarbs())*quantity;
                                         protein += Double.parseDouble(item.getNutrient().getProtein())*quantity;
                                         fats += Double.parseDouble(item.getNutrient().getFat())*quantity;
                                     }
                                 }

                                 if (lunch != null){
                                     for (Item item : lunch){
                                         quantity = item.getQuantity();
                                         calories += Double.parseDouble(item.getNutrient().getCalories())*quantity;
                                         carbs += Double.parseDouble(item.getNutrient().getCarbs())*quantity;
                                         protein += Double.parseDouble(item.getNutrient().getProtein())*quantity;
                                         fats += Double.parseDouble(item.getNutrient().getFat())*quantity;
                                     }
                                 }

                                 if (dinner != null){
                                     for (Item item : dinner){
                                         quantity = item.getQuantity();
                                         calories += Double.parseDouble(item.getNutrient().getCalories())*quantity;
                                         carbs += Double.parseDouble(item.getNutrient().getCarbs())*quantity;
                                         protein += Double.parseDouble(item.getNutrient().getProtein())*quantity;
                                         fats += Double.parseDouble(item.getNutrient().getFat())*quantity;
                                     }
                                 }

                                 if (other != null){
                                     for (Item item : other){
                                         quantity = item.getQuantity();
                                         calories += Double.parseDouble(item.getNutrient().getCalories())*quantity;
                                         carbs += Double.parseDouble(item.getNutrient().getCarbs())*quantity;
                                         protein += Double.parseDouble(item.getNutrient().getProtein())*quantity;
                                         fats += Double.parseDouble(item.getNutrient().getFat())*quantity;
                                     }
                                 }

                                 List<SliceValue> values = new ArrayList<>();
                                 if (calories != 0.00 || carbs != 0.00 || protein != 0.00 || fats != 0.00 ) {
                                     values.add(new SliceValue(Float.valueOf(String.valueOf(calories)), R.color.colorOrange/*getResources().getColor(R.color.colorOrange)*/).setLabel("Calories:" + df.format(calories)));
                                     values.add(new SliceValue(Float.valueOf(String.valueOf(carbs)), Color.BLUE).setLabel("Carbs:" + df.format(carbs)));
                                     values.add(new SliceValue(Float.valueOf(String.valueOf(protein)), Color.GREEN).setLabel("Proteins:" + df.format(protein)));
                                     values.add(new SliceValue(Float.valueOf(String.valueOf(fats)), Color.RED).setLabel("Fats:" + df.format(fats)));
                                 }else{
                                     values.add(new SliceValue(1, Color.GRAY).setLabel("Empty"));
                                 }
                                 pieChartData = new PieChartData(values);
                                 pieChartData.setHasLabels(true).setValueLabelTextSize(20);
                                 pieChart.setPieChartData(pieChartData);
                                 pieChart.invalidate();
                             }
                         }
                         previous = true;
                     }

                     @Override
                     public void onNothingSelected(AdapterView<?> adapterView) {
                         previous = false;
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
