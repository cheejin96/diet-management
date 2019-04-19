package com.example.cheejin.fyp;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment {

    TextView username, bmi;
    Button logoutBtn, editBtn, updateKgBtn, updateBtn, cancelBtn, updatePersonalInfoBtn, yesBtn, noBtn;
    View v;
    User currentUser;
    EditText kgUpdate;
    Double kgToUpdate;
    DatabaseReference databaseUsers;
    EditText userpwd;
    EditText userweight;
    EditText userheight;

    RadioGroup genderGroup;
    RadioGroup goalGroup;
    RadioButton genderBtnClick, goalBtnClick;
    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        v = inflater.inflate(R.layout.fragment_more, container, false);
        currentUser = (User) getArguments().getSerializable("currentUserBundle");
        username = (TextView) v.findViewById(R.id.userName);
        bmi = (TextView) v.findViewById(R.id.bmi);
        logoutBtn = (Button) v.findViewById(R.id.logoutBtn);
        editBtn = (Button) v.findViewById(R.id.editBtn);
        updateKgBtn = (Button) v.findViewById(R.id.updateKgButton);

        username.setText(currentUser.getUsername());
        Double userBMI = currentUser.getUserbmi();

        bmi.setText(String.format("%.2f",userBMI));
        setBMIColor(userBMI);

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                updateKgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        View updateKgView = getLayoutInflater().inflate(R.layout.activity_update_kg, null);
                        builder.setView(updateKgView);
                        final AlertDialog dialog = builder.create();
                        dialog.show();

                        kgUpdate = updateKgView.findViewById(R.id.kgToUpdate);
                        kgUpdate.setText(currentUser.getUserWeight().toString());
                        updateBtn = updateKgView.findViewById(R.id.updateBtn);

                        updateBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (kgUpdate.getText().toString()!= null) {
                                    kgToUpdate = Double.valueOf(kgUpdate.getText().toString());
                                    updateKg(kgToUpdate);
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Update successful" , Toast.LENGTH_SHORT).show();
                                    bmi.setText(String.format("%.2f", currentUser.getUserbmi()));
                                    setBMIColor(currentUser.getUserbmi());
                                }else{
                                    Toast.makeText(getContext(), "Please insert Kg to update", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        final View editPersonalInfo = getLayoutInflater().inflate(R.layout.activity_edit_personal_info, null);
                        builder.setView(editPersonalInfo);
                        final AlertDialog dialog = builder.create();
                        dialog.show();

                        updatePersonalInfoBtn = editPersonalInfo.findViewById(R.id.updateBtn);
                        cancelBtn = editPersonalInfo.findViewById(R.id.cancelBtn);

                        userpwd = (EditText) editPersonalInfo.findViewById(R.id.exercName);
                        userpwd.setText(currentUser.getUserpwd());
                        userweight = (EditText) editPersonalInfo.findViewById(R.id.min);
                        userweight.setText(currentUser.getUserWeight().toString());
                        userheight = (EditText) editPersonalInfo.findViewById(R.id.caloriesBurn);
                        userheight.setText(currentUser.getUserHeight().toString());

                        genderGroup = (RadioGroup) editPersonalInfo.findViewById(R.id.radioGenderGroup);
                        if (currentUser.getUserGender().toString().equals("Male")){
                            RadioButton maleBtn = editPersonalInfo.findViewById(R.id.radioMaleBtn);
                            maleBtn.setChecked(true);
                        }else if (currentUser.getUserGender().toString().equals("Female")){
                            RadioButton femaleBtn = editPersonalInfo.findViewById(R.id.radioFemaleBtn);
                            femaleBtn.setChecked(true);
                        }

                        goalGroup = (RadioGroup) editPersonalInfo.findViewById(R.id.radioGoalGroup);
                        if (currentUser.getUserGoal().toString().equals("Loss weight")){
                            RadioButton defaultLossBtn = editPersonalInfo.findViewById(R.id.radioLossBtn);
                            defaultLossBtn.setChecked(true);
                        }else if (currentUser.getUserGoal().toString().equals("Maintain weight")){
                            RadioButton defaultMainBtn = editPersonalInfo.findViewById(R.id.radioMaintBtn);
                            defaultMainBtn.setChecked(true);
                        }else if (currentUser.getUserGoal().toString().equals("Gain weight")){
                            RadioButton defaultGainBtn = editPersonalInfo.findViewById(R.id.radioGainBtn);
                            defaultGainBtn.setChecked(true);
                        }
                        updatePersonalInfoBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                genderBtnClick = (RadioButton) editPersonalInfo.findViewById(genderGroup.getCheckedRadioButtonId());
                                goalBtnClick = (RadioButton) editPersonalInfo.findViewById(goalGroup.getCheckedRadioButtonId());

                                if (username.getText().toString().isEmpty() || userpwd.getText().toString().isEmpty() || (genderBtnClick.getText().toString().isEmpty()) ||
                                        userheight.getText().toString().isEmpty() || userweight.getText().toString().isEmpty() || (goalBtnClick.getText().toString().isEmpty())) {

                                    Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_LONG).show();

                                } else if (userpwd.getText().toString().length()< 8 && !isValidPassword(userpwd.getText().toString().toString())){
                                    Toast.makeText(getContext(),"Invalid password",Toast.LENGTH_LONG).show();
                                }else if (Double.valueOf(userweight.getText().toString()) <= 150 || Double.valueOf(userweight.getText().toString()) >= 220){
                                    Toast.makeText(getContext(),"Invalid height input",Toast.LENGTH_LONG).show();
                                }else if(Double.valueOf(userweight.getText().toString()) <= 30){
                                    Toast.makeText(getContext(),"Invalid weight input",Toast.LENGTH_LONG).show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    View displayAlert = getLayoutInflater().inflate(R.layout.activity_sure_alert, null);
                                    builder.setView(displayAlert);
                                    final AlertDialog dialogAlert = builder.create();
                                    dialogAlert.show();

                                    yesBtn = displayAlert.findViewById(R.id.yesBtn);
                                    noBtn = displayAlert.findViewById(R.id.noBtn);

                                    yesBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String pwd = userpwd.getText().toString();
                                            String gender = (String) genderBtnClick.getText().toString();
                                            Double height = Double.parseDouble(userheight.getText().toString());
                                            Double weight = Double.parseDouble(userweight.getText().toString());
                                            String goal = (String) goalBtnClick.getText().toString();
                                            currentUser.setUserpwd(pwd);
                                            currentUser.setUserGender(gender);
                                            currentUser.setUserHeight(height);
                                            currentUser.setUserWeight(weight);
                                            currentUser.setUserGoal(goal);
                                            currentUser.updateBMI();
                                            databaseUsers.child(currentUser.id).setValue(currentUser);
                                            dialogAlert.dismiss();
                                            dialog.dismiss();
                                            Toast.makeText(getContext(), "Update successful", Toast.LENGTH_LONG).show();
                                            bmi.setText(String.format("%.2f", currentUser.getUserbmi()));
                                            setBMIColor(currentUser.getUserbmi());
                                        }
                                    });

                                    noBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialogAlert.dismiss();
                                        }
                                    });
                                }
                            }
                        });

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final View sureAlert = getLayoutInflater().inflate(R.layout.activity_sure_alert, null);
                builder.setView(sureAlert);
                final AlertDialog dialog = builder.create();
                dialog.show();

                yesBtn = sureAlert.findViewById(R.id.yesBtn);
                noBtn = sureAlert.findViewById(R.id.noBtn);

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(), MainActivity.class);
                        startActivity(i);
                    }
                });

                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

        return v;
    }

    private void setBMIColor(Double userBMI) {
        if (userBMI < 18.5){
            bmi.setTextColor(Color.DKGRAY);
        }else if (userBMI < 24.9){
            bmi.setTextColor(getResources().getColor(R.color.colorGreen));
        }else if (userBMI < 29.9){
            bmi.setTextColor(getResources().getColor(R.color.colorOrange));
        }else{
            bmi.setTextColor(Color.RED);
        }
    }

    private void updateKg(Double kg) {
        currentUser.setUserWeight(kg);
        currentUser.updateBMI();
        databaseUsers.child(currentUser.id).setValue(currentUser);
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
