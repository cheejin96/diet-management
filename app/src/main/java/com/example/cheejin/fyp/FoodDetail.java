package com.example.cheejin.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class FoodDetail extends AppCompatActivity {

    ImageButton backBtn;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        backBtn = (ImageButton) findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backFunction();
            }
        });
    }

    private void backFunction() {
        Intent i = new Intent(FoodDetail.this, SearchFoodToAdd.class);
        startActivity(i);
    }
}
