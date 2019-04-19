package com.example.cheejin.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class TipsForHelp extends AppCompatActivity {

    Button dietBtn, gainBtn, maintainBtn, dietExercBtn, gainExercBtn, maintainExercBtn;
    String tips;
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        dietBtn = findViewById(R.id.dietBtn);
        gainBtn = findViewById(R.id.gainBtn);
        maintainBtn = findViewById(R.id.maintainBtn);
        backBtn = findViewById(R.id.backBtn);
        dietExercBtn = findViewById(R.id.dietExercBtn);
        gainExercBtn = findViewById(R.id.gainExercBtn);
        maintainExercBtn = findViewById(R.id.maintainExercBtn);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().getBackStackEntryCount() != 0){
                    getFragmentManager().popBackStack();
                }else{
                    TipsForHelp.super.onBackPressed();
                }
            }
        });

        dietBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tips = "diet";
                Intent i = new Intent(TipsForHelp.this, DietTips.class);
                i.putExtra("diet",tips);
                startActivity(i);
            }
        });

        gainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tips = "gain";
                Intent i = new Intent(TipsForHelp.this, DietTips.class);
                i.putExtra("gain",tips);
                startActivity(i);
            }
        });

        maintainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tips = "maintain";
                Intent i = new Intent(TipsForHelp.this, DietTips.class);
                i.putExtra("maintain",tips);
                startActivity(i);
            }
        });

        dietExercBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tips = "diet exercise";
                Intent i = new Intent(TipsForHelp.this, DietTips.class);
                i.putExtra("diet exercise",tips);
                startActivity(i);
            }
        });

        gainExercBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tips = "gain exercise";
                Intent i = new Intent(TipsForHelp.this, DietTips.class);
                i.putExtra("gain exercise",tips);
                startActivity(i);
            }
        });

        maintainExercBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tips = "maintain exercise";
                Intent i = new Intent(TipsForHelp.this, DietTips.class);
                i.putExtra("maintain exercise",tips);
                startActivity(i);
            }
        });
    }
}
