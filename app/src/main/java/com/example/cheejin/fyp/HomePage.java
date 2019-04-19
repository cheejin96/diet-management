package com.example.cheejin.fyp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.Serializable;


public class HomePage extends AppCompatActivity {

    private BottomNavigationView mainNav;
    private FrameLayout mainFrame;

    private AddFragment addFragment;
    private SearchFragment searchFragment;
    private ChartFragment chartFragment;
    private ChatFragment chatFragment;
    private MoreFragment moreFragment;

    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mainNav = (BottomNavigationView) findViewById(R.id.main_nav);
        mainFrame = (FrameLayout) findViewById(R.id.main_frame);

        addFragment = new AddFragment();
        searchFragment = new SearchFragment();
        chartFragment = new ChartFragment();
        chatFragment = new ChatFragment();
        moreFragment = new MoreFragment();

        setFragment(addFragment);
        mainNav.setSelectedItemId(R.id.nav_add);

        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_add :
                        setFragment(addFragment);
                        return true;

                    case R.id.nav_chart:
                        setFragment(chartFragment);
                        return true;

                    case R.id.nav_chat:
                        setFragment(chatFragment);
                        return true;

                    case R.id.nav_search:
                        setFragment(searchFragment);
                        return true;

                    case R.id.nav_more:
                        setFragment(moreFragment);
                        return true;

                    default:
                        return false;

                }
            }
        });
    }

    private void setFragment(Fragment fragment) {

        currentUser = (User) getIntent().getSerializableExtra("currentUser");

        if(currentUser != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("currentUserBundle", (Serializable) currentUser);
            fragment.setArguments(bundle);

            String backStateName = fragment.getClass().getName();

            FragmentManager manager = getSupportFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frame, fragment);
                fragmentTransaction.addToBackStack(backStateName);
                fragmentTransaction.commit();
            }
        }else{
            Toast.makeText(this,"null",Toast.LENGTH_SHORT).show();
        }
    }

}
