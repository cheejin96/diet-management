package com.example.cheejin.fyp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterPage extends AppCompatActivity {

    DatabaseReference databaseUsers;

    ImageButton backBtn;
    Button regisBtnClick;
    EditText username;
    EditText userpwd;
    EditText userweight;
    EditText userheight;
    EditText userAge;

    RadioGroup genderGroup;
    RadioGroup goalGroup;
    RadioButton genderBtnClick, radioMaleBtn;
    RadioButton goalBtnClick, radioLossBtn;
    List<User> latestUserList = new ArrayList<>();
    Boolean cont = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis_page);

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        username = (EditText) findViewById(R.id.userName);
        userpwd = (EditText) findViewById(R.id.exercName);
        userweight = (EditText) findViewById(R.id.min);
        userheight = (EditText) findViewById(R.id.caloriesBurn);
        userAge = (EditText) findViewById(R.id.userAge);

        genderGroup = (RadioGroup) findViewById(R.id.radioGenderGroup);
        radioMaleBtn = findViewById(R.id.radioMaleBtn);
        radioMaleBtn.setChecked(true);
        goalGroup = (RadioGroup) findViewById(R.id.radioGoalGroup);
        radioLossBtn = findViewById(R.id.radioLossBtn);
        radioLossBtn.setChecked(true);

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot c : children) {
                    latestUserList.add(c.getValue(User.class));
                }

                regisBtnClick = (Button)findViewById(R.id.registerBtn);
                regisBtnClick.setOnClickListener(
                        new Button.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                genderBtnClick = findViewById(genderGroup.getCheckedRadioButtonId());
                                goalBtnClick = findViewById(goalGroup.getCheckedRadioButtonId());
                                addUser();
                            }
                        }
                );

                backBtn = findViewById(R.id.backBtn);
                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getFragmentManager().getBackStackEntryCount() != 0){
                            getFragmentManager().popBackStack();
                        }else{
                            RegisterPage.super.onBackPressed();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addUser() {
        if (latestUserList != null) {
            for (User u : latestUserList) {
                if (u.getUsername().equals(username.getText().toString())){
                    cont = false;
                    Toast.makeText(this,"Username taken",Toast.LENGTH_LONG).show();
                }
            }

            if (cont) {
                if (username.getText().toString().isEmpty() || userpwd.getText().toString().isEmpty() || ((String) genderBtnClick.getText()).isEmpty() ||
                        userAge.getText().toString().isEmpty() || userheight.getText().toString().isEmpty() || userweight.getText().toString().isEmpty() ||
                        ((String) goalBtnClick.getText()).isEmpty()) {

                    Toast.makeText(this, "Invalid input", Toast.LENGTH_LONG).show();
                } else if (userpwd.getText().toString().length() < 8 && !isValidPassword(userpwd.getText().toString().toString())) {
                    Toast.makeText(this, "Invalid password", Toast.LENGTH_LONG).show();
                } else if (Double.valueOf(userheight.getText().toString()) <= 150 || Double.valueOf(userheight.getText().toString()) >= 220) {
                    Toast.makeText(this, "Invalid height input", Toast.LENGTH_LONG).show();
                } else if (Double.valueOf(userweight.getText().toString()) <= 30) {
                    Toast.makeText(this, "Invalid weight input", Toast.LENGTH_LONG).show();
                } else {
                    String name = username.getText().toString();
                    String pwd = userpwd.getText().toString();
                    String gender = (String) genderBtnClick.getText();
                    Integer age = Integer.parseInt(userAge.getText().toString());
                    Double height = Double.parseDouble(userheight.getText().toString());
                    Double weight = Double.parseDouble(userweight.getText().toString());
                    String goal = (String) goalBtnClick.getText();

                    String id = databaseUsers.push().getKey();
                    User user = new User(id, name, pwd, gender, age, height, weight, goal);
                    databaseUsers.child(id).setValue(user);

                    Toast.makeText(this, "Successful register", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(RegisterPage.this, MainActivity.class);
                    startActivity(i);
                }
            }
        }
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }
}
