package com.example.cheejin.fyp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText  username, userpwd;
    String tempName, tempPass;
    boolean access = false;
    private List<User> userlist = new ArrayList<User>();
    DatabaseReference databaseUsers;
    User currentUser;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        username = (EditText) findViewById(R.id.userName);
        userpwd = (EditText) findViewById(R.id.exercName);

        Button loginBtnClick = (Button)findViewById(R.id.loginBtn);
        loginBtnClick.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        if(checkValid()){
                            access = false;
                            if(currentUser != null) {
                                showToastValidMessage();
                            }
                        }else{
                            showToastInvalidMessage();
                        }
                    }
                }
        );

        Button regisBtnClick = (Button)findViewById(R.id.registerBtn);
        regisBtnClick.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent i = new Intent(MainActivity.this, RegisterPage.class);
                        startActivity(i);
                    }
                }
        );
    }

    private void showToastInvalidMessage() {
        Toast.makeText(this,"Wrong username or password" ,Toast.LENGTH_SHORT).show();
    }

    private void showToastValidMessage() {
        Toast.makeText(this,"Loading...",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MainActivity.this, HomePage.class);
        //bundle = new Bundle();
        //bundle.putSerializable("currentUser", (Serializable) currentUser);
        i.putExtra("currentUser", currentUser);
        startActivity(i);
    }

    private boolean checkValid() {
        tempName = username.getText().toString();
        tempPass = userpwd.getText().toString();

        for(User u: userlist){
            if(tempName.equals(u.username)&& tempPass.equals(u.userpwd)){
                currentUser = new User(u.id,u.username, u.userpwd, u.gender, u.age, u.height, u.weight, u.goal);
                access = true;
            }
        }

        return access;
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot c : children){
                    userlist.add(c.getValue(User.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
