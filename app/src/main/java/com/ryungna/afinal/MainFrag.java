package com.ryungna.afinal;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainFrag extends AppCompatActivity {

    long lastPressed;
    Fragment fragment;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    fragment = new FragMe();
                    Toast.makeText(getBaseContext(),"me",Toast.LENGTH_SHORT);
                    switchFragment(fragment);
                    return true;

                case R.id.navigation_dashboard:
                    fragment = new FragGroup();
                    Toast.makeText(getBaseContext(),"G",Toast.LENGTH_SHORT);
                    switchFragment(fragment);
                    return true;

                case R.id.navigation_notifications:
                    fragment = new FragFriend();
                    Toast.makeText(getBaseContext(),"F",Toast.LENGTH_SHORT);
                    switchFragment(fragment);
                    return true;
            }
            return false;
        }

    };

    public void switchFragment(Fragment fragment){ //프래그먼트 바뀌는거
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content,fragment);
        transaction.commit();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frag);


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FragMe fragment = new FragMe(); //처음에 fragMe
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.commit();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }



    //프래그먼트 누르다가 뒤로가기 방지
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastPressed < 1500) {
            finish();
        }
        Toast.makeText(this,"한번더 누르면 종료",Toast.LENGTH_SHORT).show();
        lastPressed = System.currentTimeMillis();
    }


}
